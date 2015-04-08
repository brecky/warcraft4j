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

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * Reader for reading data files.
 * <p/>
 * This reader should either be closed manually via {@link FileDataReader#close()} or by utilizing the {@link java.io.Closeable} implementation.
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
    public abstract long position();

    /**
     * Check if there is still data available to be read.
     *
     * @return {@code true} if there is still data available to be read.
     *
     * @throws java.io.IOException When there was a problem reading remaining data information from the underlying data implementation.
     */
    public abstract boolean hasRemaining() throws IOException;

    /**
     * Get the number of remaining bytes of data available from the current position in the data.
     * <p/>
     * For readers that use buffering the remaining data can be the already read data and not the total available data.
     *
     * @return The number of remaining bytes of data.
     *
     * @throws IOException When determining the remaining bytes failed.
     */
    public abstract long remaining() throws IOException;

    /**
     * Get the size of the underlying data source if available.
     *
     * @return The size or {@code -1} if the size is not available.
     *
     * @throws IOException If the size could not be determined.
     */
    public abstract long size() throws IOException;

    /**
     * Read the next value for the given data type from the underlying data using the default byte order of the data type.
     *
     * @param dataType The data type.
     * @param <T>      The type of value to read.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public abstract <T> T readNext(DataType<T> dataType) throws IOException, DataParsingException;

    /**
     * Read the next value for the given data type from the underlying data using the provided byte order.
     *
     * @param dataType  The data type.
     * @param byteOrder The byte order to read the data in.
     * @param <T>       The type of value to read.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public abstract <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException;

    /**
     * Read the next complex value from the underlying data type.
     *
     * @param parser The parser to use to parse the complex value.
     * @param <T>    The type of value to read.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public <T> T readNext(DataParser<T> parser) throws IOException, DataParsingException {
        return parser.next(this);
    }

    /**
     * Read the next terminated string from the underlying data.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public String readNextTerminatedString() throws IOException, DataParsingException {
        return readNext(DataType.getTerminatedString());
    }

    /**
     * Read the next fixed length string from the underlying data.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public String readNextFixedLengthString(int length) throws IOException, DataParsingException {
        return readNext(DataType.getFixedLengthString(length));
    }

    /**
     * Read the next signed 32-bit integer from the underlying data.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public int readNextInt32() throws IOException, DataParsingException {
        return readNext(DataType.getInteger());
    }

    /**
     * Read the next signed 32-bit integer array from the underlying data.
     *
     * @param entries The number of array entries.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public int[] readNextInt32Array(int entries) throws IOException, DataParsingException {
        return toPrimitive(readNext(DataType.getInteger().asArrayType(entries)));
    }

    /**
     * Read the next signed 16-bit short from the underlying data.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public short readNextShort() throws IOException, DataParsingException {
        return readNext(DataType.getShort());
    }

    /**
     * Read the next signed 16-bit short array from the underlying data.
     *
     * @param entries The number of array entries.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public short[] readNextShortArray(int entries) throws IOException, DataParsingException {
        return toPrimitive(readNext(DataType.getShort().asArrayType(entries)));
    }

    /**
     * Read the next 32-bit float from the underlying data.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public float readNextFloat() throws IOException, DataParsingException {
        return readNext(DataType.getFloat());
    }

    /**
     * Read the next boolean from the underlying data.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public boolean readNextBoolean() throws IOException, DataParsingException {
        return readNext(DataType.getBoolean());
    }

    /**
     * Read the next specified number of bytes as a byte[] from the underlying data.
     *
     * @param length The number of bytes to read.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public byte[] readNextBytes(int length) throws IOException, DataParsingException {
        return readNext(DataType.getByteArray(length));
    }

    /**
     * Read the next byte from the underlying data.
     *
     * @return The next value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public byte readNextByte() throws IOException, DataParsingException {
        return readNext(DataType.getByte());
    }
}
