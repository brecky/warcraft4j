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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.lang.String.format;

/**
 * {@link DataReader} implementation that reads from a {@code java.io.InputStream}.
 *
 * @author Barre Dijkstra
 */
public class InputStreamDataReader extends DataReader {
    /** The input stream. */
    private final InputStream stream;
    /** The position in the stream. */
    private long position;

    /**
     * Create a new data reader instance for the given stream.
     *
     * @param stream The stream to wrap.
     */
    public InputStreamDataReader(InputStream stream) {
        this.stream = stream;
        position = 0;
    }

    @Override
    public final long position() {
        return position;
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return stream.available() > 0;
    }

    @Override
    public long remaining() throws IOException {
        return stream.available();
    }

    @Override
    public long size() throws IOException {
        return remaining() + position();
    }

    @Override
    public void skip(long bytes) throws IOException {
        if (bytes < 0) {
            throw new IllegalArgumentException(format("Unable to skip %d bytes.", bytes));
        }
        if (bytes > remaining()) {
            throw new IOException(format("Error skipping %d bytes from position %d with %d bytes available.", bytes, position, remaining()));
        }
        long remaining = bytes;
        while (remaining > 0) {
            remaining -= stream.skip(remaining);
        }
        position += bytes;
    }

    @Override
    public final <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException {
        ByteBuffer buffer;
        if (dataType.isVariableLength()) {
            buffer = getVarLenBuffer(dataType, byteOrder);
        } else {
            byte[] data = new byte[dataType.getLength()];
            stream.read(data);
            buffer = ByteBuffer.wrap(data).order(byteOrder);
        }
        position += buffer.limit();
        buffer.rewind();
        return dataType.readNext(buffer);
    }

    /**
     * Get a buffer for a variable length data type.
     *
     * @param dataType  The data type.
     * @param byteOrder The byte order for the returned buffer.
     * @param <T>       The data type.
     *
     * @return The byte buffer.
     *
     * @throws IOException When creating the buffer failed.
     */
    private <T> ByteBuffer getVarLenBuffer(DataType<T> dataType, ByteOrder byteOrder) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            boolean done = false;
            byte b;
            while (!done) {
                if ((b = (byte) stream.read()) != -1) {
                    byteOut.write(b);
                    done = (b == dataType.getVariableLengthTerminator());
                } else {
                    done = true;
                }
            }
            return ByteBuffer.wrap(byteOut.toByteArray()).order(byteOrder);
        }
    }

    @Override
    public final void close() throws IOException {
        if (stream != null) {
            stream.close();
        }
    }
}
