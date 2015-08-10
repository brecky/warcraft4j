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

import nl.salp.warcraft4j.clientdata.ClientDataConfiguration;
import nl.salp.warcraft4j.clientdata.Region;
import nl.salp.warcraft4j.clientdata.casc.blte.BlteFile;
import nl.salp.warcraft4j.clientdata.casc.cdn.CdnCascConfig;
import nl.salp.warcraft4j.clientdata.casc.cdn.CdnDataReaderProvider;
import nl.salp.warcraft4j.clientdata.casc.local.FileDataReaderProvider;
import nl.salp.warcraft4j.clientdata.casc.local.LocalCascConfig;
import nl.salp.warcraft4j.clientdata.casc.local.LocalIndexFile;
import nl.salp.warcraft4j.clientdata.casc.local.LocalIndexParser;
import nl.salp.warcraft4j.hash.JenkinsHash;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.io.reader.CompositeDataReader;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.reader.RandomAccessDataReader;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 *
 * CASC
 *  + getFileReader(path | hash)
 *  + getFileSize(path | hash)
 *  - map[path,hash]
 *  - casccontext (local | cdn | extracted)
 *      + getCascEntry(hash): CascEntry
 *          + getHash(): long
 *          + getPath(): String
 *          + getFileReader(): Supplier(DataReader)  <- might be composite for multiple content checksums (also for extracted files without name)
 *          + getFileSize(): long
 *      + getContentChecksums(hash): ContentChecksum[]
 *      + getFileKey(ContentChecksum): FileKey
 *      * local (local wow installation, CASC structure)
 *          - root (BLTE)
 *          - encoding (BLTE)
 *          - local indices (.idx)
 *      * cdn (online, CASC structure)
 *          - root (BLTE)
 *          - encoding (BLTE)
 *          - local indices (.index)
 *      * extracted (local files, ./filepath or ./unknown/filekey)
 *          - fallback casccontext   <- cdn | local, for extracting files that haven't been extracted yet
 *          - root (extracted)
 *          - encoding (extracted)
 *
 *
 *
 *
 *
 * @author Barre Dijkstra
 */
