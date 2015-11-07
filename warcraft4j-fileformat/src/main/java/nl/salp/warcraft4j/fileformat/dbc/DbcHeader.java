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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * File header for {@code DBC}, {@code DB2} and {@code ADB} files.
 *
 * @author Barre Dijkstra
 */
public class DbcHeader {
    /** Value for an unknown integer value. */
    public static final int UNKNOWN_INT = Integer.MIN_VALUE;
    /** Value for an unknown byte[] value. */
    public static final byte[] UNKNOWN_BYTE_ARRAY = new byte[0];
    /** Value for an unknown int[] value. */
    public static final int[] UNKNOWN_INT_ARRAY = new int[0];
    /** Value for an unknown short[] value. */
    public static final short[] UNKNOWN_SHORT_ARRAY = new short[0];
    /** The magic String for a DBC file. */
    public static final String DBC_MAGICSTRING = "WDBC";
    /** The magic string for a DB2 file. */
    public static final String DB2_MAGICSTRING = "WDB2";
    /** The magic string for an ADB file. */
    public static final String ADB_MAGICSTRING = "WCH2";
    /** The version of the last build before the extended DB2 header was introduced. */
    public static final int DB2_LASTBUILD_BEFORE_EXTENSION = 12880;
    /** The length of the magic string. */
    public static final int MAGIC_STRING_LENGTH = 4;
    /** The header size of a DBC file. */
    public static final int DBC_HEADER_SIZE = 20;
    /** The default header size of a DB2. */
    public static final int DB2_HEADER_SIZE = 32;
    /** The base size for an extended DB2 header. */
    public static final int DB2_EXTENDED_HEADER_SIZE_BASE = 48;

    /**
     * The header type.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final String magicString;

    /**
     * The size of the header data block in bytes.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int headerSize;
    /**
     * The number of entries in the DBC file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int entryCount;
    /**
     * The number of fields in an entry.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int entryFieldCount;
    /**
     * The size of a single entry in bytes.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int entrySize;
    /**
     * The size of the StringTable data block.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int stringTableBlockSize;
    /**
     * The hash of the StringTable data block.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int stringTableBlockHash;
    /**
     * The World of Warcraft client build number.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </p>
     * </ul>
     */
    private final int buildNumber;
    /**
     * The timestamp when the DBC file was last altered/written.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int timestampLastWritten;
    /**
     * The minimum id.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int minimumEntryId;
    /**
     * The maximum id.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int maximumEntryId;
    /**
     * The id of the locale the DBC file is in.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int locale;
    /**
     * Unknown 4-byte segment.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final byte[] unknown;
    /**
     * The row indexes.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final int[] rowIndexes;
    /**
     * The row String length in bytes.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     */
    private final short[] rowStringLength;

    /**
     * Create a new DBC header instance.
     *
     * @param magicString          The file magic string.
     * @param headerSize           The size of the header data block in bytes.
     * @param entryCount           The number of entries in the DBC file.
     * @param entryFieldCount      The number of fields in an entry.
     * @param entrySize            The size of a single entry in bytes.
     * @param stringTableBlockSize The size of the StringTable data block.
     */
    public DbcHeader(String magicString, int headerSize, int entryCount, int entryFieldCount, int entrySize, int stringTableBlockSize) {
        this(magicString, headerSize, entryCount, entryFieldCount, entrySize, stringTableBlockSize, UNKNOWN_INT, UNKNOWN_INT, UNKNOWN_INT, UNKNOWN_INT, UNKNOWN_INT, UNKNOWN_INT, UNKNOWN_BYTE_ARRAY, UNKNOWN_INT_ARRAY, UNKNOWN_SHORT_ARRAY);
    }

