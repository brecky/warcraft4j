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

import nl.salp.warcraft4j.clientdata.casc.blte.BlteFile;
import nl.salp.warcraft4j.clientdata.casc.local.LocalCascConfig;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.RandomAccessDataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.clientdata.util.hash.JenkinsHash;
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

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Casc {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Casc.class);
    /** The name of the build info file. */
    private static final String BUILD_INFO_FILE = ".build.info";
    /** The path to the World of Warcraft installation directory.. */
    private final Path installationDirectory;
    /** The installation branch to use. */
    private final Branch branch;
    /** Lock for parsing. */
    private final Lock parseLock;
    /** The CASC configuration. */
    private CascConfig config;
    /** The parsed CASC index file. */
    private Index index;
    /** The parsed CASC encoding file. */
    private EncodingFile encodingFile;
    /** The parsed CASC root file. */
    private Root root;
    private Map<Integer, DataFile> dataFiles;

    /**
     * Create a new CASC instance.
     *
     * @param installationDirectory The path to the World of Warcraft installation directory.
     * @param branch                The installation branch for which the CASC is initialised.
     */
    public Casc(Path installationDirectory, Branch branch) {
        this.installationDirectory = installationDirectory;
        this.branch = branch;
        this.parseLock = new ReentrantLock();
        this.dataFiles = new HashMap<>();
        LOGGER.debug("Creating new CASC container (branch: {}, localDirectory: {})", branch, installationDirectory);
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
        return installationDirectory;
    }

    /**
     * Get the installation branch for which the CASC is initialised.
     *
     * @return The installation branch.
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Get the actual (hash-based) file path for the given file path.
     *
     * @param path The path to get the actual file path for.
     *
     * @return The actual file path.
     */
    public <T> Optional<T> readFile(String path, Function<BlteFile, T> parser) {
        return Optional.ofNullable(getIndexEntry(path).get(0))
                .map(e -> parser.apply(getDataFile(e).getEntry(e)));
    }

    public List<Checksum> getContentChecksumForFilenameHash(long filenameHash) {
        return getRoot().getContentChecksums(filenameHash);
    }

    public Optional<Checksum> getFileKeyForContentChecksum(Checksum contentChecksum) {
        return getEncodingFile().getFileKey(contentChecksum);
    }

    public Optional<IndexEntry> getIndexEntryForFileKey(Checksum fileKey) {
        return getIndex().getEntry(fileKey);
    }

    public Optional<IndexEntry> getIndexEntryForFileChecksum(Checksum fileChecksum) {
        Checksum cs = fileChecksum;
        if (fileChecksum.length() > 9) {
            cs = fileChecksum.trim(9);
        }
        return getIndexEntryForFileKey(cs);
    }

    public List<IndexEntry> getIndexEntry(String path) {
        return Optional.ofNullable(path)
                .map(s -> s.replace('/', '\\').toUpperCase().getBytes(StandardCharsets.US_ASCII))
                .map(d -> JenkinsHash.hashLittle2(d, d.length))
                .map(this::getContentChecksumForFilenameHash)
                .map(c -> c.stream().map(this::getFileKeyForContentChecksum).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .map(c -> c.stream().map(this::getIndexEntryForFileKey).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public DataFile getDataFile(IndexEntry entry) {
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

    public CascConfig getConfig() {
        if (config == null) {
            parseLock.lock();
            try {
                LOGGER.debug("Parsing config file from {}", installationDirectory);
                config = new LocalCascConfig(installationDirectory);
                LOGGER.debug("Successfully parsed config file with CDN url {}, encoding file {} and root {}",
                        config.getCdnUrl(),
                        config.getEncodingFileChecksum(),
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
    public EncodingFile getEncodingFile() {
        if (encodingFile == null) {
            IndexEntry indexEntry = Optional.ofNullable(getConfig().getEncodingFileChecksum())
                    .flatMap(this::getIndexEntryForFileChecksum)
                    .orElseThrow(() -> new CascParsingException(format("No entry found for encoding file entry %s", getConfig().getEncodingFileChecksum())));
            LOGGER.debug("Parsing encoding file from {} bytes of data in data.{} at offset {}",
                    indexEntry.getFileSize(),
                    format("%03d", indexEntry.getFileNumber()),
                    indexEntry.getDataFileOffset()
            );
            Supplier<RandomAccessDataReader> readerSupplier = getBlteDataReader(indexEntry);
            parseLock.lock();
            try (RandomAccessDataReader reader = readerSupplier.get()) {
                encodingFile = reader.readNext(new EncodingFileParser());
            } catch (IOException e) {
                throw new CascParsingException(format("Error parsing encoding file %s", getConfig().getEncodingFileChecksum()), e);
            } finally {
                parseLock.unlock();
            }
            LOGGER.debug("Successfully parsed encoding file with {} entries", getEncodingFile().getEntries().size());
        }
        return encodingFile;
    }

    /**
     * Get the CASC {@link Root}.
     *
     * @return The root file.
     */
    public Root getRoot() {
        if (root == null) {
            Checksum contentChecksum = Optional.of(config.getRootContentChecksum())
                    .orElseThrow(() -> new CascParsingException(format("No checksum found for root file")));
            Checksum fileKey = getFileKeyForContentChecksum(contentChecksum)
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
                root = reader.readNext(new RootFileParser(blteFile.getDecompressedSize()));
            } catch (IOException e) {
                throw new CascParsingException(format("Error parsing root file from data file %d at offset %d", indexEntry.getFileNumber(), indexEntry.getDataFileOffset()), e);
            } finally {
                parseLock.unlock();
            }
            LOGGER.debug("Successfully parsed root file with {} entries", root.getEntries().size());
        }
        return root;
    }

    /**
     * Get loaded the CASC {@link IndexFile} instances.
     *
     * @return The index files.
     */
    public Index getIndex() {
        if (index == null) {
            LOGGER.debug("Initialising index files");
            parseLock.lock();
            try {
                index = new IndexParser(installationDirectory).parse();
            } finally {
                parseLock.unlock();
            }
            LOGGER.debug("Successfully initialised index files resulting in {} entries", index.getEntryCount());
        }
        return index;
    }

    public Collection<IndexEntry> getIndexEntries() {
        return getIndex().getEntries();
    }

    public Path getDataFilePath(IndexEntry entry) {
        String filename = format("data.%03d", entry.getFileNumber());
        return installationDirectory.resolve(Paths.get("Data", "data", filename));
    }

    public Supplier<RandomAccessDataReader> getBlteDataReader(IndexEntry entry) {
        DataFile dataFile = getDataFile(entry);
        LOGGER.debug("Creating BLTE reader supplier from data file {} ({}) at offset {} for file with key {} ", dataFile.getFileNumber(), dataFile.getPath(), entry.getDataFileOffset(), entry.getFileKey());
        return dataFile.getEntryReader(entry);
    }


    public long extractBlteFile(IndexEntry entry, Supplier<OutputStream> outputStreamSupplier) {
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

    private Supplier<BlteFile> getBlteFile(IndexEntry entry) {
        DataFile dataFile = getDataFile(entry);
        LOGGER.debug("Creating BLTE file supplier from data file {} ({}) at offset {} for file with key {} ", dataFile.getFileNumber(), dataFile.getPath(), entry.getDataFileOffset(), entry.getFileKey());
        return () -> getDataFile(entry).getEntry(entry);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
