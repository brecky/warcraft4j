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
package nl.salp.warcraft4j.clientdata.casc;

import nl.salp.warcraft4j.clientdata.*;
import nl.salp.warcraft4j.clientdata.casc.blte.BlteDataReader;
import nl.salp.warcraft4j.hash.JenkinsHash;
import nl.salp.warcraft4j.io.reader.CompositeDataReader;
import nl.salp.warcraft4j.io.reader.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public abstract class CascContext {
    /** The logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CascContext.class);
    private final ClientDataConfiguration clientDataConfig;
    private final Lock parseLock;
    private Index index;
    private EncodingFile encoding;
    private Root root;

    public CascContext(ClientDataConfiguration clientDataConfig) {
        this.clientDataConfig = clientDataConfig;
        this.parseLock = new ReentrantLock();
    }

    protected final ClientDataConfiguration getClientDataConfig() {
        return clientDataConfig;
    }

    protected abstract CascConfig getCascConfig();

    protected final EncodingFile getEncoding() {
        if (encoding == null) {
            parseLock.tryLock();
            try {
                if (encoding == null) {
                    encoding = parseEncoding();
                }
            } finally {
                parseLock.unlock();
            }
        }
        return encoding;
    }

    protected abstract Supplier<DataReader> getEncodingReader();

    protected EncodingFile parseEncoding() throws CascParsingException {
        Supplier<DataReader> readerSupplier = getEncodingReader();
        LOGGER.debug("Initialising encoding file (fileKey: {}, fileSize: {}).", getCascConfig().getStorageEncodingFileChecksum(), getCascConfig().getStorageEncodingFileSize());
        parseLock.lock();
        try (DataReader reader = readerSupplier.get()) {
            EncodingFile encodingFile = reader.readNext(new EncodingFileParser(getCascConfig().getExtractedEncodingFileSize()));
            LOGGER.debug("Successfully parsed encoding file with {} entries", encodingFile.getEntryCount());
            return encodingFile;
        } catch (IOException e) {
            throw new CascParsingException(format("Error parsing encoding file %s", getCascConfig().getExtractedEncodingFileChecksum().toHexString()), e);
        } finally {
            parseLock.unlock();
        }
    }

    protected Root parseRoot() throws CascParsingException {
        ContentChecksum contentChecksum = Optional.of(getCascConfig().getRootContentChecksum())
                .orElseThrow(() -> new CascParsingException(format("No checksum found for root file")));
        FileKey fileKey = getFileKey(contentChecksum)
                .orElseThrow(() -> new CascParsingException(format("No file key found for root file entry %s", contentChecksum.toHexString())));
        IndexEntry indexEntry = getIndexEntry(fileKey)
                .orElseThrow(() -> new CascParsingException(format("No index entry found for root file entry %s with key %s", contentChecksum.toHexString(), fileKey
                        .toHexString())));
        LOGGER.debug("Initialising root file (contentChecksum: {}, fileKey: {}) from {} bytes of data in data.{} at offset {}",
                contentChecksum.toHexString(), fileKey.toHexString(), indexEntry.getFileSize(), format("%03d", indexEntry.getFileNumber()), indexEntry.getDataFileOffset());

        parseLock.lock();
        try (DataReader reader = getFileDataReader(indexEntry)) {
            root = reader.readNext(new RootFileParser());
        } catch (IOException e) {
            throw new CascParsingException(format("Error parsing root file from data file %d at offset %d", indexEntry.getFileNumber(), indexEntry.getDataFileOffset()), e);
        } finally {
            parseLock.unlock();
        }
        LOGGER.debug("Successfully parsed root file with {} entries", root.getHashCount());
        return root;
    }

    protected abstract Index parseIndex() throws CascParsingException;


    protected final Index getIndex() {
        if (index == null) {
            parseLock.tryLock();
            try {
                if (index == null) {
                    LOGGER.debug("Parsing indices");
                    index = parseIndex();
                }
            } finally {
                parseLock.unlock();
            }
        }
        return index;
    }

    /**
     * Get the parsed root.
     *
     * @return The {@link Root}.
     */
    protected final Root getRoot() {
        if (root == null) {
            parseLock.tryLock();
            try {
                if (root == null) {
                    LOGGER.debug("Parsing root)");
                    root = parseRoot();
                }
            } finally {
                parseLock.unlock();
            }
        }
        return root;
    }

    /**
     * Get the (preferred) branch for which the current context is configured.
     *
     * @return The {@link Branch}.
     */
    public Branch getBranch() {
        return clientDataConfig.getBranch();
    }

    /**
     * Get the (preferred) region for which the current context is configured.
     *
     * @return The {@link Region}.
     */
    public Region getRegion() {
        return clientDataConfig.getRegion();
    }

    /**
     * Get the (preferred) locale for which the current context is configured.
     *
     * @return The {@link Locale}.
     */
    public Locale getLocale() {
        return clientDataConfig.getLocale();
    }

    /**
     * Get the WoW version of the build/data the current context is working on.
     *
     * @return The version.
     */
    public String getVersion() {
        return getCascConfig().getVersion();
    }

    /**
     * Get the content checksums associated for a hash.
     *
     * @param filenameHash The hash to get the checksums for.
     *
     * @return List containing all checksums for the hash or an empty list if no checksums were associated with the hash.
     */
    public List<ContentChecksum> getContentChecksums(long filenameHash) {
        return getRoot().getContentChecksums(filenameHash);
    }

    /**
     * TODO Remove me!
     *
     * @return
     */
    @Deprecated
    public Collection<Long> getRootHashes() {
        return getRoot().getHashes();
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
     * TODO Remove me!
     *
     * @return
     */
    @Deprecated
    public Collection<ContentChecksum> getEncodingContentChecksums() {
        return getEncoding().getEntries().stream().map(EncodingEntry::getContentChecksum).collect(Collectors.toSet());
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
     * TODO Remove me!
     *
     * @return
     */
    @Deprecated
    public Collection<FileKey> getIndexFileKeys() {
        return getIndex().getFileKeys();
    }

    /**
     * Get the {@link DataReaderProvider} to use for getting the data readers to use.
     *
     * @return The {@link DataReaderProvider}.
     */
    protected abstract DataReaderProvider getDataReaderProvider();

    /**
     * Get the data reader for an index entry.
     *
     * @param entry The index entry.
     *
     * @return The data reader for the raw data.
     *
     * @throws CascParsingException When the data reader could not be created.
     */
    protected Supplier<DataReader> getDataReader(IndexEntry entry) throws CascParsingException {
        LOGGER.trace("Getting data reader for file key {} (uri: {}, datafile: {}, offset: {}, size: {})",
                entry.getFileKey().toHexString(), getDataFileUri(entry).orElse("#error#"), entry.getFileNumber(), entry.getDataFileOffset(), entry.getFileSize());
        return Optional.ofNullable(entry)
                .flatMap(this::getDataFileUri)
                .map(file -> getDataReader(file, entry.getDataFileOffset(), entry.getFileSize()))
                .orElseThrow(() -> new CascEntryNotFoundException(format("Unable to get the data reader for entry %s in data file %d", entry.getFileKey(), entry.getFileNumber())));
    }

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
     * Get a {@link DataReader} for an extracted file, referenced by the hashcode of the filename.
     *
     * @param hashCode The hash code of the filename.
     *
     * @return The data reader.
     *
     * @throws CascEntryNotFoundException When the files could not be resolved for the hash code.
     * @throws CascParsingException       When there is a problem creating a data reader for the file.
     */
    public DataReader getFileDataReader(long hashCode) throws CascParsingException, CascEntryNotFoundException {
        return Optional.of(hashCode)
                .map(this::getIndexEntries)
                .filter(indices -> !indices.isEmpty())
                .map(this::getFileDataReader)
                .orElseThrow(() -> new CascEntryNotFoundException(format("No entry found for a file with hashcode %d", hashCode)));
    }

    /**
     * Get a {@link DataReader} for an extracted file, referenced by the filename.
     *
     * @param filename The name of the file.
     *
     * @return The data reader.
     *
     * @throws CascEntryNotFoundException When the files could not be resolved for the hash code.
     * @throws CascParsingException       When there is a problem creating a data reader for the file.
     */
    public DataReader getFileDataReader(String filename) throws CascParsingException, CascEntryNotFoundException {
        return Optional.ofNullable(filename)
                .map(CascContext::hashFilename)
                .map(this::getFileDataReader)
                .orElseThrow(() -> new CascEntryNotFoundException(format("No entry found for a file with filename %s", filename)));
    }

    /**
     * Get a data reader for an extracted file referenced by 1 or more index entries.
     *
     * @param indexEntries The index entries pointing to the file segments.
     *
     * @return The data reader for the extract file.
     *
     * @throws CascParsingException When the data reader cannot be created for the index entries.
     */
    protected DataReader getFileDataReader(List<IndexEntry> indexEntries) throws CascParsingException {
        DataReader dataReader;
        if (indexEntries == null || indexEntries.isEmpty()) {
            throw new CascParsingException("Cannot create a data reader with no index entries provided.");
        } else if (indexEntries.size() == 1) {
            dataReader = getFileDataReader(indexEntries.get(0));
        } else {
            try {
                List<Supplier<? extends DataReader>> dataReaders = indexEntries.stream()
                        .map(this::getFileDataReaderSupplier)
                        .collect(Collectors.toList());
                dataReader = new CompositeDataReader(dataReaders);
            } catch (IOException e) {
                throw new CascParsingException(format("Cannot create a composite data reader for %d sources", indexEntries.size()), e);
            }
        }
        return dataReader;
    }

    protected DataReader getFileDataReader(IndexEntry indexEntry) throws CascParsingException {
        if (indexEntry == null) {
            throw new CascParsingException("Cannot create a data reader with no index entries provided.");
        }
        LOGGER.trace("Getting BLTE data reader for {} (uri: {}, datafile: {}, offset: {}, datasize: {})", indexEntry.getFileKey().toHexString(),
                getDataFileUri(indexEntry).orElse("#error#"), indexEntry.getFileNumber(), indexEntry.getDataFileOffset(), indexEntry.getFileSize());
        return new BlteDataReader(getDataReader(indexEntry), indexEntry.getFileSize());
    }

    protected Supplier<DataReader> getFileDataReaderSupplier(IndexEntry indexEntry) throws CascParsingException {
        return () -> getFileDataReader(indexEntry);
    }

    protected List<IndexEntry> getIndexEntries(String path) {
        return Optional.ofNullable(path)
                .map(CascContext::hashFilename)
                .map(this::getIndexEntries)
                .orElse(Collections.emptyList());
    }

    protected List<IndexEntry> getIndexEntries(long hashCode) {
        return Optional.ofNullable(getContentChecksums(hashCode))
                .map(c -> c.stream().map(this::getFileKey).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .map(c -> c.stream().map(this::getIndexEntry).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    protected static long hashFilename(String filename) {
        long hash;
        if (isEmpty(filename)) {
            hash = 0;
        } else {
            byte[] data = filename.replace('/', '\\').toUpperCase().getBytes(StandardCharsets.US_ASCII);
            hash = JenkinsHash.hashLittle2(data, data.length);
        }
        return hash;
    }
}