    /**
     * Create a default DB2 header.
     *
     * @param magicString          The file magic string.
     * @param headerSize           The size of the header data block in bytes.
     * @param entryCount           The number of entries in the DBC file.
     * @param entryFieldCount      The number of fields in an entry.
     * @param entrySize            The size of a single entry in bytes.
     * @param stringTableBlockSize The size of the StringTable data block.
     * @param stringTableBlockHash The hash of the StringTable data block.
     * @param buildNumber          The World of Warcraft client build number.
     * @param timestampLastWritten The timestamp when the DBC file was last altered/written.
     */
    public DbcHeader(String magicString, int headerSize, int entryCount, int entryFieldCount, int entrySize, int stringTableBlockSize, int stringTableBlockHash, int buildNumber,
            int timestampLastWritten) {
        this(magicString, headerSize, entryCount, entryFieldCount, entrySize, stringTableBlockSize, stringTableBlockHash, buildNumber, timestampLastWritten, UNKNOWN_INT, UNKNOWN_INT, UNKNOWN_INT, UNKNOWN_BYTE_ARRAY, UNKNOWN_INT_ARRAY, UNKNOWN_SHORT_ARRAY);
    }

    /**
     * Create an extended DB2 header.
     *
     * @param magicString          The file magic string.
     * @param headerSize           The size of the header data block in bytes.
     * @param entryCount           The number of entries in the DBC file.
     * @param entryFieldCount      The number of fields in an entry.
     * @param entrySize            The size of a single entry in bytes.
     * @param stringTableBlockSize The size of the StringTable data block.
     * @param stringTableBlockHash The hash of the StringTable data block.
     * @param buildNumber          The World of Warcraft client build number.
     * @param timestampLastWritten The timestamp when the DBC file was last altered/written.
     * @param minimumEntryId       The minimum entry id.
     * @param maximumEntryId       The maximum entry id.
     * @param locale               The id of the locale the DBC file is in.
     * @param unknown              Unknown 4-byte segment.
     * @param rowIndexes           The row indexes.
     * @param rowStringLength      The row String length in bytes.
     *
     * @throws DbcParsingException When invalid values were provided.
     */
    public DbcHeader(String magicString, int headerSize, int entryCount, int entryFieldCount, int entrySize, int stringTableBlockSize, int stringTableBlockHash, int buildNumber,
            int timestampLastWritten, int minimumEntryId, int maximumEntryId, int locale, byte[] unknown, int[] rowIndexes, short[] rowStringLength) {
        this.magicString = magicString;
        this.headerSize = headerSize;
        this.entryCount = entryCount;
        this.entryFieldCount = entryFieldCount;
        this.entrySize = entrySize;
        this.stringTableBlockSize = stringTableBlockSize;
        this.stringTableBlockHash = stringTableBlockHash;
        this.buildNumber = buildNumber;
        this.timestampLastWritten = timestampLastWritten;
        this.minimumEntryId = minimumEntryId;
        this.maximumEntryId = maximumEntryId;
        this.locale = locale;
        this.unknown = unknown;
        this.rowIndexes = rowIndexes;
        this.rowStringLength = rowStringLength;
        validateValues();
    }

