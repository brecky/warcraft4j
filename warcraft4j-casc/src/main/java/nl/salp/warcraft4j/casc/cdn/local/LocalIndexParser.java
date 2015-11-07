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
package nl.salp.warcraft4j.casc.cdn.local;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.IndexEntry;
import nl.salp.warcraft4j.casc.cdn.Index;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.FileDataReader;
import nl.salp.warcraft4j.util.DataTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Parser for parsing all {@link LocalIndexFile} files in a World of Warcraft installation directory to a {@link Index}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.cdn.Index
 */
public class LocalIndexParser {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalIndexParser.class);
    /** The World of Warcraft installation directory. */
    private final Path installationDirectory;

    /**
     * Create a new instance.
     *
     * @param installationDirectory The World of Warcraft installation directory.
     */
    public LocalIndexParser(Path installationDirectory) {
        this.installationDirectory = installationDirectory;
    }

    /**
     * Read and parse all index files into a {@link Index}.
     *
     * @return The index.
     */
    public Index parse() {
        LOGGER.trace("Parsing local index entries from {}.", installationDirectory);
        Collection<Path> latestIndexFilePaths = getLatestIndexFilePaths();
        List<LocalIndexFile> latestIndexFiles = latestIndexFilePaths.stream()
                .map(LocalIndexParser::parse)
                .collect(Collectors.toList());
        List<IndexEntry> entries = latestIndexFiles.stream()
                .map(LocalIndexFile::getEntries)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        LOGGER.trace("Parsed {} index entries from {} index files.", entries.size(), latestIndexFiles.size());
        return new Index(entries);
    }

    /**
     * Parse a local index file to a {@link LocalIndexFile}.
     *
     * @return The parsed index file.
     */
    private static LocalIndexFile parse(Path path) {
        try (DataReader reader = new FileDataReader(path)) {
            LOGGER.debug("Parsing index file {}", path);
            return new LocalIndexFileParser(path, LocalIndexParser::parseFileNumber, LocalIndexParser::parseFileVersion).parse(reader);
        } catch (IOException e) {
            throw new CascParsingException(format("Error parsing index file %s", path), e);
        }
    }

    /**
     * Get the paths of the latest version of each index file.
     *
     * @return The paths of the latest index file version.
     */
    private Collection<Path> getLatestIndexFilePaths() {
        try {
            IndexFileScanner scanner = new IndexFileScanner();
            Path path = installationDirectory.resolve(Paths.get("Data", "data"));
            Files.walkFileTree(path, scanner);
            Map<Integer, Path> latestFiles = new HashMap<>();
            for (Path p : scanner.getIndexFiles()) {
                int fileNum = parseFileNumber(p);
                if (!latestFiles.containsKey(fileNum) || parseFileVersion(p) > parseFileNumber(latestFiles.get(fileNum))) {
                    LOGGER.trace("Using index file {} version {} instead of version {}", fileNum, parseFileVersion(p),
                            latestFiles.containsKey(fileNum) ? parseFileVersion(latestFiles.get(fileNum)) : 0);
                    latestFiles.put(fileNum, p);
                } else {
                    LOGGER.trace("Skipping index file {} version {} in favor of version {}", fileNum, parseFileVersion(p), parseFileVersion(latestFiles.get(fileNum)));
                }
            }
            LOGGER.trace("Found {} up-to-date index files in {}", latestFiles.size(), path);
            return latestFiles.values();
        } catch (IOException e) {
            throw new CascParsingException("Error getting the latest index files", e);
        }
    }

    /**
     * Parse the file number from the path of an index file.
     *
     * @param file The file path.
     *
     * @return The file number.
     *
     * @throws CascParsingException When the file number couldn't be read.
     */
    private static int parseFileNumber(Path file) throws CascParsingException {
        String filename = String.valueOf(file.getFileName());
        byte[] data = DataTypeUtil.hexStringToByteArray(filename.substring(0, filename.indexOf('.')));
        return data[0];
    }

    /**
     * Parse the file version from the path of an index file.
     *
     * @param file The file path.
     *
     * @return The file version.
     *
     * @throws CascParsingException When the file version couldn't be read.
     */
    private static int parseFileVersion(Path file) throws CascParsingException {
        String filename = String.valueOf(file.getFileName());
        byte[] data = DataTypeUtil.hexStringToByteArray(filename.substring(0, filename.indexOf('.')));
        return ByteBuffer.wrap(data, 1, 4).order(ByteOrder.BIG_ENDIAN).getInt();
    }


    /**
     * {@code FileVisitor} for finding index files.
     */
    private static class IndexFileScanner implements FileVisitor<Path> {
        /** The discovered index files. */
        private final List<Path> indexFiles;

        /**
         * Create a new instance.
         */
        public IndexFileScanner() {
            this.indexFiles = new ArrayList<>();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (isIndexFile(file)) {
                indexFiles.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        /**
         * Get the discovered index files.
         *
         * @return The index files.
         */
        public List<Path> getIndexFiles() {
            return indexFiles;
        }

        /**
         * Check if a path refers to a readable index file.
         *
         * @param file The path to a file.
         *
         * @return {@code true} if the path refers to an index file.
         */
        private static boolean isIndexFile(Path file) {
            return file != null && Files.exists(file) && Files.isRegularFile(file) && Files.isReadable(file) && String.valueOf(file.getFileName()).endsWith(".idx");
        }
    }
}
