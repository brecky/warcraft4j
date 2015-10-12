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
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.READ;

/**
 * {@link DataReader} that reads a file as a memory mapped file.
 *
 * @author Barre Dijkstra
 */
public class MemMapFileDataReader extends BaseDataReader {
    /** The default maximum length. */
    private static final long DEFAULT_MAX_LENGTH = -1;
    /** The default offset. */
    private static final long DEFAULT_OFFSET = 0;
    /** The file channel for the file. */
    private final FileChannel channel;
    /** The memory mapped buffer for the file. */
    private final MappedByteBuffer mappedBuffer;
    /** The maximum amount of bytes to have available for the stream. */
    private final long maxLength;
    /** The offset. */
    private final long offset;
    /** The lock for loading the buffer. */
    private final Lock loadLock;

    /**
     * Create a new FileDataReader instance for the given file.
     *
     * @param file The file.
     *
     * @throws IllegalArgumentException When the file could not be read.
     */
    public MemMapFileDataReader(Path file) throws IllegalArgumentException {
        this(file, DEFAULT_OFFSET, DEFAULT_MAX_LENGTH);
    }

    /**
     * Create a new FileDataReader instance for the given file, starting at the given offset.
     *
     * @param file   The file.
     * @param offset The file offset to start at.
     *
     * @throws IllegalArgumentException When the file could not be read.
     */
    public MemMapFileDataReader(Path file, long offset) throws IllegalArgumentException {
        this(file, offset, DEFAULT_MAX_LENGTH);
    }

    /**
     * Create a new FileDataReader instance for the given file, starting at the given offset.
     *
     * @param file      The file.
     * @param offset    The file offset to start at.
     * @param maxLength The maximum number of bytes to be available.
     *
     * @throws DataReadingException     When the file could not be memory mapped.
     * @throws IllegalArgumentException When invalid data was provided.
     */
    public MemMapFileDataReader(Path file, long offset, long maxLength) throws DataReadingException, IllegalArgumentException {
        if (file == null) {
            throw new IllegalArgumentException("Can't create a memory mapped file data reader for a null file.");
        }
        if (Files.notExists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException(format("Can't create a memory mapped file data reader for non existing file %s", file));
        }
        if (!Files.isReadable(file)) {
            throw new IllegalArgumentException(format("Can't create a memory mapped file data reader for non readable file %s", file));
        }
        try {
            this.channel = FileChannel.open(file, READ);
            this.offset = offset > 0 ? offset : 0;
            this.maxLength = maxLength < 1 ? Files.size(file) - this.offset : maxLength;
            this.mappedBuffer = channel.map(FileChannel.MapMode.READ_ONLY, this.offset, this.maxLength);
        } catch (IOException e) {
            throw new DataReadingException(format("Error creating memory mapped FileDataReader instance for file %s", file), e);
        }
        this.loadLock = new ReentrantLock();
    }

    /**
     * Get a memory mapped byte buffer.
     *
     * @return The mapped byte buffer.
     *
     * @throws DataParsingException When the mapped buffer could not be retrieved.
     */
    private MappedByteBuffer getMappedBuffer() throws DataParsingException {
        if (!channel.isOpen()) {
            throw new DataParsingException("Tried to get the memory mapped buffer for a closed file channel.");
        }
        if (!mappedBuffer.isLoaded()) {
            if (loadLock.tryLock()) {
                try {
                    mappedBuffer.load();
                } finally {
                    loadLock.unlock();
                }
            } else {
                while (!mappedBuffer.isLoaded()) {
                    try {
                        Thread.currentThread().wait(1);
                    } catch (InterruptedException e) {
                        // Ignore.
                    }
                }
            }
        }
        return mappedBuffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long position() {
        // FIXME Determine whether MappedByteBuffer#position() counts from the channel start or from offset.
        return getMappedBuffer().position() + offset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() {
        return getMappedBuffer().hasRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long remaining() {
        return getMappedBuffer().remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() {
        return getMappedBuffer().position() + getMappedBuffer().remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int readData(ByteBuffer buffer) throws DataReadingException {
        getMappedBuffer().get(buffer.array());
        return buffer.capacity();

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