    /**
     * Validate the header values by checking for invalid values or combination of values.
     *
     * @throws DbcParsingException When the header contains invalid values or combination of values.
     */
    private void validateValues() throws DbcParsingException {
        if (isEmpty(magicString) || !(isDbcVersion1() || isDbcVersion2() || isExtendedDbcVersion2() || ADB_MAGICSTRING.equals(magicString))) {
            throw new DbcParsingException(format("Invalid DBC magic string %s, must be one of %s, %s or %s", magicString, DBC_MAGICSTRING, DB2_MAGICSTRING, ADB_MAGICSTRING));
        }
        if (isDbcVersion1() && headerSize != DBC_HEADER_SIZE) {
            throw new DbcParsingException(format("Invalid header size for DBC version 1 file of %d bytes, expected %d bytes.", headerSize, DBC_HEADER_SIZE));
        }
        if (isDbcVersion1Supported()) {
            if (entrySize < 4) {
                throw new DbcParsingException(format("Invalid entry size of %d bytes, a minimum of 4 bytes is required (for the id).", entrySize));
            }
            if (entryCount < 1) {
                throw new DbcParsingException(format("An entry count of %d is invalid.", entryCount));
            }
            if (entryFieldCount < 1) {
                throw new DbcParsingException(format("An entry field count of %d is invalid.", entryFieldCount));
            }
            if (stringTableBlockSize < 0) {
                throw new DbcParsingException(format("Can't have a negative StringTable block size of %d.", stringTableBlockSize));
            }
            if (entryFieldCount > (entrySize - 3)) {
                throw new DbcParsingException(format("An entry with a size of %d bytes cannot have %d fields, a maximum of %d fields was expected.", entrySize, entryFieldCount,
                        entrySize - 3));
            }
        }
        if (isDbcVersion2() && headerSize != DB2_HEADER_SIZE) {
            throw new DbcParsingException(format("Invalid header size for DBC version 2 file of %d bytes, expected %d bytes.", headerSize, DB2_HEADER_SIZE));
        }
        if (isDbcVersion2Supported()) {
            if (buildNumber < 0 && buildNumber != UNKNOWN_INT) {
                throw new DbcParsingException(format("Can't have negative build number %d.", buildNumber));
            }
            if (timestampLastWritten < 0 && buildNumber != UNKNOWN_INT) {
                throw new DbcParsingException(format("Can't have negative timestamp %d for the last write timestamp of the DBC.", timestampLastWritten));
            }
        }
        if (isExtendedDbcVersion2() && headerSize < DB2_EXTENDED_HEADER_SIZE_BASE) {
            throw new DbcParsingException(format("Invalid header size for DBC version 2 extended file of %d bytes, expected minimum of %d bytes.", headerSize, DB2_EXTENDED_HEADER_SIZE_BASE));
        }
        if (isExtenededDbcVersion2Supported()) {
            if (minimumEntryId < 0) {
                throw new DbcParsingException(format("Can't have negative minimum entry id %d.", minimumEntryId));
            }
            if (maximumEntryId < 0) {
                throw new DbcParsingException(format("Can't have negative maximum entry id %d.", maximumEntryId));
            }
            if (maximumEntryId < minimumEntryId) {
                throw new DbcParsingException(format("Can't have maximum entry id %d which is smaller then the minimum entry id %d.", maximumEntryId, minimumEntryId));
            }
            // TODO Validate locale.
            if (unknown != null && unknown.length > 0 && unknown.length != 4) {
                throw new DbcParsingException(format("Unknown data block with data set must be 4 bytes instead of %d.", unknown.length));
            }
            // TODO Validate row indexes
            // TODO Validate row string lengths.
        }
    }

    /**
     * Check if the header is for a DBC version 1 file.
     *
     * @return {@code true} if the header is a version 1 header.
     */
    public boolean isDbcVersion1() {
        return DBC_MAGICSTRING.equals(magicString);
    }

    /**
     * Check if the header structure supports the structure of a DBC version 1 file.
     *
     * @return {@code true} if the header supports the DBC version 1 structure.
     */
    public boolean isDbcVersion1Supported() {
        return isDbcVersion1() || isDbcVersion2() || isExtendedDbcVersion2();
    }

    /**
     * Check if the header is a version 2 header.
     *
     * @return {@code true} if the header is a version 2 header.
     */
    public boolean isDbcVersion2() {
        return DB2_MAGICSTRING.equals(magicString) && buildNumber <= DB2_LASTBUILD_BEFORE_EXTENSION;
    }


    /**
     * Check if the header structure supports the structure of a DBC version 2 file.
     *
     * @return {@code true} if the header supports the DBC version 2 structure.
     */
    public boolean isDbcVersion2Supported() {
        return isDbcVersion2() || isExtendedDbcVersion2();
    }

    /**
     * Check if the header is an extended version 2 header.
     *
     * @return {@code true} if the header is an extended version 2 header.
     */
    public boolean isExtendedDbcVersion2() {
        return DB2_MAGICSTRING.equals(magicString) && buildNumber > DB2_LASTBUILD_BEFORE_EXTENSION;
    }

