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
package nl.salp.warcraft4j.io.reader.file;

import nl.salp.warcraft4j.Warcraft4jException;
import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.reader.RandomAccessDataReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.READ;

/**
 * {@link DataReader} implementation for reading random access files, allowing for repositioning of the read cursor.
 *
 * @author Barre Dijkstra
 */
public class FileDataReader extends RandomAccessDataReader {
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
     * @throws IllegalArgumentException When the file could not be read.
     */
    public FileDataReader(Path file, long offset, long maxLength) throws IllegalArgumentException {
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
            throw new IllegalArgumentException(format("Error creating FileDataReader instance for file %s", file), e);
        }
        if (offset > 0) {
            try {
                this.offset = offset;
                position(offset);
            } catch (IOException e) {
                throw new IllegalArgumentException(format("Can't open file %s at offset %d", file, offset), e);
            }
        } else {
            this.offset = offset;
        }
        this.maxLength = maxLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long position() {
        try {
            return channel.position();
        } catch (IOException e) {
            throw new Warcraft4jException("Unable to get the random access file position", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() throws IOException {
        return size() > position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long remaining() throws IOException {
        return size() - position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() throws IOException {
        return Math.min(channel.size(), (offset + maxLength));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException {
        ByteBuffer buffer;
        if (dataType.isVariableLength()) {
            buffer = getVarLenBuffer(dataType);
        } else {
            buffer = ByteBuffer.allocate(dataType.getLength());
            channel.read(buffer);
        }
        buffer.rewind();
        return dataType.readNext(buffer, byteOrder);
    }

    /**
     * Get a buffer for a variable length data type.
     *
     * @param dataType The data type.
     * @param <T>      The data type.
     *
     * @return The byte buffer.
     *
     * @throws IOException When creating the buffer failed.
     */
    private <T> ByteBuffer getVarLenBuffer(DataType<T> dataType) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            boolean done = false;
            while (!done) {
                ByteBuffer buffer = ByteBuffer.wrap(new byte[1]);
                if (channel.read(buffer) > 0) {
                    byte value = buffer.get(0);
                    byteOut.write(value);
                    done = dataType.isVariableLengthTerminator(value);
                } else {
                    done = true;
                }
            }
            return ByteBuffer.wrap(byteOut.toByteArray());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void position(long position) throws IOException {
        channel.position(position);
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

    /**
     * Get a supplier for creating a {@link RandomAccessDataReader} instance for a file.
     *
     * @param file The file.
     *
     * @return The supplier.
     */
    public static Supplier<RandomAccessDataReader> supplier(Path file) {
        return () -> new FileDataReader(file);
    }

    /**
     * Get a supplier for creating a {@link RandomAccessDataReader} instance for a file.
     *
     * @param directory The directory the file is in.
     * @param filename  The name of the file.
     *
     * @return The supplier.
     */
    public static Supplier<RandomAccessDataReader> supplier(String directory, String filename) {
        return () -> new FileDataReader(Paths.get(directory, filename));
    }
}
