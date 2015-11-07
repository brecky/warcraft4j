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
package nl.salp.warcraft4j.casc.cdn.util;

import nl.salp.warcraft4j.casc.CascEntryNotFoundException;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static nl.salp.warcraft4j.util.DataTypeUtil.byteArrayToHexString;
import static nl.salp.warcraft4j.util.DataTypeUtil.toByteArray;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CascFileExtractor {
    /** The chunk size to read/write data in. */
    private static final int CHUNK_SIZE = 4096;
    /** The context to extract the CASC files from. */
    private final CdnCascContext context;
    /** The directory to extract the files to. */
    private final Path extractDir;

    /**
     * Create a new instance.
     *
     * @param extractionDirectory The directory to extract the files to.
     * @param context             The context to extract the CASC files from.
     */
    public CascFileExtractor(Path extractionDirectory, CdnCascContext context) {
        try {
            this.extractDir = Files.createDirectories(extractionDirectory);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to create the extraction directory tree", e);
        }
        this.context = context;
    }

    public Set<Path> extractAllFiles() throws CascExtractionException, DataReadingException, DataParsingException {
        Map<Long, Path> files = new HashMap<>();
        files.putAll(context.getResolvedFilenames().stream()
                .filter(context::isRegisteredData)
                .filter(fn -> context.getHash(fn).isPresent())
                .collect(Collectors.toMap(fn -> context.getHash(fn).get(), this::getExtractionFilename)));
        files.putAll(context.getHashes().stream()
                .filter(context::isRegisteredData)
                .filter(h -> !files.containsKey(h))
                .collect(Collectors.toMap(Function.identity(), this::getExtractionFilename)));

        return files.keySet().stream()
                .map(h -> extractFile(files.get(h), h))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public Optional<Path> extractFile(String filename) throws CascExtractionException, DataReadingException, DataParsingException {
        return Optional.ofNullable(filename)
                .filter(StringUtils::isNotEmpty)
                .filter(context::isRegisteredData)
                .filter(fn -> context.getHash(fn).isPresent())
                .flatMap(fn -> extractFile(getExtractionFilename(fn), context.getHash(fn).get()));
    }

    public Optional<Path> extractFile(long filenameHash) throws CascExtractionException, DataReadingException, DataParsingException {
        return Optional.of(filenameHash)
                .filter(context::isRegisteredData)
                .flatMap(h -> extractFile(getExtractionFilename(h), h));
    }

    private Optional<Path> extractFile(Path destination, long filenameHash) throws CascExtractionException, DataReadingException, DataParsingException {
        Optional<Path> file;
        if (isWritableFile(destination)) {
            try {
                Files.createDirectories(destination.getParent());
                try (DataReader in = context.getFileDataReader(filenameHash);
                     OutputStream out = Files.newOutputStream(destination, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                    while (in.hasRemaining()) {
                        int chunkSize = (int) Math.min(CHUNK_SIZE, in.remaining());
                        byte[] chunk = in.readNext(DataTypeFactory.getByteArray(chunkSize));
                        out.write(chunk);
                    }
                    out.flush();
                    file = Optional.of(destination);
                } catch (CascEntryNotFoundException e) {
                    file = Optional.empty();
                }
            } catch (IOException e) {
                throw new CascExtractionException(format("Error while extraction CASC file to %s: %s", destination, e.getMessage()), e);
            }
        } else {
            throw new CascExtractionException(format("Unable to extract a file to %s, the path already exists and is not a file or not writable.", destination));
        }
        return file;
    }

    private boolean isWritableFile(Path file) {
        return Files.notExists(file) || (Files.isRegularFile(file) && Files.isWritable(file));
    }

    private Path getExtractionFilename(long filenameHash) throws CascExtractionException {
        return extractDir.resolve("unknown").resolve(byteArrayToHexString(toByteArray(filenameHash)));
    }

    private Path getExtractionFilename(String filename) throws CascExtractionException {
        return extractDir.resolve(filename);
    }
}
