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
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteOrder;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * Reader for reading data files.
 * <p/>
 * This reader should either be closed manually via {@link DataReader#close()} or by utilizing the {@link java.io.Closeable} implementation.
 *
 * @author Barre Dijkstra
 */
public abstract class DataReader implements Closeable, AutoCloseable {
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
    public <T> T readNext(DataType<T> dataType) throws IOException, DataParsingException {
        return readNext(dataType, dataType.getDefaultByteOrder());
    }

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

}
