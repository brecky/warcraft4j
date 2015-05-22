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

package nl.salp.warcraft4j.clientdata.datafile;

import nl.salp.warcraft4j.clientdata.io.DataParser;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.DataType;

import java.io.IOException;
import java.nio.ByteOrder;

/**
 * {@link DataParser} implementation for {@link DataHeader}.
 *
 * @author Barre Dijkstra
 */
class DataHeaderParser extends DataParser<DataHeader> {
    /** The size of the MD5 segment in bytes. */
    private static final int MD5S_SIZE = 16;
    /** The size of the unknown segment in bytes. */
    private static final int UNKNOWN_SEGMENT_SIZE = 10;
    /** The size of the full data header. */
    private static final int HEADER_SIZE = 30;

    /**
     * Parse the next header from the reader.
     *
     * @param reader The reader to read and parse the data from.
     *
     * @return The parsed data header.
     *
     * @throws IOException When reading failed.
     */
    protected DataHeader parse(DataReader reader) throws IOException {
        byte[] md5 = reader.readNext(DataType.getByteArray(MD5S_SIZE));
        int blockSize = reader.readNext(DataType.getInteger(), ByteOrder.LITTLE_ENDIAN);
        byte[] unknownSegment = reader.readNext(DataType.getByteArray(UNKNOWN_SEGMENT_SIZE));
        return new DataHeader(md5, blockSize, unknownSegment);
    }

    @Override
    public int getInstanceDataSize() {
        return HEADER_SIZE;
    }
}
