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

import nl.salp.warcraft4j.io.datatype.DataType;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public final long position() {
        return position;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException When the position is before the current reading position.
     */
    @Override
    public void position(long position) throws DataReadingException, UnsupportedOperationException {
        if (position < this.position) {
            throw new UnsupportedOperationException("Unable to move to a position before the current reading position.");
        }
        if (position > remaining()) {
            throw new DataReadingException(format("Error to position to offset %d, positioning past end of the data.", position));
        }
        skip(position - this.position);
    }

    /**
     * {@inheritDoc}
     *
     * @return Always returns {@code false}.
     */
    @Override
    public boolean isRandomAccessSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() throws DataReadingException {
        try {
            return stream.available() > 0;
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long remaining() throws DataReadingException {
        try {
            return stream.available();
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() throws DataReadingException {
        return remaining() + position();
    }

    @Override
    public void skip(long bytes) throws DataReadingException {
        if (bytes < 0) {
            throw new IllegalArgumentException(format("Unable to skip %d bytes.", bytes));
        }
        if (bytes > remaining()) {
            throw new DataReadingException(format("Error skipping %d bytes from position %d with %d bytes available.", bytes, position, remaining()));
        }
        try {
            long remaining = bytes;
            while (remaining > 0) {
                remaining -= stream.skip(remaining);
            }
            position += bytes;
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException {
        try {
            ByteBuffer buffer;
            if (dataType.isVariableLength()) {
                buffer = getVarLenBuffer(dataType);
            } else {
                byte[] data = new byte[dataType.getLength()];
                stream.read(data);
                buffer = ByteBuffer.wrap(data);
            }
            position += buffer.limit();
            buffer.rewind();
            return dataType.readNext(buffer, byteOrder);
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
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
    private <T> ByteBuffer getVarLenBuffer(DataType<T> dataType) throws DataReadingException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            boolean done = false;
            byte b;
            while (!done) {
                if ((b = (byte) stream.read()) != -1) {
                    byteOut.write(b);
                    done = dataType.isVariableLengthTerminator(b);
                } else {
                    done = true;
                }
            }
            return ByteBuffer.wrap(byteOut.toByteArray());
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void close() throws IOException {
        if (stream != null) {
            stream.close();
        }
    }
}
