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

import java.io.IOException;
import java.nio.ByteOrder;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * {@link DataReader} extension for random access data reading.
 *
 * @author Barre Dijkstra
 */
public abstract class RandomAccessDataReader extends DataReader {

    /**
     * Set the position of the reader cursor.
     *
     * @param position The position.
     *
     * @throws IOException When the position could not be set.
     */
    public abstract void position(long position) throws IOException;

    /**
     * Read the data in the given data type in the default byte order from the provided file position (moving the file pointer to the 1st byte after the read data).
     *
     * @param dataType The data type.
     * @param position The position in the file to read the data from.
     * @param <T>      The type of the data to read.
     *
     * @return The read data.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public <T> T read(DataType<T> dataType, long position) throws IOException, DataParsingException {
        position(position);
        return readNext(dataType);
    }

    /**
     * Read the data in the given data type from the provided file position (moving the file pointer to the 1st byte after the read data).
     *
     * @param dataType  The data type.
     * @param position  The position in the file to read the data from.
     * @param byteOrder The byte order of the data.
     * @param <T>       The type of the data to read.
     *
     * @return The read data.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public <T> T read(DataType<T> dataType, long position, ByteOrder byteOrder) throws IOException, DataParsingException {
        position(position);
        return readNext(dataType, byteOrder);
    }

    /**
     * Read the next complex value from the underlying data type.
     *
     * @param parser   The parser to use to parse the complex value.
     * @param position The position to read the value from.
     * @param <T>      The type of value to read.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public <T> T readNext(DataParser<T> parser, long position) throws IOException, DataParsingException {
        position(position);
        return parser.next(this);
    }
}
