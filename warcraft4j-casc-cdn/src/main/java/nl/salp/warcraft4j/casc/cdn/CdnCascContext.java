/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.casc.*;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.hash.JenkinsHash;
import nl.salp.warcraft4j.io.CompositeDataReader;
import nl.salp.warcraft4j.io.DataReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * CDN based CASC context for accessing CASC stored files and their reference trees.
 *
 * @author Barre Dijkstra
 */
public abstract class CdnCascContext {
    /** The logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CdnCascContext.class);
    /** The {@link Warcraft4jConfig} the context has been initialised with. */
    private final Warcraft4jConfig warcraft4jConfig;
    /** The resolved filename hashes, indexed by the filename. */
    private final Map<String, Long> hashes;
    /** The resolved filenames, indexed by the filename hashes. */
    private final Map<Long, String> filenames;
    /** The parsed index file(s). */
    private Index index;
    /** The parsed encoding file. */
    private EncodingFile encoding;
    /** The parsed root file. */
    private RootFile rootFile;

    /**
     * Create a new CdnCascContext.
     *
     * @param warcraft4jConfig The {@link Warcraft4jConfig} to use.
     */
    protected CdnCascContext(Warcraft4jConfig warcraft4jConfig) {
        LOGGER.debug("Created CASC context for branch {}, region {} and locale {} (wow directory: {}, online: {}, caching: {})",
                warcraft4jConfig.getBranch(), warcraft4jConfig.getRegion(), warcraft4jConfig.getLocale(), warcraft4jConfig.getWowInstallationDirectory(),
                warcraft4jConfig.isOnline(), warcraft4jConfig.isCaching());
        this.warcraft4jConfig = warcraft4jConfig;
        this.hashes = new HashMap<>();
        this.filenames = new HashMap<>();
    }

    /**
     * Get the Warcraft4J configuration used for the CASC context.
     *
     * @return The Warcraft4J configuration.
     */
    public final Warcraft4jConfig getWarcraft4jConfig() {
        return warcraft4jConfig;
    }

    /**
     * Get the parsed CASC config.
     *
     * @return The CASC config.
     */
    public abstract CdnCascConfig getCdnCascConfig();

    /**
     * Get the supplier for the {@link DataReader} for the encoding file.
     *
     * @return The supplier. for the reader.
     */
    protected abstract Supplier<DataReader> getEncodingReader();

    /**
     * Parse the encoding file.
     *
     * @return The parsed encoding file.
     *
     * @throws CascParsingException When parsing of the encoding file failed.
     */
    protected EncodingFile parseEncoding() throws CascParsingException {
        Supplier<DataReader> readerSupplier = getEncodingReader();
        LOGGER.debug("Initialising encoding file (fileKey: {}, fileSize: {}).", getCdnCascConfig().getStorageEncodingFileChecksum(), getCdnCascConfig()
                .getStorageEncodingFileSize());
        try (DataReader reader = readerSupplier.get()) {
            EncodingFile encodingFile = new EncodingFileParser().parse(reader, getCdnCascConfig().getExtractedEncodingFileSize());
            return encodingFile;
        } catch (IOException e) {
            throw new CascParsingException(format("Error parsing encoding file %s", getCdnCascConfig().getExtractedEncodingFileChecksum().toHexString()), e);
        }
    }

    /**
     * Parse the root file.
     *
     * @return The parsed root file.
     *
     * @throws CascParsingException When parsing of the rootFile failed.
     */
    protected RootFile parseRoot() throws CascParsingException {
        ContentChecksum contentChecksum = Optional.of(getCdnCascConfig().getRootContentChecksum())
                .orElseThrow(() -> new CascParsingException("No checksum found for root file"));
        FileKey fileKey = getFileKey(contentChecksum)
                .orElseThrow(() -> new CascParsingException(format("No file key found for root file entry %s", contentChecksum.toHexString())));
        IndexEntry indexEntry = getIndexEntry(fileKey)
                .orElseThrow(() -> new CascParsingException(format("No index entry found for root file entry %s with key %s", contentChecksum.toHexString(), fileKey
                        .toHexString())));
        LOGGER.debug("Initialising root file (contentChecksum: {}, fileKey: {}) from {} bytes of data in data.{} at offset {}",
                contentChecksum.toHexString(), fileKey.toHexString(), indexEntry.getFileSize(), format("%03d", indexEntry.getFileNumber()), indexEntry.getDataFileOffset());

        try (DataReader reader = getFileDataReader(indexEntry, contentChecksum)) {
            return new RootFileParser().parse(reader);
        } catch (IOException e) {
            throw new CascParsingException(format("Error parsing root file from data file %d at offset %d", indexEntry.getFileNumber(), indexEntry.getDataFileOffset()), e);
        }
    }

