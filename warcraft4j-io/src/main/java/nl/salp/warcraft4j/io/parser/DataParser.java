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

package nl.salp.warcraft4j.io.parser;

import nl.salp.warcraft4j.io.reader.DataReader;

import java.io.IOException;

/**
 * Data parser that reads and parses instances from a data reader.
 *
 * @author Barre Dijkstra
 */
public interface DataParser<T> {
    /**
     * Read and parse the next instance from the reader.
     *
     * @param reader The reader.
     *
     * @return The parsed instance.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    default T next(DataReader reader) throws IOException, DataParsingException {
        if (reader.remaining() < getInstanceDataSize()) {
            throw new IOException(String.format("Unable to read %d bytes from a data reader with %d remaining.", getInstanceDataSize(), reader.remaining()));
        }
        return parse(reader);
    }

    /**
     * Read and parse the next instance from the reader.
     *
     * @param reader The reader.
     *
     * @return The parsed instance.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    T parse(DataReader reader) throws IOException, DataParsingException;


    /**
     * Get the size of the data of an instance in bytes.
     *
     * @return The size of the instance data or a {@code &lt;= 0} for variable a size.
     */
    default int getInstanceDataSize() {
        return 0;
    }
}
