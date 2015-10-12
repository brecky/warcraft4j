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
package nl.salp.warcraft4j.neo4j.file;

import nl.salp.warcraft4j.neo4j.Neo4jConfig;
import nl.salp.warcraft4j.neo4j.Neo4jException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DirectoryNeo4JFileProvider implements Neo4jFileProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryNeo4JFileProvider.class);
    private final Path storageDirectory;

    public DirectoryNeo4JFileProvider(Neo4jConfig config) throws IllegalArgumentException {
        this(parseStoragePath(config));
    }

    public DirectoryNeo4JFileProvider(Path storageDirectory) throws IllegalArgumentException {
        if (storageDirectory == null) {
            throw new IllegalArgumentException("Unable to create a Neo4J file provider for a null file storage directory.");
        } else if (Files.notExists(storageDirectory)) {
            try {
                LOGGER.debug("Creating non-existing Neo4J file storage directory {}.", storageDirectory);
                Files.createDirectories(storageDirectory);
            } catch (IOException e) {
                throw new IllegalArgumentException(format("Unable to create Neo4J file storage directory %s", storageDirectory), e);
            }
        } else if (Files.isRegularFile(storageDirectory) || !Files.isReadable(storageDirectory) || !Files.isWritable(storageDirectory)) {
            throw new IllegalArgumentException(format("Neo4J file storage directory %s either is not a directory or inaccessible.", storageDirectory));
        }
        this.storageDirectory = storageDirectory.normalize().toAbsolutePath();
    }

    @Override
    public void persist(Neo4jFile file, byte[] data) throws Neo4jException, IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException(format("Unable to null data for file %s", file.getPath()));
        }
        int fileSize = file.getFileSize().orElse(-1);
        if (fileSize > -1 && data.length != fileSize) {
            throw new IllegalArgumentException(format("Tried to write %d bytes to file %s with a size of %d.", data.length, file.getPath().orElse(null), fileSize));
        }
        try (FileChannel channel = getWriteChannel(file)) {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int written = 0;
            while (written < data.length) {
                written += channel.write(buffer, written);
            }
            if (!file.getFileSize().isPresent()) {
                file.setFileSize(written);
            }
        } catch (IOException e) {
            throw new Neo4jException(format("Error writing %d bytes of data to file %s", data.length, file.getPath().orElse(null)), e);
        }
    }

    @Override
    public byte[] load(Neo4jFile file) throws Neo4jException, IllegalArgumentException {
        byte[] data;
        int fileSize = 0;
        try (FileChannel channel = getReadChannel(file)) {
            fileSize = (int) channel.size();
            data = new byte[fileSize];
            ByteBuffer dataBuffer = ByteBuffer.wrap(data);
            long read = 0;
            int segmentRead = 0;
            while (segmentRead != -1 && read < fileSize) {
                segmentRead = channel.read(dataBuffer);
                if (segmentRead != -1) {
                    read += segmentRead;
                }
            }
            if (read != fileSize) {
                throw new Neo4jException(format("Error reading data from file %s, read %d bytes till file end with %d bytes expected.",
                        file.getPath().orElse(null), read, fileSize));
            }
        } catch (IOException e) {
            throw new Neo4jException(format("Error reading %d bytes of data from file %s", fileSize, file.getPath().orElse(null)), e);
        }
        return data;
    }

    @Override
    public boolean isNotExisting(Neo4jFile file) throws Neo4jException, IllegalArgumentException {
        return !isExisting(file);
    }

    @Override
    public boolean isExisting(Neo4jFile file) throws Neo4jException, IllegalArgumentException {
        return Optional.ofNullable(file)
                .flatMap(this::getPath)
                .filter(Files::exists)
                .filter(Files::isRegularFile)
                .map(Files::isReadable)
                .orElse(false);
    }

    private FileChannel getReadChannel(Neo4jFile file) throws Neo4jException {
        Path path = Optional.ofNullable(file)
                .filter(this::isExisting)
                .flatMap(this::getPath)
                .orElseThrow(() -> new IllegalArgumentException(format("Unable resolve file from storage with path %s", file.getPath().orElse(null))));
        try {
            long size = Files.size(path);
            if (size > Integer.MAX_VALUE) {
                throw new Neo4jException(format("Unable to read %d byte file %d, maximum size is %d bytes.", size, path, Integer.MAX_VALUE));
            }
            int expectedSize = file.getFileSize().orElse(-1);
            if (expectedSize > -1 && expectedSize != (int) size) {
                throw new Neo4jException(format("File %s is %d bytes on the file system while %d bytes were expected.", path, size, expectedSize));
            }
            return FileChannel.open(path, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new Neo4jException(format("Unable to open storage file %s for reading.", path), e);
        }
    }

    private FileChannel getWriteChannel(Neo4jFile file) throws Neo4jException {
        Path path = getPath(file)
                .orElseThrow(() -> new IllegalArgumentException(format("Unable resolve file from storage with path %s", file.getPath().orElse(null))));
        if (isNotExisting(file)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new Neo4jException(format("Unable to create storage directory for file %s", file.getPath().orElse(null)), e);
            }
        }
        try {
            return FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new Neo4jException(format("Unable to open storage file %s for writing.", path), e);
        }
    }

    private Optional<Path> getPath(Neo4jFile file) {
        return file.getPath()
                .map(Paths::get)
                .map(storageDirectory::resolve)
                .map(Path::normalize);
    }

    private Optional<String> getFile(Path filePath) {
        Optional<String> file;
        if (filePath == null) {
            file = Optional.empty();
        } else if (filePath.startsWith(storageDirectory)) {
            file = Optional.of(filePath)
                    .map(Path::normalize)
                    .map(storageDirectory::relativize)
                    .map(String::valueOf);
        } else {
            file = Optional.of(filePath)
                    .map(Path::normalize)
                    .map(String::valueOf);
        }
        return file;
    }

    /**
     * Validate the configuration and get the database path from it.
     *
     * @param config The configuration.
     *
     * @return The database path.
     *
     * @throws IllegalArgumentException When the configuration is {@code null} or not configured for embedded Neo4J usage.
     */
    private static Path parseStoragePath(Neo4jConfig config) throws IllegalArgumentException {
        if (config == null || config.getNeo4jFileStoragePath() == null) {
            throw new IllegalArgumentException("Unable to create a Neo4J file provider for a configuration with no file storage settings.");
        }
        return config.getNeo4jFileStoragePath();
    }
}
