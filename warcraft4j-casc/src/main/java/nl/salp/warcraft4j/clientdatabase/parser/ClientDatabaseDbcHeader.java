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
 * DBC file header.
 *
 * @author Barre Dijkstra
 */
class ClientDatabaseDbcHeader implements ClientDatabaseHeader {
    /** The magic String for a DBC file. */
    public static final String MAGICSTRING = "WDBC";
    /** The header size of a DBC file. */
    private static final int HEADER_SIZE = 20;

    /** The header. */
    private String magicString;
    /** The number of records in the DBC file. */
    private int recordCount;
    /** The number of fields in a record. */
    private int fieldCount;
    /** The size of a record in bytes. */
    private int recordSize;
    /** The size of the StringBlock in bytes. */
    private int stringBlockSize;

    /**
     * Create a new DbcHeader.
     *
     * @param magicString     The magic string.
     * @param recordCount     The number of records in the file.
     * @param fieldCount      The number of fields per record.
     * @param recordSize      The size of a single record in bytes.
     * @param stringBlockSize The size of the StringBlock in bytes.
     */
    public ClientDatabaseDbcHeader(String magicString, int recordCount, int fieldCount, int recordSize, int stringBlockSize) {
        this.magicString = magicString;
        this.recordCount = recordCount;
        this.fieldCount = fieldCount;
        this.recordSize = recordSize;
        this.stringBlockSize = stringBlockSize;
    }

    @Override
    public String getMagicString() {
        return magicString;
    }

    @Override
    public int getRecordCount() {
        return recordCount;
    }

    @Override
    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    @Override
    public int getFieldCount() {
        return fieldCount;
    }

    @Override
    public int getRecordSize() {
        return recordSize;
    }

    @Override
    public int getRecordBlockOffset() {
        return getHeaderSize();
    }

    @Override
    public int getRecordBlockSize() {
        return getRecordCount() * getRecordSize();
    }

    @Override
    public int getStringBlockOffset() {
        return getHeaderSize() + getRecordBlockSize();
    }

    @Override
    public int getStringBlockSize() {
        return stringBlockSize;
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
