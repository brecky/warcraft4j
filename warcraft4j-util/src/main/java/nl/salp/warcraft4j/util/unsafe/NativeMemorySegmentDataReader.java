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

import nl.salp.warcraft4j.io.BaseDataReader;
import nl.salp.warcraft4j.io.DataReadingException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * {@link nl.salp.warcraft4j.io.DataReader} for reading from a {@link NativeMemorySegment}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.io.DataReader
 * @see nl.salp.warcraft4j.util.unsafe.NativeMemorySegment
 */
public class NativeMemorySegmentDataReader extends BaseDataReader {
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
        return offset;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void setPosition(long position) throws DataReadingException {
        this.offset = position;
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
     *
     * @throws IOException When the memory segment is closed.
     */
    @Override
    public long size() throws DataReadingException {
        return segment.getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int readData(ByteBuffer buffer) throws DataReadingException {
        int length = buffer.capacity();
        buffer.put(segment.get(offset, buffer.capacity()));
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {

    }
}
