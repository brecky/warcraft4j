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
import java.nio.ByteOrder;
import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class NativeMemorySegmentDataReader extends DataReader {
    /** The {@link NativeMemorySegment} to read from. */
    private final NativeMemorySegment segment;
    /** The current offset from the start of the segment. */
    private long offset;

    public NativeMemorySegmentDataReader(NativeMemorySegment segment) {
        this.segment = Optional.ofNullable(segment)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a reader for a null native memory segment."));
    }

    @Override
    public long position() {
        return offset;
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return offset < segment.getSize() - 1;
    }

    @Override
    public long remaining() throws IOException {
        return segment.getSize() - offset;
    }

    @Override
    public long size() throws IOException {
        return segment.getSize();
    }

    @Override
    public void skip(long bytes) throws IOException {
        if (offset + bytes > segment.getSize()) {
            offset = segment.getSize() - 1;
        } else {
            offset += bytes;
        }
    }

    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