    /**
     * Check if the header structure supports the structure of an extended DBC version 2 file.
     *
     * @return {@code true} if the header supports the extended DBC version 2 structure.
     */
    public boolean isExtenededDbcVersion2Supported() {
        return isExtendedDbcVersion2();
    }

    /**
     * Get the file magic string.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The magic string.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public String getMagicString() {
        return magicString;
    }

    /**
     * Get the size of the header block for the header in bytes.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The size.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getHeaderSize() {
        return headerSize;
    }

    /**
     * Get the number of entries in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The number of entries.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getEntryCount() {
        return entryCount;
    }

    /**
     * Get the number of fields in an entry in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The number of fields.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getEntryFieldCount() {
        return entryFieldCount;
    }

    /**
     * Get the size in bytes of a single entry in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The size in bytes.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getEntrySize() {
        return entrySize;
    }

    /**
     * Get the size in bytes of the entry data block in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The size.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getEntryBlockSize() {
        return entryCount * entrySize;
    }

    /**
     * Get the starting offset of the entry data block in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The starting offset.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getEntryBlockStartingOffset() {
        return headerSize;
    }

    /**
     * Get the size of the StringTable data block in bytes.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The size.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getStringTableBlockSize() {
        return stringTableBlockSize;
    }

    /**
     * Get the starting offset of the string table data block in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The starting offset.
     *
     * @see #isDbcVersion1Supported()
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getStringTableStartingOffset() {
        return headerSize + getEntryBlockSize();
    }

    /**
     * Get the hash of the StringTable block.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The hash or {@link #UNKNOWN_INT} if it was not available on the header.
     *
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getStringTableBlockHash() {
        return stringTableBlockHash;
    }

    /**
     * Get the build number of the World of Warcraft client.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The build number or {@link #UNKNOWN_INT} if it was not available on the header.
     *
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getBuildNumber() {
        return buildNumber;
    }

    /**
     * Get the timestamp the file was last altered.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The timestamp or {@link #UNKNOWN_INT} if it was not available on the header.
     *
     * @see #isDbcVersion2Supported()
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getTimestampLastWritten() {
        return timestampLastWritten;
    }

    /**
     * Get the minimum id of the entries in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The minimum id or {@link #UNKNOWN_INT} if it was not available on the header.
     *
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getMinimumEntryId() {
        return minimumEntryId;
    }

    /**
     * Get the maximum id of the entries in the file.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The maximum id or {@link #UNKNOWN_INT} if it was not available on the header.
     *
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getMaximumEntryId() {
        return maximumEntryId;
    }

    /**
     * Get the id of the locale the file is in.
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The locale id or {@link #UNKNOWN_INT} if it was not available on the header.
     *
     * @see #isExtenededDbcVersion2Supported()
     */
    public int getLocale() {
        return locale;
    }

    /**
     * Get values of the unknown 4-byte data segment.
     * <p>
     * TODO This is probably an uint32, figure out where it's used for.
     * </p>
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The data segment or {@link #UNKNOWN_BYTE_ARRAY} if it was not available on the header.
     *
     * @see #isExtenededDbcVersion2Supported()
     */
    public byte[] getUnknown() {
        return unknown;
    }

    /**
     * Get row indexes.
     * <p>
     * TODO Determine the exact usage.
     * </p>
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The indexes or {@link #UNKNOWN_INT_ARRAY} if it was not available on the header.
     *
     * @see #isExtenededDbcVersion2Supported()
     */
    public int[] getRowIndexes() {
        return rowIndexes;
    }

    /**
     * Get row string lengths.
     * <p>
     * TODO Determine the exact usage.
     * </p>
     * <p>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     * </p>
     *
     * @return The string lengths or {@link #UNKNOWN_SHORT_ARRAY} if it was not available on the header.
     *
     * @see #isExtenededDbcVersion2Supported()
     */
    public short[] getRowStringLength() {
        return rowStringLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}