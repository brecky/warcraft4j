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

package nl.salp.warcraft4j.datafile;

import static java.lang.String.format;

/**
 * {@link DataBlock} header containing data block information.
 *
 * @author Barre Dijkstra
 */
class DataHeader {
    /** The size of the header in bytes. */
    private static final int HEADER_SIZE = 30;
    /** The MD5 sum for the data block (Storage: 16B, ? Endian). */
    private final byte[] md5; //
    /** The size of the full data block (including header) (Storage: 4B, Little-Endian). */
    private final int blockSize;
    /** Unknown 10B data segment. (Storage: 10B, ? Endian). */
    private final byte[] unknown;

    /**
     * Create a new DataHeader instance.
     *
     * @param md5       The md5 as listed in the header.
     * @param blockSize The size of the block in bytes (including the header).
     * @param unknown   Unknown, 10B, data segment.
     */
    public DataHeader(byte[] md5, int blockSize, byte[] unknown) {
        verify(md5, blockSize, unknown);
        this.md5 = md5;
        this.blockSize = blockSize;
        this.unknown = unknown;
    }

    /**
     * Verify the header data.
     *
     * @param md5       The MD5.
     * @param blockSize The block size.
     * @param unknown   The unknown 10B data segment.
     */
    private void verify(byte[] md5, int blockSize, byte[] unknown) {
        if (md5 == null) {
            throw new IllegalArgumentException("Unable to create a DataHeader with an empty md5.");
        }
        if (md5.length != 16) {
            throw new IllegalArgumentException(format("Unable to create a DataHeader with an md5 of %d bytes (16 expected)", md5.length));
        }
        if (blockSize <= getHeaderSize()) {
            throw new IllegalArgumentException(format("Unable to create a DataHeader with a block size of %d (including the %dB header).", blockSize, getHeaderSize()));
        }
        if (unknown == null || unknown.length != 10) {
            throw new IllegalArgumentException("Unable to create a DataHeader without the 10B data segment.");
        }
    }

    /**
     * Get the MD5 hash from the header.
     *
     * @return The MD5 hash.
     */
    public byte[] getMd5() {
        return md5;
    }

    /**
     * Get the full block size (including header).
     *
     * @return The block size in bytes.
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * Get the data segment size.
     *
     * @return The data size in bytes.
     */
    public int getDataSize() {
        return blockSize - getHeaderSize();
    }

    /**
     * Get the header size.
     *
     * @return The header size in bytes.
     */
    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    /**
     * Get the data from the unknown segment.
     *
     * @return The data.
     */
    public byte[] getUnknownSegment() {
        return unknown;
    }
}
