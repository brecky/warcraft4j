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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.lang.String.format;

/**
 * Base {@link DataReader} implementation for reading raw (byte) data.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.io.DataReader
 */
public abstract class BaseDataReader implements DataReader {
    /**
     * {@inheritDoc}
     */
    @Override
    public void position(long position) throws DataReadingException, UnsupportedOperationException {
        if (!isRandomAccessSupported() && position < position()) {
            throw new UnsupportedOperationException(
                    format("Unable to position the reader to %d, position is before current reading position and random access is not supported.", position));
        }
        if (position > size()) {
            throw new DataReadingException(format("Error setting the reader position to %d, position is after the end of the available data.", position));
        } else if (position < 0) {
            throw new DataReadingException(format("Error setting the reader position to %d, position is before the start of the data.", position));
        }
        setPosition(position);
    }

    /**
     * Set the position of the underlying data provider to the given position.
     * <p>
     * The position will never be negative, beyond the available data or before the current position in case of a non random access reader.
     *
     * @param position The position to set the data provider to.
     *
     * @throws DataReadingException When setting the position failed.
     */
    protected abstract void setPosition(long position) throws DataReadingException;

    /**
     * {@inheritDoc}
     */
    @Override
    public long remaining() throws DataReadingException {
        return size() - position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() throws DataReadingException {
        return remaining() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skip(long bytes) throws DataReadingException, UnsupportedOperationException {
        position(position() + bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T peek(DataType<T> dataType) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return peek(dataType, dataType.getDefaultByteOrder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T peek(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        if (!isRandomAccessSupported()) {
            throw new UnsupportedOperationException("Reader does not support random access positioning, can't peek a value.");
        }
        long marker = position();
        try {
            return readNext(dataType, byteOrder);
        } finally {
            position(marker);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T readNext(DataType<T> dataType) throws DataReadingException, DataParsingException {
        return readNext(dataType, dataType.getDefaultByteOrder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException {
        ByteBuffer buffer;
        if (dataType.isVariableLength()) {
            buffer = getVarLenBuffer(dataType);
        } else {
            buffer = ByteBuffer.allocate(dataType.getLength());
            readData(buffer);
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
     * @throws DataReadingException When reading the variable sized data failed.
     */
    private <T> ByteBuffer getVarLenBuffer(DataType<T> dataType) throws DataReadingException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            boolean done = false;
            while (!done) {
                ByteBuffer buffer = ByteBuffer.wrap(new byte[1]);
                if (readData(buffer) > 0) {
                    byte value = buffer.get(0);
                    byteOut.write(value);
                    done = dataType.isVariableLengthTerminator(value);
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
     * Read the data from the underlying data into a byte buffer.
     *
     * @param buffer The buffer to read the data into.
     *
     * @return The number of bytes read.
     *
     * @throws DataReadingException When reading the data failed.
     */
    protected abstract int readData(ByteBuffer buffer) throws DataReadingException;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T read(DataType<T> dataType, long position) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        position(position);
        return readNext(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T read(DataType<T> dataType, long position, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        position(position);
        return readNext(dataType, byteOrder);
    }
}
