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
package nl.salp.warcraft4j.fileformat.dbc;

/**
 * Builder for creating a DbcHeader instance.
 *
 * @author Barre Dijkstra
 * @see DbcHeader
 */
public class DbcHeaderBuilder {
    /** The magic String from the header. */
    private String magicString;
    /** The size of the header in bytes. */
    private int headerSize;
    /** The number of entries in the DBC file. */
    private int entryCount;
    /** The number of fields per entry. */
    private int entryFieldCount;
    /** The size of a single entry in bytes. */
    private int entrySize;
    /** The size of the {@link DbcStringTable} block in bytes. */
    private int stringTableBlockSize;
    /** The MD5 hash of the {@link DbcStringTable} block. */
    private int stringTableBlockHash;
    /** The World of Warcraft build number the DBC file was last updated. */
    private int buildNumber;
    /** The timestamp of when then DBC file was last changed. */
    private int timestampLastWritten;
    /** The minimum id of the entries contained. */
    private int minId;
    /** The maximum id of the entries contained. */
    private int maxId;
    /** The locale of the entries. */
    private int locale;
    /** Unknown byte[] data block. */
    private byte[] unknown;
    /** The indexes of the rows. */
    private int[] rowIndexes;
    /** The lengths of the row Strings. */
    private short[] rowStringLength;

    /**
     * Create a new builder instance.
     */
    public DbcHeaderBuilder() {
        stringTableBlockHash = DbcHeader.UNKNOWN_INT;
        buildNumber = DbcHeader.UNKNOWN_INT;
        timestampLastWritten = DbcHeader.UNKNOWN_INT;
        minId = DbcHeader.UNKNOWN_INT;
        maxId = DbcHeader.UNKNOWN_INT;
        locale = DbcHeader.UNKNOWN_INT;
        unknown = DbcHeader.UNKNOWN_BYTE_ARRAY;
        rowIndexes = DbcHeader.UNKNOWN_INT_ARRAY;
        rowStringLength = DbcHeader.UNKNOWN_SHORT_ARRAY;
    }

    /**
     * Set the magic string of the DBC file.
     *
     * @param magicString The magic string.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withMagicString(String magicString) {
        this.magicString = magicString;
        return this;
    }

    /**
     * Set the size of the header in bytes.
     *
     * @param headerSize The header size.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withHeaderSize(int headerSize) {
        this.headerSize = headerSize;
        return this;
    }

    /**
     * Set the number of entries in the DBC file.
     *
     * @param entryCount The number of entries.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withEntryCount(int entryCount) {
        this.entryCount = entryCount;
        return this;
    }

    /**
     * Set the number of fields per entry.
     *
     * @param entryFieldCount The number of fields.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withEntryFieldCount(int entryFieldCount) {
        this.entryFieldCount = entryFieldCount;
        return this;
    }

    /**
     * Set the size of a single entry in bytes.
     *
     * @param recordSize The size of an entry.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withSingleEntrySize(int recordSize) {
        this.entrySize = recordSize;
        return this;
    }

    /**
     * Set the size of the StringTable block in bytes.
     *
     * @param stringTableBlockSize The size of the StringTable block.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withStringTableBlockSize(int stringTableBlockSize) {
        this.stringTableBlockSize = stringTableBlockSize;
        return this;
    }

    /**
     * Set the MD5 hash of the StringTable block.
     *
     * @param stringTableBlockHash The MD5 hash.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withStringTableBlockHash(int stringTableBlockHash) {
        this.stringTableBlockHash = stringTableBlockHash;
        return this;
    }

    /**
     * Set the World of Warcraft build number for the DBC file.
     *
     * @param buildNumber The build number.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
        return this;
    }

    /**
     * Set the timestamp of when the DBC file was last written.
     *
     * @param timestampLastWritten The timestamp.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withTimestampLastWritten(int timestampLastWritten) {
        this.timestampLastWritten = timestampLastWritten;
        return this;
    }

    /**
     * Set the minimum id for the entries.
     *
     * @param minId The minimum id.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withMinEntryId(int minId) {
        this.minId = minId;
        return this;
    }

    /**
     * Set the maximum id for the entries.
     *
     * @param maxId The maximum id.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withMaxEntryId(int maxId) {
        this.maxId = maxId;
        return this;
    }

    /**
     * Set the locale for the entries.
     *
     * @param locale The locale.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withLocaleId(int locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Set the unknown {@code 4-byte} data block.
     *
     * @param unknown The unknown data block.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withUnknownDataBlock(byte[] unknown) {
        this.unknown = unknown;
        return this;
    }

    /**
     * Set the row indices.
     *
     * @param rowIndexes The row indices.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withRowIndexes(int[] rowIndexes) {
        this.rowIndexes = rowIndexes;
        return this;
    }

    /**
     * Set the string length for the rows.
     *
     * @param rowStringLength The string lengths.
     *
     * @return The builder instance.
     */
    public DbcHeaderBuilder withRowStringLength(short[] rowStringLength) {
        this.rowStringLength = rowStringLength;
        return this;
    }

    /**
     * Build the {@link DbcHeader} from the provided data.
     *
     * @return The {@link DbcHeader} instance.
     *
     * @throws DbcParsingException When no valid header could be build with the provided values.
     */
    public DbcHeader build() {
        return new DbcHeader(magicString, headerSize, entryCount, entryFieldCount, entrySize, stringTableBlockSize, stringTableBlockHash, buildNumber, timestampLastWritten,
                minId, maxId, locale, unknown, rowIndexes, rowStringLength);
    }
}
