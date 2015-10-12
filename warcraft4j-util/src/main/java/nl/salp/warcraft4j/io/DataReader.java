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

import java.io.Closeable;
import java.nio.ByteOrder;

/**
 * Reader for reading data files.
 * <p>
 * This reader should either be closed manually via {@link BaseDataReader#close()} or by utilizing the {@link java.io.Closeable} implementation.
 *
 * @author Barre Dijkstra
 */
public interface DataReader extends Closeable, AutoCloseable {
    /**
     * Get the current position of the underlying data.
     *
     * @return The current position.
     *
     * @throws DataReadingException When the position could not be set.
     */
    long position() throws DataReadingException;

    /**
     * Check if full random access (backward and forward repositioning) is supported.
     *
     * @return {@code true} if full repositioning is supported.
     */
    boolean isRandomAccessSupported();

    /**
     * Set the position of the reader cursor.
     *
     * @param position The position.
     *
     * @throws DataReadingException          When the position could not be set.
     * @throws UnsupportedOperationException When the position is before the current reading position and random access is not supported.
     * @see #isRandomAccessSupported()
     */
    void position(long position) throws DataReadingException, UnsupportedOperationException;

    /**
     * Check if there is still data available to be read.
     *
     * @return {@code true} if there is still data available to be read.
     *
     * @throws DataReadingException When there was a problem reading remaining data information from the underlying data implementation.
     */
    boolean hasRemaining() throws DataReadingException;

    /**
     * Get the number of remaining bytes of data available from the current position in the data.
     * <p>
     * For readers that use buffering the remaining data can be the already read data and not the total available data.
     *
     * @return The number of remaining bytes of data.
     *
     * @throws DataReadingException When determining the remaining bytes failed.
     */
    long remaining() throws DataReadingException;

    /**
     * Get the size of the underlying data source if available.
     *
     * @return The size or {@code -1} if the size is not available.
     *
     * @throws DataReadingException When determining the size failed.
     */
    long size() throws DataReadingException;

    /**
     * Skip a number of bytes.
     *
     * @param bytes The number of bytes to skip.
     *
     * @throws DataReadingException          When skipping failed.
     * @throws UnsupportedOperationException When the position is before the current reading position and random access is not supported.
     * @see #isRandomAccessSupported()
     */
    void skip(long bytes) throws DataReadingException, UnsupportedOperationException;

    /**
     * Read the next value for a data type from the underlying data using the default byte order of the data type without repositioning the stream.
     *
     * @param dataType The data type.
     * @param <T>      The type of value to read.
     *
     * @return The value.
     *
     * @throws DataReadingException          When reading the data failed.
     * @throws DataParsingException          When parsing the data failed.
     * @throws UnsupportedOperationException When the reader does not support random access positioning.
     * @see #isRandomAccessSupported()
     * @see #peek(DataType, ByteOrder)
     */
    <T> T peek(DataType<T> dataType) throws DataReadingException, DataParsingException, UnsupportedOperationException;

    /**
     * Read the next value for a data type from the underlying data without repositioning the stream.
     *
     * @param dataType The data type.
     * @param <T>      The type of value to read.
     *
     * @return The value.
     *
     * @throws DataReadingException          When reading the data failed.
     * @throws DataParsingException          When parsing the data failed.
     * @throws UnsupportedOperationException When the reader does not support random access positioning.
     * @see #isRandomAccessSupported()
     */
    <T> T peek(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException;

    /**
     * Read the next value for a data type from the underlying data using the default byte order of the data type.
     *
     * @param dataType The data type.
     * @param <T>      The type of value to read.
     *
     * @return The value.
     *
     * @throws DataReadingException When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    <T> T readNext(DataType<T> dataType) throws DataReadingException, DataParsingException;

    /**
     * Read the next value for a data type from the underlying data using the provided byte order.
     *
     * @param dataType  The data type.
     * @param byteOrder The byte order to read the data in.
     * @param <T>       The type of value to read.
     *
     * @return The value.
     *
     * @throws DataReadingException When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException;

    /**
     * Read the value for a data type in the default byte order from a position (moving the position to the 1st byte after the read data).
     *
     * @param dataType The data type.
     * @param position The position in the file to read the data from.
     * @param <T>      The type of the data to read.
     *
     * @return The read data.
     *
     * @throws DataReadingException          When reading the data failed.
     * @throws DataParsingException          When parsing the data failed.
     * @throws UnsupportedOperationException When the reader does not support random access positioning.
     * @see #read(DataType, long, ByteOrder)
     * @see #isRandomAccessSupported()
     */
    <T> T read(DataType<T> dataType, long position) throws DataReadingException, DataParsingException, UnsupportedOperationException;

    /**
     * Read the value for a data type from (moving the position to the 1st byte after the read data).
     *
     * @param dataType  The data type.
     * @param position  The position in the file to read the data from.
     * @param byteOrder The byte order of the data.
     * @param <T>       The type of the data to read.
     *
     * @return The read data.
     *
     * @throws DataReadingException          When reading the data failed.
     * @throws DataParsingException          When parsing the data failed.
     * @throws UnsupportedOperationException When the reader does not support random access positioning.
     * @see #isRandomAccessSupported()
     */
    <T> T read(DataType<T> dataType, long position, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException;
}