public class Casc {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Casc.class);
    /** The name of the build info file. */
    private static final String BUILD_INFO_FILE = ".build.info";
    /** Lock for parsing. */
    private final Lock parseLock;
    /** The configuration. */
    private final ClientDataConfiguration clientDataConfiguration;
    /** The CASC configuration. */
    private CascConfig config;
    /** The parsed CASC index file. */
    private Index index;
    /** The parsed CASC encoding file. */
    private EncodingFile encodingFile;
    /** The parsed CASC root file. */
    private Root root;
    /** The resolved data files. */
    private Map<Integer, DataFile> dataFiles;

    /**
     * Create a new CASC instance.
     *
     * @param configuration The configuration.
     */
    public Casc(ClientDataConfiguration configuration) {
        this.clientDataConfiguration = configuration;
        this.parseLock = new ReentrantLock();
        this.dataFiles = new HashMap<>();
        LOGGER.debug("Creating new CASC container (region: {}, branch: {}, locale: {}, localDirectory: {})",
                configuration.getRegion(), configuration.getBranch(), configuration.getLocale(), configuration.getWowInstallationDirectory());
    }

    /**
     * Force intialisation of the lazy-loaded context.
     */
    public void initialise() {
        LOGGER.trace("Initialising CASC indexes.");
        getConfig();
        LOGGER.trace("Initialised CASC config ({})", getConfig());
        getIndex();
        LOGGER.trace("Initialised indices ({})", getIndex());
        getEncodingFile();
        LOGGER.trace("Initialised encodings ({})", getEncodingFile());
        getRoot();
        LOGGER.trace("Initialised roots ({})", getRoot());
        LOGGER.trace("Finished initialising all CASC indexes");
    }

    /**
     * Get the path to the World of Warcraft installation directory.
     *
     * @return The installation path.
     */
    public Path getInstallationDirectory() {
        return clientDataConfiguration.getWowInstallationDirectory();
    }

    /**
     * Get the installation region for which the CASC is initialised.
     *
     * @return The installation region.
     */
    public Region getRegion() {
        return clientDataConfiguration.getRegion();
    }

    public String getCdnUrl() {
        return getConfig().getCdnUrl();
    }

    /**
     * Get the actual (hash-based) file path for the given file path.
     *
     * @param path The path to get the actual file path for.
     *
     * @return The actual file path.
     */
    protected <T> Optional<T> readFile(String path, Function<BlteFile, T> parser) {
        return Optional.ofNullable(getIndexEntry(path).get(0))
                .map(e -> parser.apply(getDataFile(e).getEntry(e)));
    }

    public List<ContentChecksum> getContentChecksumForFilenameHash(long filenameHash) {
        return getRoot().getContentChecksums(filenameHash);
    }

    public Optional<FileKey> getFileKeyForContentChecksum(ContentChecksum contentChecksum) {
        return getEncodingFile().getFileKey(contentChecksum);
    }

    protected Optional<IndexEntry> getIndexEntryForFileKey(FileKey fileKey) {
        return getIndex().getEntry(fileKey);
    }

    protected List<IndexEntry> getIndexEntry(String path) {
        return Optional.ofNullable(path)
                .map(Casc::hashFilename)
                .map(this::getContentChecksumForFilenameHash)
                .map(c -> c.stream().map(this::getFileKeyForContentChecksum).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .map(c -> c.stream().map(this::getIndexEntryForFileKey).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    protected DataFile getDataFile(IndexEntry entry) {
        if (!dataFiles.containsKey(entry.getFileNumber())) {
            parseLock.lock();
            try {
                Path path = getDataFilePath(entry);
                DataFile dataFile = new DataFile(entry.getFileNumber(), path);
                dataFiles.put(entry.getFileNumber(), dataFile);
            } finally {
                parseLock.unlock();
            }
        }
        return dataFiles.get(entry.getFileNumber());
    }

    protected CascConfig getConfig() {
        if (config == null) {
            parseLock.lock();
            try {
                if (clientDataConfiguration.isOnline()) {
                    LOGGER.debug("Parsing remote CDN config with branch {}, region {} and locale {}",
                            clientDataConfiguration.getBranch(), clientDataConfiguration.getRegion(), clientDataConfiguration.getLocale());
                    config = new CdnCascConfig(clientDataConfiguration, new CdnDataReaderProvider());
                } else {
                    LOGGER.debug("Parsing local config from {} with branch {}, region {} and locale {}",
                            clientDataConfiguration.getWowInstallationDirectory(), clientDataConfiguration.getBranch(), clientDataConfiguration.getRegion(), clientDataConfiguration.getLocale());
                    config = new LocalCascConfig(clientDataConfiguration, new FileDataReaderProvider());
                }
                LOGGER.debug("Successfully parsed config file with CDN url {}, encoding file {} and root {}",
                        config.getCdnUrl(),
                        config.getStorageEncodingFileChecksum(),
                        config.getRootContentChecksum());
            } finally {
                parseLock.unlock();
            }
        }
        return config;
    }

    /**
     * Get the CASC {@link EncodingFile}.
     *
     * @return The encoding file.
     */
    protected EncodingFile getEncodingFile() {
        if (encodingFile == null) {
            IndexEntry indexEntry = Optional.ofNullable(getConfig().getStorageEncodingFileChecksum())
                    .flatMap(this::getIndexEntryForFileKey)
                    .orElseThrow(() -> new CascParsingException(format("No entry found for encoding file entry %s", getConfig().getStorageEncodingFileChecksum())));
            LOGGER.debug("Parsing encoding file from {} bytes of data in data.{} at offset {}",
                    indexEntry.getFileSize(),
                    format("%03d", indexEntry.getFileNumber()),
                    indexEntry.getDataFileOffset()
            );
            Supplier<RandomAccessDataReader> readerSupplier = getBlteDataReader(indexEntry);
            parseLock.lock();
            try (RandomAccessDataReader reader = readerSupplier.get()) {
                encodingFile = reader.readNext(new EncodingFileParser(indexEntry.getFileSize()));
            } catch (IOException e) {
                throw new CascParsingException(format("Error parsing encoding file %s", getConfig().getStorageEncodingFileChecksum()), e);
            } finally {
                parseLock.unlock();
            }
            LOGGER.debug("Successfully parsed encoding file with {} entries", encodingFile.getEntryCount());
        }
        return encodingFile;
    }

    public long getEncodingEntryCount() {
        return getEncodingFile().getEntryCount();
    }

    /**
     * Get the CASC {@link Root}.
     *
     * @return The root file.
     */
    protected Root getRoot() {
        if (root == null) {
            ContentChecksum contentChecksum = Optional.of(config.getRootContentChecksum())
                    .orElseThrow(() -> new CascParsingException(format("No checksum found for root file")));
            FileKey fileKey = getFileKeyForContentChecksum(contentChecksum)
                    .orElseThrow(() -> new CascParsingException(format("No file key found for root file entry %s", contentChecksum)));
            IndexEntry indexEntry = getIndexEntryForFileKey(fileKey)
                    .orElseThrow(() -> new CascParsingException(format("No index entry found for root file entry %s with key %s", contentChecksum, fileKey)));
            LOGGER.debug("Initialising root file from {} bytes of data in data.{} at offset {}",
                    indexEntry.getFileSize(),
                    format("%03d", indexEntry.getFileNumber()),
                    indexEntry.getDataFileOffset());
            BlteFile blteFile = getBlteFile(indexEntry).get();
            Supplier<RandomAccessDataReader> readerSupplier = blteFile.getDataReader();
            parseLock.lock();
            try (DataReader reader = readerSupplier.get()) {
                root = reader.readNext(new RootFileParser());
            } catch (IOException e) {
                throw new CascParsingException(format("Error parsing root file from data file %d at offset %d", indexEntry.getFileNumber(), indexEntry.getDataFileOffset()), e);
            } finally {
                parseLock.unlock();
            }
            LOGGER.debug("Successfully parsed root file with {} entries", root.getHashCount());
        }
        return root;
    }

    public Set<Long> getRegisteredHashes() {
        return getRoot().getHashes();
    }

    public long getRegisteredHashCount() {
        return getRoot().getHashCount();
    }

    public boolean isHashRegistered(long hash) {
        return getRoot().isEntryAvailable(hash);
    }

    public boolean isHashForValidDataBlock(long hash) {
        return getRoot().getContentChecksums(hash).stream()
                .allMatch(c -> Optional.ofNullable(c)
                                .flatMap(this::getFileKeyForContentChecksum)
                                .flatMap(this::getIndexEntryForFileKey)
                                .isPresent()
                );
    }

    public Supplier<RandomAccessDataReader> getFileReader(String filename) {
        return getFileReader(hashFilename(filename));
    }

    public Supplier<RandomAccessDataReader> getFileReader(long hash) {
        return getFileReader(hash, 0, 0);
    }

    public Supplier<RandomAccessDataReader> getFileReader(long hash, long offset, long length) {
        List<IndexEntry> indexEntries = getRoot().getContentChecksums(hash).stream()
                .map(h -> {
                    Optional<FileKey> c = getFileKeyForContentChecksum(h);
                    if (!c.isPresent()) {
                        throw new CascParsingException(format("No file key found for file with hash %s", hash));
                    }
                    return c.get();
                })
                .map(c -> {
                    Optional<IndexEntry> i = this.getIndexEntryForFileKey(c);
                    if (!i.isPresent()) {
                        throw new CascParsingException(format("No index entry found for file hash %s", hash));
                    }
                    return i.get();
                })
                .collect(Collectors.toList());
        return getBlteDataReader(indexEntries, offset, length);
    }

    /**
     * Get loaded the CASC {@link LocalIndexFile} instances.
     *
     * @return The index files.
     */
    protected Index getIndex() {
        if (index == null) {
            LOGGER.debug("Initialising index files");
            parseLock.lock();
            try {
                index = new LocalIndexParser(clientDataConfiguration.getWowInstallationDirectory()).parse();
            } finally {
                parseLock.unlock();
            }
            LOGGER.debug("Successfully initialised index files resulting in {} entries", index.getEntryCount());
        }
        return index;
    }

    public long getIndexEntryCount() {
        return getIndex().getEntryCount();
    }

    protected Path getDataFilePath(IndexEntry entry) {
        String filename = format("data.%03d", entry.getFileNumber());
        return clientDataConfiguration.getWowInstallationDirectory().resolve(Paths.get("Data", "data", filename));
    }

    protected Supplier<RandomAccessDataReader> getBlteDataReader(IndexEntry... entries) {
        return getBlteDataReader(Arrays.asList(entries));
    }

    protected Supplier<RandomAccessDataReader> getBlteDataReader(List<IndexEntry> entries, long offset, long length) {
        Supplier<RandomAccessDataReader> blteDataReader;
        if (entries.size() == 1) {
            IndexEntry entry = entries.iterator().next();
            DataFile dataFile = getDataFile(entry);
            LOGGER.trace("Creating BLTE reader supplier from data file {} ({}) at offset {} for file with key {} ", dataFile.getFileNumber(), dataFile.getPath(), entry
                    .getDataFileOffset(), entry.getFileKey());
            blteDataReader = dataFile.getEntryReader(entry, offset, length);
        } else {
            blteDataReader = () -> {
                try {
                    return new CompositeDataReader(entries.stream()
                            .map(entry -> getDataFile(entry).getEntryReader(entry))
                            .collect(Collectors.toList()));
                } catch (IOException e) {
                    throw new CascParsingException(format("Error creating single BLTE reader from %d entries", entries.size()), e);
                }
            };
            LOGGER.trace("Creating single BLTE reader supplier from {} entries.", entries.size());
        }
        return blteDataReader;
    }

    protected Supplier<RandomAccessDataReader> getBlteDataReader(List<IndexEntry> entries) {
        Supplier<RandomAccessDataReader> blteDataReader;
        if (entries.size() == 1) {
            IndexEntry entry = entries.iterator().next();
            DataFile dataFile = getDataFile(entry);
            LOGGER.debug("Creating BLTE reader supplier from data file {} ({}) at offset {} for file with key {} ", dataFile.getFileNumber(), dataFile.getPath(), entry
                    .getDataFileOffset(), entry.getFileKey());
            blteDataReader = dataFile.getEntryReader(entry);
        } else {
            blteDataReader = () -> {
                try {
                    return new CompositeDataReader(entries.stream()
                            .map(entry -> getDataFile(entry).getEntryReader(entry))
                            .collect(Collectors.toList()));
                } catch (IOException e) {
                    throw new CascParsingException(format("Error creating single BLTE reader from %d entries", entries.size()), e);
                }
            };
            LOGGER.debug("Creating single BLTE reader supplier from {} entries.", entries.size());
        }
        return blteDataReader;
    }

    protected long extractBlteFile(IndexEntry entry, Supplier<OutputStream> outputStreamSupplier) {
        long writtenBytes = 0;
        try (RandomAccessDataReader reader = getBlteDataReader(entry).get()) {
            try (OutputStream out = outputStreamSupplier.get()) {
                while (reader.hasRemaining()) {
                    out.write(reader.readNext(DataTypeFactory.getByte()));
                    writtenBytes++;
                }
                out.flush();
            }
        } catch (IOException e) {
            throw new CascParsingException(e);
        }
        return writtenBytes;
    }

    protected Supplier<BlteFile> getBlteFile(IndexEntry entry) {
        DataFile dataFile = getDataFile(entry);
        LOGGER.debug("Creating BLTE file supplier from data file {} ({}) at offset {} for file with key {} ", dataFile.getFileNumber(), dataFile.getPath(), entry
                .getDataFileOffset(), entry.getFileKey());
        return () -> getDataFile(entry).getEntry(entry);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static long hashFilename(String filename) {
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
