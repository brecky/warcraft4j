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

package nl.salp.warcraft4j.files.clientdatabase.parser;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Parsed extended DB2 file header.
 *
 * @author Barre Dijkstra
 */
class ClientDatabaseDb2ExtHeader extends ClientDatabaseDb2Header {
    /** The version of the last build before the extended header was introduced. */
    public static final int LAST_BUILD_PRE_EXTENDED_HEADER = 12880;
    /** The base size for the header. */
    private static final int HEADER_SIZE_BASE = 48;
    /** The minimum id. */
    private int minId;
    /** The maximum id. */
    private int maxId;
    /** The locale id. */
    private int locale;
    /** Unknown 4-byte segment. */
    private byte[] unknown;
    /** The row indexes. */
    private int[] rowIndexes;
    /** The row String length in bytes. */
    private short[] rowStringLength;

    public ClientDatabaseDb2ExtHeader(String magicString, int recordCount, int fieldCount, int recordSize, int stringBlockSize, int stringBlockHash, int build, int timestampLastWritten, int minId, int maxId, int locale, byte[] unknown, int[] rowIndexes, short[] rowStringLength) {
        super(magicString, recordCount, fieldCount, recordSize, stringBlockSize, stringBlockHash, build, timestampLastWritten);
        this.minId = minId;
        this.maxId = maxId;
        this.locale = locale;
        this.unknown = unknown;
        this.rowIndexes = rowIndexes;
        this.rowStringLength = rowStringLength;
    }

    @Override
    public int getHeaderSize() {
        return HEADER_SIZE_BASE + (rowIndexes.length * 4) + (rowStringLength.length * 2);
    }

    /**
     * Get the minimum ID.
     *
     * @return The minimum ID.
     */
    public int getMinId() {
        return minId;
    }

    /**
     * Get the maximum ID.
     *
     * @return The maximum ID.
     */
    public int getMaxId() {
        return maxId;
    }

    /**
     * Get the Locale.
     *
     * @return The locale.
     */
    public int getLocale() {
        return locale;
    }

    /**
     * Get the unknown (4-byte) value.
     *
     * @return The unknown value.
     */
    public byte[] getUnknown() {
        return unknown;
    }

    /**
     * Get the row indexes.
     *
     * @return The row indexes.
     */
    public int[] getRowIndexes() {
        return rowIndexes;
    }

    /**
     * Get the length of the row string in bytes.
     *
     * @return The length in bytes.
     */
    public short[] getRowStringLength() {
        return rowStringLength;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
