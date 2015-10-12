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
package nl.salp.warcraft4j.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.READ;

/**
 * {@link DataReader} implementation for reading random access files, allowing for repositioning of the read cursor.
 *
 * @author Barre Dijkstra
 */
public class FileDataReader extends BaseDataReader {
    private static final long DEFAULT_MAX_LENGTH = Long.MAX_VALUE;
    /** The file channel for the file. */
    private FileChannel channel;
    /** The maximum amount of bytes to have available for the stream. */
    private long maxLength;
    /** The offset. */
    private final long offset;

    /**
     * Create a new FileDataReader instance for the given file.
     *
     * @param file The file.
     *
     * @throws IllegalArgumentException When the file could not be read.
     */
    public FileDataReader(Path file) throws IllegalArgumentException {
        this(file, 0, DEFAULT_MAX_LENGTH);
    }

    /**
     * Create a new FileDataReader instance for the given file, starting at the given offset.
     *
     * @param file   The file.
     * @param offset The file offset to start at.
     *
     * @throws IllegalArgumentException When the file could not be read.
     */
    public FileDataReader(Path file, long offset) throws IllegalArgumentException {
        this(file, offset, DEFAULT_MAX_LENGTH);
    }

    /**
     * Create a new FileDataReader instance for the given file, starting at the given offset.
     *
     * @param file      The file.
     * @param offset    The file offset to start at.
     * @param maxLength The maximum number of bytes to be available.
     *
     * @throws DataReadingException     When the file could not be opened for reading.
     * @throws IllegalArgumentException When invalid data was provided.
     */
    public FileDataReader(Path file, long offset, long maxLength) throws DataReadingException, IllegalArgumentException {
        if (file == null) {
            throw new IllegalArgumentException("Can't create a random access file data reader for a null file.");
        }
        if (Files.notExists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException(format("Can't create a random access file data reader for non existing file %s", file));
        }
        if (!Files.isReadable(file)) {
            throw new IllegalArgumentException(format("Can't create a random access file data reader for non readable file %s", file));
        }
        try {
            channel = FileChannel.open(file, READ);
        } catch (IOException e) {
            throw new DataReadingException(format("Error creating FileDataReader instance for file %s", file), e);
        }
        if (offset > 0) {
            this.offset = offset;
            position(offset);
        } else {
            this.offset = offset;
        }
        this.maxLength = maxLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long position() throws DataReadingException {
        try {
            return channel.position();
        } catch (IOException e) {
            throw new DataReadingException("Unable to get the random access file position", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() throws DataReadingException {
        try {
            return Math.min(channel.size(), (offset + maxLength));
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setPosition(long position) throws DataReadingException {
        try {
            channel.position(position);
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int readData(ByteBuffer buffer) throws DataReadingException {
        try {
            return channel.read(buffer);
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRandomAccessSupported() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void close() throws IOException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }
}