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
import java.io.InputStream;
import java.nio.ByteBuffer;

import static java.lang.String.format;

/**
 * {@link DataReader} implementation that reads from a {@code java.io.InputStream}.
 *
 * @author Barre Dijkstra
 */
public class InputStreamDataReader extends BaseDataReader {
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

    @Override
    protected void setPosition(long position) throws DataReadingException {
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
        return position() + remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skip(long bytes) throws DataReadingException {
        if (bytes < 0) {
            throw new UnsupportedOperationException(format("Unable to skip %d bytes.", bytes));
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
    protected int readData(ByteBuffer buffer) throws DataReadingException {
        try {
            int read = 0;
            byte[] dataArray;
            if (buffer.hasArray()) {
                read = stream.read(buffer.array());
            } else {
                byte[] data = new byte[buffer.capacity()];
                read = stream.read(data);
                buffer.put(data);
            }
            return read;
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
