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

package nl.salp.warcraft4j.clientdata.io;

import nl.salp.warcraft4j.clientdata.io.datatype.DataType;
import nl.salp.warcraft4j.clientdata.io.parser.DataParsingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * {@link RandomAccessDataReader} implementation that uses a {@code byte[]} as underlying data.
 */
public class ByteArrayDataReader extends RandomAccessDataReader {
    /** The ByteBuffer holding the data. */
    private ByteBuffer buffer;
    /** The size of the data. */
    private final long dataSize;

    /**
     * Create a new ByteArrayDataReader, wrapping the provided byte array.
     *
     * @param data The byte[] to wrap.
     */
    public ByteArrayDataReader(byte[] data) {
        this(data, DEFAULT_BYTE_ORDER);
    }

    /**
     * Create a new ByteArrayDataReader, wrapping the provided byte array.
     *
     * @param data      The byte[] to wrap.
     * @param byteOrder The byte order to wrap the byte array in.
     */
    public ByteArrayDataReader(byte[] data, ByteOrder byteOrder) {
        buffer = ByteBuffer.wrap(data).order(byteOrder);
        this.dataSize = data.length;
    }

    @Override
    public long position() {
        return buffer.position();
    }

    @Override
    public void position(long position) throws IOException, DataParsingException {
        buffer.position((int) position);
    }

    @Override
    public boolean hasRemaining() throws IOException, DataParsingException {
        return buffer.hasRemaining();
    }

    @Override
    public long size() throws IOException {
        return dataSize;
    }

    @Override
    public long remaining() throws IOException {
        return buffer.remaining();
    }

    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException {
        byte[] data;
        if (dataType.isVariableLength()) {
            data = getVarLenData(dataType);
        } else {
            data = new byte[dataType.getLength()];
            this.buffer.get(data);
        }
        return dataType.readNext(ByteBuffer.wrap(data).order(byteOrder), byteOrder);
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
    private <T> byte[] getVarLenData(DataType<T> dataType) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            boolean done = false;
            while (!done) {
                byte value;
                if ((value = buffer.get()) > 0) {
                    byteOut.write(value);
                    done = dataType.isVariableLengthTerminator(value);
                } else {
                    done = true;
                }
            }
            return byteOut.toByteArray();
        }
    }

    @Override
    public void close() throws IOException {
        // no-op
    }
}