    /**
     * Get the parsed encoding file.
     *
     * @return The parsed encoding file.
     */
    protected final EncodingFile getEncoding() {
        if (encoding == null) {
            LOGGER.debug("Parsing encoding file");
            encoding = parseEncoding();
            LOGGER.debug("Successfully initialised encoding file with {} entries", encoding.getEntryCount());
        }
        return encoding;
    }

    /**
     * Get all encoding entries.
     *
     * @return The encoding entries.
     */
    public final Collection<EncodingEntry> getEncodingEntries() {
        return Collections.unmodifiableCollection(getEncoding().getEntries());
    }

    /**
     * Get the encoding entry for a root entry.
     *
     * @param rootEntry The root entry.
     *
     * @return Optional containing the encoding entry for the root entry.
     */
    public final Optional<EncodingEntry> getEncodingEntry(RootEntry rootEntry) {
        return Optional.ofNullable(rootEntry)
                .map(RootEntry::getContentChecksum)
                .flatMap(this::getEncodingEntry);
    }

    /**
     * Get the encoding entry for a content checksum.
     *
     * @param contentChecksum The content checksum.
     *
     * @return Optional containing the encoding entry for the content checksum.
     */
    public final Optional<EncodingEntry> getEncodingEntry(ContentChecksum contentChecksum) {
        return Optional.ofNullable(contentChecksum)
                .flatMap(getEncoding()::getEncodingEntry);
    }

    /**
     * Parse the index.
     *
     * @return The parsed index.
     *
     * @throws CascParsingException When parsing failed.
     */
    protected abstract Index parseIndex() throws CascParsingException;

    /**
     * Get the parsed index.
     *
     * @return The index.
     *
     * @throws CascParsingException When parsing failed.
     */
    protected final Index getIndex() throws CascParsingException {
        if (index == null) {
            LOGGER.debug("Parsing index files");
            index = parseIndex();
            LOGGER.debug("Successfully initialised index files with {} entries", index.getEntryCount());
        }
        return index;
    }

    /**
     * Get all index entries.
     *
     * @return The index entries.
     */
    public final Collection<IndexEntry> getIndexEntries() {
        return Collections.unmodifiableCollection(getIndex().getEntries());
    }

    /**
     * Get all index entries for an encoding entry.
     *
     * @param encodingEntry The encoding entry.
     *
     * @return The index entries.
     */
    public final List<IndexEntry> getIndexEntries(EncodingEntry encodingEntry) {
        return Optional.ofNullable(encodingEntry)
                .map(EncodingEntry::getFileKeys)
                .map(this::getIndexEntries)
                .orElse(Collections.emptyList());
    }

