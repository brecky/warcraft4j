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
package nl.salp.warcraft4j.util.unsafe;

import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.parser.DataParsingException;
import nl.salp.warcraft4j.io.reader.DataReader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

import static java.lang.String.format;

/**
 * {@link DataReader} for reading from a {@link NativeMemorySegment}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.io.reader.DataReader
 * @see nl.salp.warcraft4j.util.unsafe.NativeMemorySegment
 */
public class NativeMemorySegmentDataReader extends DataReader {
    /** The {@link NativeMemorySegment} to read from. */
    private final NativeMemorySegment segment;
    /** The current offset from the start of the segment. */
    private long offset;

    /**
     * Create a new data reader for a segment.
     *
     * @param segment The {@link NativeMemorySegment}.
     *
     * @throws IllegalArgumentException When the segment is null or closed.
     */
    public NativeMemorySegmentDataReader(NativeMemorySegment segment) throws IllegalArgumentException {
        this.segment = Optional.ofNullable(segment)
                .filter(s -> !s.isClosed())
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a reader for a null or closed native memory segment."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long position() {
        return segment.isClosed() ? -1 : offset;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException When the memory segment is closed.
     */
    @Override
    public boolean hasRemaining() throws IOException {
        return remaining() > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException When the memory segment is closed.
     */
    @Override
    public long remaining() throws IOException {
        if (segment.isClosed()) {
            throw new IOException("Can't read the remaining bytes on a closed memory segment.");
        }
        return segment.getSize() - offset;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException When the memory segment is closed.
     */
    @Override
    public long size() throws IOException {
        if (segment.isClosed()) {
            throw new IOException("Can't get the size of a closed memory segment.");
        }
        return segment.getSize();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException When the memory segment is closed.
     */
    @Override
    public void skip(long bytes) throws IOException {
        if (segment.isClosed()) {
            throw new IOException("Can't skip bytes on a closed memory segment.");
        }
        if (offset + bytes > segment.getSize()) {
            offset = segment.getSize() - 1;
        } else {
            offset += bytes;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException When reading failed or the memory segment is closed.
     */
    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException {
        if (dataType == null) {
            throw new DataParsingException("Can't read a null data type from a memory segment.");
        }
        if (segment.isClosed()) {
            throw new DataParsingException("Can't read from a closed memory segment.");
        }
        try {
            ByteBuffer buffer = ByteBuffer.wrap(segment.get(offset, dataType.getLength()));
            return dataType.readNext(buffer, byteOrder);
        } catch (IllegalArgumentException e) {
            throw new DataParsingException(format("Unable to read %d bytes at offset %d from a %d byte memory segment.", dataType.getLength(), offset, segment.getSize()), e);
        } catch (IllegalStateException e) {
            throw new IOException("Unable to read from a closed memory segment.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {

    }
}
