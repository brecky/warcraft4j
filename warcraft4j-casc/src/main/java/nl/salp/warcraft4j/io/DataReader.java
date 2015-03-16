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

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * Reader for reading data files.
 * <p>
 * This reader should either be closed manually via {@link FileDataReader#close()} or by utilizing the {@link java.io.Closeable} implementation.
 * </p>
 *
 * @author Barre Dijkstra
 */
public abstract class DataReader implements Closeable {
    /** The default ByteOrder for data files. */
    public static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    /**
     * Get the current position of the underlying data.
     *
     * @return The current position.
     */
    public abstract int position();

    /**
     * Check if there is still data available to be read.
     *
     * @return {@code true} if there is still data available to be read.
     *
     * @throws java.io.IOException When there was a problem reading remaining data information from the underlying data implementation.
     */
    public abstract boolean hasRemaining() throws IOException;

    /**
     * Read the next value for the given data type from the underlying data using the default byte order of the data type.
     *
     * @param dataType The data type.
     * @param <T>      The type of value to read.
     *
     * @return The value.
     *
     * @throws java.io.IOException When reading failed.
     */
    public abstract <T> T readNext(DataType<T> dataType) throws IOException;

    /**
     * Read the next value for the given data type from the underlying data using the provided byte order.
     *
     * @param dataType  The data type.
     * @param byteOrder The byte order to read the data in.
     * @param <T>       The type of value to read.
     *
     * @return The value.
     *
     * @throws java.io.IOException When reading failed.
     */
    public abstract <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException;

    /**
     * Read the next complex value from the underlying data type.
     *
     * @param parser The parser to use to parse the complex value.
     * @param <T>    The type of value to read.
     *
     * @return The value.
     *
     * @throws IOException When reading failed.
     */
    public final <T> T readNext(DataParser<T> parser) throws IOException {
        return parser.next(this);
    }

    /**
     * Read the next terminated string from the underlying data.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getTerminatedString()
     */
    public final String readNextTerminatedString() throws IOException {
        return readNext(DataType.getTerminatedString());
    }

    /**
     * Read the next fixed length string from the underlying data.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getFixedLengthString(int)
     */
    public final String readNextFixedLengthString(int length) throws IOException {
        return readNext(DataType.getFixedLengthString(length));
    }

    /**
     * Read the next signed 32-bit integer from the underlying data.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getInteger()
     */
    public final int readNextInt32() throws IOException {
        return readNext(DataType.getInteger());
    }

    /**
     * Read the next signed 32-bit integer array from the underlying data.
     *
     * @param entries The number of array entries.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getInteger()
     */
    public final int[] readNextInt32Array(int entries) throws IOException {
        return toPrimitive(readNext(DataType.getInteger().asArrayType(entries)));
    }

    /**
     * Read the next signed 16-bit short from the underlying data.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getShort()
     */
    public final short readNextShort() throws IOException {
        return readNext(DataType.getShort());
    }

    /**
     * Read the next signed 16-bit short array from the underlying data.
     *
     * @param entries The number of array entries.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getShort()
     */
    public final short[] readNextShortArray(int entries) throws IOException {
        return toPrimitive(readNext(DataType.getShort().asArrayType(entries)));
    }

    /**
     * Read the next 32-bit float from the underlying data.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getFloat()
     */
    public final float readNextFloat() throws IOException {
        return readNext(DataType.getFloat());
    }

    /**
     * Read the next boolean from the underlying data.
     *
     * @return The next value.
     *
     * @throws java.io.IOException When reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getBoolean()
     */
    public final boolean readNextBoolean() throws IOException {
        return readNext(DataType.getBoolean());
    }

    /**
     * Read the next specified number of bytes as a byte[] from the underlying data.
     *
     * @param length The number of bytes to read.
     *
     * @return The next value.
     *
     * @throws java.io.IOException when reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getByteArray(int)
     */
    public final byte[] readNextBytes(int length) throws IOException {
        return readNext(DataType.getByteArray(length));
    }

    /**
     * Read the next byte from the underlying data.
     *
     * @return The next value.
     *
     * @throws java.io.IOException when reading failed.
     * @see DataReader#readNext(DataType)
     * @see DataType#getByte()
     */
    public final byte readNextByte() throws IOException {
        return readNext(DataType.getByte());
    }

    @Override
    public void close() throws IOException {
        // No-op.
    }
}
