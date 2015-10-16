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

/**
 * {@link DataReader} implementation that uses a {@code byte[]} as underlying data.
 */
public class ByteArrayDataReader extends BaseDataReader {
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
        buffer = ByteBuffer.wrap(data);
        this.dataSize = data.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long position() {
        return buffer.position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setPosition(long position) throws DataReadingException {
        buffer.position((int) position);
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
    public boolean hasRemaining() {
        return buffer.hasRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() {
        return dataSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long remaining() {
        return buffer.remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int readData(ByteBuffer buffer) throws DataReadingException {
        int read = -1;
        if (this.buffer.hasRemaining()) {
            read = Math.min(this.buffer.remaining(), buffer.capacity());
            this.buffer.get(buffer.array(), 0, read);
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        // no-op
    }
}
