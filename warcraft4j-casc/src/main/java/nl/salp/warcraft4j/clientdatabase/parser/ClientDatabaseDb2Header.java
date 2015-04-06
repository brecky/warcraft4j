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

package nl.salp.warcraft4j.clientdatabase.parser;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Parsed DB2 file header.
 */
class ClientDatabaseDb2Header extends ClientDatabaseDbcHeader {
    /** The magic string for the file. */
    public static final String MAGICSTRING = "WDB2";
    /** The default header size in bytes. */
    private static int HEADER_SIZE = 32;
    /** The hash of the string block. */
    private int stringBlockHash;
    /** The World of Warcraft client build number. */
    private int build;
    /** The timestamp when the DB2 file was last altered/written. */
    private int timestampLastWritten;

    /**
     * Create a new DB2 file header.
     *
     * @param magicString          The magic string.
     * @param recordCount          The number of records in the file.
     * @param fieldCount           The number of fields per record.
     * @param recordSize           The size of a record in bytes.
     * @param stringBlockSize      The size of the StringBlock in bytes.
     * @param stringBlockHash      The hash of the StringBlock.
     * @param build                The World of Warcraft client build number
     * @param timestampLastWritten The timestamp when the DB2 file was last altered/written.
     */
    public ClientDatabaseDb2Header(String magicString, int recordCount, int fieldCount, int recordSize, int stringBlockSize, int stringBlockHash, int build, int timestampLastWritten) {
        super(magicString, recordCount, fieldCount, recordSize, stringBlockSize);
        this.stringBlockHash = stringBlockHash;
        this.build = build;
        this.timestampLastWritten = timestampLastWritten;

    }

    @Override
    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    /**
     * Get the hash for the StringBlock.
     *
     * @return The hash.
     */
    public int getStringBlockHash() {
        return stringBlockHash;
    }

    /**
     * Get the build number.
     *
     * @return The build.
     */
    public int getBuild() {
        return build;
    }

    /**
     * Get the timestamp the DBC was last written/altered.
     *
     * @return The timestamp.
     */
    public int getTimestampLastWritten() {
        return timestampLastWritten;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Check if the header is applicable for the given magic string.
     *
     * @param magicString The magic string.
     *
     * @return {@code true} if the header is applicable for a header with the given magic string.
     */
    public static boolean isHeaderFor(String magicString) {
        return MAGICSTRING.equals(magicString);
    }
}