    /**
     * Get all index entries that are referenced by a list of file keys.
     *
     * @param fileKeys The file keys.
     *
     * @return The index entries for the file keys.
     */
    public final List<IndexEntry> getIndexEntries(List<FileKey> fileKeys) {
        return Optional.ofNullable(fileKeys)
                .map(keys -> keys.stream()
                                .map(key -> getIndex().getEntry(key))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toList())
                ).orElse(Collections.emptyList());
    }

    /**
     * Get the parsed rootFile.
     *
     * @return The {@link RootFile}.
     */
    protected final RootFile getRootFile() {
        if (rootFile == null) {
            LOGGER.debug("Parsing rootFile");
            rootFile = parseRoot();
            LOGGER.debug("Successfully initialised root file with {} entries", rootFile.getHashCount());
        }
        return rootFile;
    }

    /**
     * Get all root entries.
     *
     * @return The root entries.
     */
    public Collection<RootEntry> getRootEntries() {
        return Collections.unmodifiableCollection(getRootFile().getEntries());
    }

    /**
     * Get all root entries for a filename hash.
     *
     * @param filenameHash The filename hash.
     *
     * @return All root entries for the filename hash.
     */
    public List<RootEntry> getRootEntries(long filenameHash) {
        return getRootFile().getEntries(filenameHash);
    }

    /**
     * Get the (preferred) branch for which the current context is configured.
     *
     * @return The {@link Branch}.
     */
    public Branch getBranch() {
        return warcraft4jConfig.getBranch();
    }

    /**
     * Get the (preferred) region for which the current context is configured.
     *
     * @return The {@link Region}.
     */
    public Region getRegion() {
        return warcraft4jConfig.getRegion();
    }

    /**
     * Get the (preferred) locale for which the current context is configured.
     *
     * @return The {@link Locale}.
     */
    public Locale getLocale() {
        return warcraft4jConfig.getLocale();
    }

    /**
     * Get the WoW version of the build/data the current context is working on.
     *
     * @return The version.
     */
    public String getVersion() {
        return getCdnCascConfig().getVersion();
    }

    /**
     * Check if a filename hash refers to registered data in the CASC with all data segment present.
     *
     * @param hash The filename hash.
     *
     * @return {@code true} if the filename hash is registered and all data segments are present in the CASC.
     */
    public boolean isRegisteredData(long hash) {
        List<ContentChecksum> contentChecksum = getContentChecksums(hash);
        List<FileKey> fileKeys = contentChecksum.stream()
                .map(this::getFileKey)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        List<IndexEntry> indexEntries = fileKeys.stream()
                .map(this::getIndexEntry)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return !indexEntries.isEmpty();
    }

    /**
     * Check if a filename hash is registered in the CASC.
     *
     * @param hash The filename hash.
     *
     * @return {@code true} if the filename hash is registered in the CASC.
     */
    public boolean isRegistered(long hash) {
        return getFilename(hash)
                .map(StringUtils::isNotEmpty)
                .orElse(getRootFile().isEntryAvailable(hash));
    }

    /**
     * Check if a filename refers to registered data in the CASC with all data segment present.
     *
     * @param filename The filename.
     *
     * @return {@code true} if the filename is registered and all data segments are present in the CASC.
     */
    public boolean isRegisteredData(String filename) {
        return getHash(filename)
                .map(this::isRegisteredData)
                .orElse(false);
    }

    /**
     * Check if a filename is registered in the CASC.
     *
     * @param filename The filename.
     *
     * @return {@code true} if the filename is registered in the hash.
     */
    public boolean isRegistered(String filename) {
        return getHash(filename).isPresent();
    }

    /**
     * Get a resolved filename for a filename hash.
     *
     * @param hash The filename hash.
     *
     * @return Optional containing the filename if it has been resolved.
     */
    public Optional<String> getFilename(long hash) {
        return Optional.ofNullable(filenames.get(hash));
    }

    /**
     * Get the hash for a filename if the hash refers to an entry available in the context, resolving the hash if possible.
     *
     * @param filename The filename.
     *
     * @return Optional containing the hash of the filename if the hash refers to an entry available in the context.
     */
    public Optional<Long> getHash(String filename) {
        Optional<Long> hash;
        String cleanedFilename = cleanFilename(filename);
        if (isEmpty(cleanedFilename)) {
            hash = Optional.empty();
        } else if (hashes.containsKey(cleanedFilename)) {
            hash = Optional.of(hashes.get(cleanedFilename));
        } else {
            long filenameHash = hashFilename(cleanedFilename);
            if (getRootFile().isEntryAvailable(filenameHash)) {
                hashes.putIfAbsent(cleanedFilename, filenameHash);
                filenames.putIfAbsent(filenameHash, cleanedFilename);
                hash = Optional.of(filenameHash);
            } else {
                hash = Optional.empty();
            }
        }
        return hash;
    }

    /**
     * Get all registered filename hashes.
     *
     * @return The hashes.
     */
    public Set<Long> getHashes() {
        return getRootFile().getHashes();
    }

    /**
     * Get all registered filename hashes that have been resolved to filenames.
     *
     * @return The resolved filename hashes.
     */
    public Set<Long> getResolvedHashes() {
        return Collections.unmodifiableSet(filenames.keySet());
    }

    /**
     * Resolve a hash to a filename.
     *
     * @param filename The filename.
     * @param hash     The hash for the filename.
     */
    public void resolve(String filename, long hash) {
        if (isNotEmpty(filename)) {
            String name = cleanFilename(filename);
            if (isRegistered(hash)) {
                filenames.put(hash, name);
                hashes.put(name, hash);
            }
        }
    }

    /**
     * Get all resolved filenames.
     *
     * @return The resolved filenames.
     */
    public Set<String> getResolvedFilenames() {
        return Collections.unmodifiableSet(hashes.keySet());
    }

    /**
     * Get the content checksums associated for a hash.
     *
     * @param filenameHash The hash to get the checksums for.
     *
     * @return List containing all checksums for the hash or an empty list if no checksums were associated with the hash.
     */
    public List<ContentChecksum> getContentChecksums(long filenameHash) {
        return getRootFile().getContentChecksums(filenameHash);
    }

    /**
     * Get the file key for a content checksum.
     *
     * @param contentChecksum The content checksum.
     *
     * @return {@code Optional} of the file key, being empty if none was found for the checksum.
     */
    public Optional<FileKey> getFileKey(ContentChecksum contentChecksum) {
        return getEncoding().getFileKey(contentChecksum);
    }

    /**
     * Get the index entry for a file key.
     *
     * @param fileKey The file key.
     *
     * @return {@code Optional} of the index entry, being empty if none was found for the file key.
     */
    public Optional<IndexEntry> getIndexEntry(FileKey fileKey) {
        return getIndex().getEntry(fileKey);
    }

    /**
     * Get the {@link DataReaderProvider} to use for getting the data readers to use.
     *
     * @return The {@link DataReaderProvider}.
     */
    protected abstract DataReaderProvider getDataReaderProvider();

    /**
     * Get a supplier for a raw data reader for a file referenced by an index entry.
     *
     * @param entry The index entry.
     *
     * @return The supplier for the reader.
     *
     * @throws CascParsingException       When the data reader could not be created.
     * @throws CascEntryNotFoundException When there is no data for the index entry.
     */
    public Supplier<DataReader> getDataReader(IndexEntry entry) throws CascParsingException, CascEntryNotFoundException {
        LOGGER.trace("Getting data reader for file key {} (uri: {}, datafile: {}, offset: {}, size: {})",
                entry.getFileKey().toHexString(), getDataFileUri(entry).orElse("#error#"), entry.getFileNumber(), entry.getDataFileOffset(), entry.getFileSize());
        return Optional.ofNullable(entry)
                .flatMap(this::getDataFileUri)
                .map(file -> getDataReader(file, entry.getDataFileOffset(), entry.getFileSize()))
                .orElseThrow(() -> new CascEntryNotFoundException(format("Unable to get the data reader for entry %s in data file %d", entry.getFileKey(), entry.getFileNumber())));
    }

    /**
     * Get a supplier for a raw data reader for a file inside a data file.
     *
     * @param dataFile       The path of the data file.
     * @param dataFileOffset The offset of the start of the data in the data file.
     * @param fileSize       The size of the contained file.
     *
     * @return The supplier for the reader.
     *
     * @throws CascParsingException When the data reader could not be created.
     */
    protected Supplier<DataReader> getDataReader(String dataFile, long dataFileOffset, long fileSize) throws CascParsingException {
        LOGGER.trace("Getting data reader for file key {} (offset: {}, size: {})", dataFile, dataFileOffset, fileSize);
        return getDataReaderProvider().getDataReader(dataFile, dataFileOffset, fileSize);
    }

    /**
     * Get the URI for the data file that contains the index entry.
     *
     * @param entry The index entry.
     *
     * @return {@code Optional} of the data file URI, being empty if none was found for the index entry.
     */
    protected abstract Optional<String> getDataFileUri(IndexEntry entry);

    /**
     * Get a {@link DataReader} for a file extracted from the CASC, referenced by the hashcode of the filename.
     *
     * @param hashCode The hash code of the filename.
     *
     * @return The data reader.
     *
     * @throws CascParsingException       When there is a problem creating a data reader for the file.
     * @throws CascEntryNotFoundException When the files could not be resolved for the hash code.
     */
    public DataReader getFileDataReader(long hashCode) throws CascParsingException, CascEntryNotFoundException {
        LOGGER.trace("Getting DataReader for extracted file with filename hashcode {}", hashCode);
        return Optional.of(hashCode)
                .map(this::getIndexEntries)
                .filter(indices -> !indices.isEmpty())
                .map(this::getFileDataReader)
                .orElseThrow(() -> new CascEntryNotFoundException(format("No entry found for a file with hashcode %d", hashCode)));
    }

    /**
     * Get a {@link DataReader} for a file extracted from the CASC, referenced by the filename.
     *
     * @param filename The name of the file.
     *
     * @return The data reader.
     *
     * @throws CascParsingException       When there is a problem creating a data reader for the file.
     * @throws CascEntryNotFoundException When the files could not be resolved for the filename.
     */
    public DataReader getFileDataReader(String filename) throws CascParsingException, CascEntryNotFoundException {
        LOGGER.trace("Getting DataReader for extracted file with filename {}", filename);
        return getHash(filename)
                .map(this::getFileDataReader)
                .orElseThrow(() -> new CascEntryNotFoundException(format("No entry found for a file with filename %s", filename)));
    }

    /**
     * Get a {@link DataReader} for a file extracted from the CASC, referenced by the checksum of the file's content.
     *
     * @param contentChecksum The {@link ContentChecksum} containing a checksum of the file.
     *
     * @return The data reader.
     *
     * @throws CascParsingException       When there is a problem creating a data reader for the file.
     * @throws CascEntryNotFoundException When the files could not be resolved for the content checksum.
     */
    public DataReader getFileDataReader(ContentChecksum contentChecksum) throws CascParsingException, CascEntryNotFoundException {
        LOGGER.trace("Getting DataReader for extracted file with content checksum {}", contentChecksum.toHexString());
        return Optional.ofNullable(contentChecksum)
                .flatMap(this::getEncodingEntry)
                .map(EncodingEntry::getFileKeys)
                .map(this::getIndexEntries)
                .map(this::getFileDataReader)
                .orElseThrow(() -> new CascEntryNotFoundException(format("No entry found for a file with content checksum %s", contentChecksum)));
    }

    /**
     * Get a {@link DataReader} for a file extracted from the CASC, referenced by 1 or more index entries.
     *
     * @param indexEntries The index entries referencing the file segments.
     *
     * @return The data reader for the extract file.
     *
     * @throws CascParsingException       When the data reader cannot be created for the index entries.
     * @throws CascEntryNotFoundException When there is no data for one or more index entries.
     */
    public DataReader getFileDataReader(List<IndexEntry> indexEntries) throws CascParsingException, CascEntryNotFoundException {
        DataReader dataReader;
        if (indexEntries == null || indexEntries.isEmpty()) {
            throw new CascParsingException("Cannot create a data reader with no index entries provided.");
        } else if (indexEntries.size() == 1) {
            LOGGER.trace("Getting DataReader for extracted file with file key {}", indexEntries.get(0).getFileKey().toHexString());
            dataReader = getFileDataReader(indexEntries.get(0));
        } else {
            LOGGER.trace("Getting DataReader for extracted file with {} file keys: {}", indexEntries.size(), indexEntries.stream()
                    .map(IndexEntry::getFileKey)
                    .map(FileKey::toHexString)
                    .collect(Collectors.joining(",")));
            List<Supplier<? extends DataReader>> dataReaders = indexEntries.stream()
                    .map(this::getFileDataReaderSupplier)
                    .collect(Collectors.toList());
            dataReader = new CompositeDataReader(dataReaders);
        }
        return dataReader;
    }

    /**
     * Get a {@link DataReader} for a file, or file segment, extracted from the CASC, referenced by an index entry.
     *
     * @param indexEntry The index entry referencing the file (segment).
     *
     * @return The data reader.
     *
     * @throws CascParsingException       When the data reader cannot be created for the index entry.
     * @throws CascEntryNotFoundException When there is no data for the index entry.
     */
    public DataReader getFileDataReader(IndexEntry indexEntry) throws CascParsingException, CascEntryNotFoundException {
        if (indexEntry == null) {
            throw new CascParsingException("Cannot create a data reader with no index entries provided.");
        }
        LOGGER.trace("Getting BLTE data reader for {} (uri: {}, datafile: {}, offset: {}, datasize: {})", indexEntry.getFileKey().toHexString(),
                getDataFileUri(indexEntry).orElse("#error#"), indexEntry.getFileNumber(), indexEntry.getDataFileOffset(), indexEntry.getFileSize());
        return new BlteDataReader(getDataReader(indexEntry), indexEntry.getFileSize());
    }

    /**
     * Get a {@link DataReader} for a file, or file segment, extracted from the CASC and validated against a content checksum, referenced by an index entries.
     *
     * @param indexEntry      The index entry referencing the file (segment).
     * @param contentChecksum The content checksum of the file (segment) data.
     *
     * @return The data reader.
     *
     * @throws CascParsingException       When the data reader cannot be created for the index entry.
     * @throws CascEntryNotFoundException When there is no data for the index entry.
     */
    public DataReader getFileDataReader(IndexEntry indexEntry, ContentChecksum contentChecksum) throws CascParsingException, CascEntryNotFoundException {
        if (indexEntry == null) {
            throw new CascParsingException("Cannot create a data reader with no index entries provided.");
        }
        LOGGER.trace("Getting BLTE data reader for {} with checksum {} (uri: {}, datafile: {}, offset: {}, datasize: {})", indexEntry.getFileKey().toHexString(),
                contentChecksum.toHexString(), getDataFileUri(indexEntry).orElse("#error#"), indexEntry.getFileNumber(),
                indexEntry.getDataFileOffset(), indexEntry.getFileSize());
        return new BlteDataReader(getDataReader(indexEntry), indexEntry.getFileSize(), contentChecksum);
    }

    /**
     * Get a supplier for a {@link DataReader} for a file extracted from the CASC, referenced by 1 or more index entries.
     *
     * @param indexEntry The index entry referencing the file (segment).
     *
     * @return The supplier for the data reader for the extracted file (segment).
     *
     * @throws CascParsingException When the supplier could not be created.
     */
    public Supplier<DataReader> getFileDataReaderSupplier(IndexEntry indexEntry) throws CascParsingException {
        return () -> getFileDataReader(indexEntry);
    }

    /**
     * Get the index entries for a file.
     *
     * @param filename The filename.
     *
     * @return The index entries for the file.
     */
    public List<IndexEntry> getIndexEntries(String filename) {
        return getHash(filename)
                .map(this::getIndexEntries)
                .orElse(Collections.emptyList());
    }

    /**
     * Get the index entries for a file.
     *
     * @param hashCode The hashcode of the filename.
     *
     * @return The index entries for the file.
     */
    public List<IndexEntry> getIndexEntries(long hashCode) {
        return Optional.ofNullable(getContentChecksums(hashCode))
                .map(c -> c.stream().map(this::getFileKey).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .map(c -> c.stream().map(this::getIndexEntry).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    /**
     * Clean a filename to match the format used for hashing / referencing.
     *
     * @param filename The filename.
     *
     * @return The cleaned filename.
     */
    protected static String cleanFilename(String filename) {
        String cleaned;
        if (isEmpty(filename)) {
            cleaned = filename;
        } else {
            cleaned = filename.replace('/', '\\').toUpperCase();
        }
        return cleaned;
    }

    /**
     * Create a hash for a filename.
     *
     * @param filename The filename.
     *
     * @return The hash.
     */
    public static long hashFilename(String filename) {
        long hash;
        if (isEmpty(filename)) {
            hash = 0;
        } else {
            ;
            byte[] data = cleanFilename(filename).getBytes(StandardCharsets.US_ASCII);
            hash = JenkinsHash.hashLittle2(data, data.length);
        }
        return hash;
    }
}
