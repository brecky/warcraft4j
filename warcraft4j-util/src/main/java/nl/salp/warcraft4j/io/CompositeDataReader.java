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
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.function.Supplier;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CompositeDataReader extends BaseDataReader {
    private static final int CHUNK_SIZE = 1024;
    private final ByteArrayDataReader reader;

    public CompositeDataReader(List<Supplier<? extends DataReader>> readers) throws DataReadingException {
        this(readers, 0, 0);
    }

    public CompositeDataReader(List<Supplier<? extends DataReader>> readers, long offset, long length) throws DataReadingException {
        this.reader = new ByteArrayDataReader(readAllData(readers, offset, length));
    }

    private static byte[] readAllData(List<Supplier<? extends DataReader>> readers, long offset, long length) throws DataReadingException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        long position = 0;
        for (Supplier<? extends DataReader> readerSupplier : readers) {
            try (DataReader reader = readerSupplier.get()) {
                if (offset > 0 && reader.hasRemaining() && position < offset) {
                    long skipped = Math.min(reader.remaining(), offset - position);
                    reader.skip(skipped);
                    position += skipped;
                } else {
                    while (reader.hasRemaining() && (length < 1 || position < offset + length)) {
                        int chunkSize = (int) Math.min(CHUNK_SIZE, reader.remaining());
                        data.write(reader.readNext(DataTypeFactory.getByteArray(chunkSize)));
                        position += chunkSize;
                    }
                }
            } catch (IOException e) {
                throw new DataReadingException(e);
            }
        }
        return data.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long position() {
        return reader.position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() {
        return reader.hasRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long remaining() {
        return reader.remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() {
        return reader.size();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void setPosition(long position) throws DataReadingException {
        reader.setPosition(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRandomAccessSupported() {
        return reader.isRandomAccessSupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skip(long bytes) {
        reader.skip(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T peek(DataType<T> dataType) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return reader.peek(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T peek(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return reader.peek(dataType, byteOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T readNext(DataType<T> dataType) throws DataReadingException, DataParsingException {
        return reader.readNext(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException {
        return reader.readNext(dataType, byteOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T read(DataType<T> dataType, long position) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return reader.read(dataType, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T read(DataType<T> dataType, long position, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return reader.read(dataType, position, byteOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int readData(ByteBuffer buffer) throws DataReadingException {
        return reader.readData(buffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
