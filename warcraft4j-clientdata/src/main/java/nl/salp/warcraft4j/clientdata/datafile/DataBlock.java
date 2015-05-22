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

import static java.lang.String.format;

/**
 * Data block in a {@link DataFile}.
 *
 * @author Barre Dijkstra
 */
public class DataBlock {
    /** The data block header. */
    private final DataHeader header;
    /** The data block data. */
    private final byte[] data;

    /**
     * Create a new DataBlock instance.
     *
     * @param header The data block header.
     * @param data   The data.
     */
    public DataBlock(DataHeader header, byte[] data) {
        verify(header, data);
        this.header = header;
        this.data = data;
    }

    /**
     * Verify the header and data integrity.
     *
     * @param header The data block header.
     * @param data   The data.
     *
     * @throws IllegalArgumentException When either the header or data are not valid.
     */
    private void verify(DataHeader header, byte[] data) throws IllegalArgumentException {
        if (header == null) {
            throw new IllegalArgumentException("Unable to create a DataBlock with a null header.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Unable to create a DataBlock with null data.");
        }
        if (data.length != header.getDataSize()) {
            throw new IllegalArgumentException(format("Unable to create a DataBlock with non-matching data size."));
        }
    }

    /**
     * Get the data.
     *
     * @return The data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Get the size of the data segment.
     *
     * @return The size of the data segment in bytes.
     */
    public int getDataSize() {
        return header.getDataSize();
    }

    /**
     * Get the size of the header segment.
     *
     * @return The size of the header segment in bytes.
     */
    public int getHeaderSize() {
        return header.getHeaderSize();
    }

    /**
     * Get the size of the whole data block.
     *
     * @return The size of the whole data block in bytes.
     */
    public int getBlockSize() {
        return header.getBlockSize();
    }
}
