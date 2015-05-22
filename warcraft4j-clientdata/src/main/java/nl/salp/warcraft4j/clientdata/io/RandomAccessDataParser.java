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

import static java.lang.String.format;

/**
 * Data parser that parses instances from a data reader with support for indexed reading through a {@link RandomAccessDataReader}.
 *
 * @author Barre Dijkstra
 */
public abstract class RandomAccessDataParser<T> extends DataParser<T> {
    /**
     * Read an parse an instance from the offset on the reader.
     * <p/>
     * <p/>
     * If the offset is reader has a size of the data type and the offset != {@code 0} then the reader is reset to position 0.
     *
     * @param reader The reader to read from.
     * @param offset The offset to start reading from.
     *
     * @return The parsed data.
     *
     * @throws IOException              When reading the data failed.
     * @throws DataParsingException     When parsing the data failed.
     * @throws IllegalArgumentException When an invalid offset has been provided.
     */
    public T read(RandomAccessDataReader reader, long offset) throws IOException, DataParsingException {
        if ((offset + getInstanceDataSize()) > reader.size()) {
            throw new IllegalArgumentException(format("Unable to read %d bytes at position %d from a data reader with a size of %d bytes.", getInstanceDataSize(), offset, reader.size()));
        } else if (offset < 0) {
            throw new IllegalArgumentException("Unable to reader data from an offset < 0");
        } else if (getInstanceDataSize() == reader.size()) {
            reader.position(0);
        } else {
            reader.position(offset);
        }
        return parse(reader);
    }
}
