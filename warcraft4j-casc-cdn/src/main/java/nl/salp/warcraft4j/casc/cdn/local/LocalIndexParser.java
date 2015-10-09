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
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class LocalIndexParser {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalIndexParser.class);

    private final Path installationDirectory;

    public LocalIndexParser(Path installationDirectory) {
        this.installationDirectory = installationDirectory;
    }

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
     * Get loaded the CASC {@link LocalIndexFile} instances.
     *
     * @return The index files.
     */
    private static LocalIndexFile parse(Path path) {
        try (DataReader reader = new FileDataReader(path)) {
            LOGGER.debug("Parsing index file {}", path);
            return new LocalIndexFileParser(path, LocalIndexParser::parseFileNumber, LocalIndexParser::parseFileVersion).parse(reader);
        } catch (IOException e) {
            throw new CascParsingException(format("Error parsing index file %s", path), e);
        }
    }


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

    private static int parseFileNumber(Path file) throws CascParsingException {
        try {
            String filename = String.valueOf(file.getFileName());
            byte[] data = Hex.decodeHex(filename.substring(0, filename.indexOf('.')).toCharArray());
            return data[0];
        } catch (DecoderException e) {
            throw new CascParsingException("Unable to parse the file number.", e);
        }
    }

    private static int parseFileVersion(Path file) throws CascParsingException {
        try {
            String filename = String.valueOf(file.getFileName());
            byte[] data = Hex.decodeHex(filename.substring(0, filename.indexOf('.')).toCharArray());
            return ByteBuffer.wrap(data, 1, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        } catch (DecoderException e) {
            throw new CascParsingException("Unable to parse the file version.", e);
        }
    }


    private static class IndexFileScanner implements FileVisitor<Path> {
        private final List<Path> indexFiles;

        public IndexFileScanner() {
            this.indexFiles = new ArrayList<>();
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (isIndexFile(file)) {
                indexFiles.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        public List<Path> getIndexFiles() {
            return indexFiles;
        }

        private static boolean isIndexFile(Path file) {
            return file != null && Files.exists(file) && Files.isRegularFile(file) && Files.isReadable(file) && String.valueOf(file.getFileName()).endsWith(".idx");
        }
    }
}
