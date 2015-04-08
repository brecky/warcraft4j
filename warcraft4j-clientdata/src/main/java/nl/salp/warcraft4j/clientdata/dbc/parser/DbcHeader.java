package nl.salp.warcraft4j.clientdata.dbc.parser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    /**
     * The header type.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final String magicString;

    /**
     * The size of the header data block in bytes.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int headerSize;
    /**
     * The number of entries in the DBC file.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int entryCount;
    /**
     * The number of fields in an entry.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int entryFieldCount;
    /**
     * The size of a single entry in bytes.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int entrySize;
    /**
     * The size of the StringTable data block.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DBC}</li>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int stringTableBlockSize;
    /**
     * The hash of the StringTable data block.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int stringTableBlockHash;
    /**
     * The World of Warcraft client build number.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int buildNumber;
    /**
     * The timestamp when the DBC file was last altered/written.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2}</li>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int timestampLastWritten;
    /**
     * The minimum id.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int minimumEntryId;
    /**
     * The maximum id.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int maximumEntryId;
    /**
     * The id of the locale the DBC file is in.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int locale;
    /**
     * Unknown 4-byte segment.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final byte[] unknown;
    /**
     * The row indexes.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
     */
    private final int[] rowIndexes;
    /**
     * The row String length in bytes.
     * <p/>
     * <p/>
     * Available for
     * <ul>
     * <li>{@code DB2_EXT}</li>
     * </ul>
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
    public DbcHeader(String magicString, int headerSize, int entryCount, int entryFieldCount, int entrySize, int stringTableBlockSize, int stringTableBlockHash, int buildNumber, int timestampLastWritten) {
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
     */
    public DbcHeader(String magicString, int headerSize, int entryCount, int entryFieldCount, int entrySize, int stringTableBlockSize, int stringTableBlockHash, int buildNumber, int timestampLastWritten, int minimumEntryId, int maximumEntryId, int locale, byte[] unknown, int[] rowIndexes, short[] rowStringLength) {
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
    }

    /**
     * Get the file magic string.
     *
     * @return The magic string.
     */
    public String getMagicString() {
        return magicString;
    }

    /**
     * Get the size of the header block for the header in bytes.
     *
     * @return The size.
     */
    public int getHeaderSize() {
        return headerSize;
    }

    /**
     * Get the number of entries in the file.
     *
     * @return The number of entries.
     */
    public int getEntryCount() {
        return entryCount;
    }

    /**
     * Get the number of fields in an entry in the file.
     *
     * @return The number of fields.
     */
    public int getEntryFieldCount() {
        return entryFieldCount;
    }

    /**
     * Get the size in bytes of a single entry in the file.
     *
     * @return The size in bytes.
     */
    public int getEntrySize() {
        return entrySize;
    }

    /**
     * Get the size in bytes of the entry data block in the file.
     *
     * @return The size.
     */
    public int getEntryBlockSize() {
        return entryCount * entrySize;
    }

    /**
     * Get the starting offset of the entry data block in the file.
     *
     * @return The starting offset.
     */
    public int getEntryBlockStartingOffset() {
        return headerSize;
    }

    /**
     * Get the size of the StringTable data block in bytes.
     *
     * @return The size.
     */
    public int getStringTableBlockSize() {
        return stringTableBlockSize;
    }

    /**
     * Get the starting offset of the string table data block in the file.
     *
     * @return The starting offset.
     */
    public int getStringTableStartingOffset() {
        return headerSize + getEntryBlockSize();
    }

    /**
     * Get the hash of the StringTable block.
     *
     * @return The hash or {@link #UNKNOWN_INT} if it was not available on the header.
     */
    public int getStringTableBlockHash() {
        return stringTableBlockHash;
    }

    /**
     * Get the build number of the World of Warcraft client.
     *
     * @return The build number or {@link #UNKNOWN_INT} if it was not available on the header.
     */
    public int getBuildNumber() {
        return buildNumber;
    }

    /**
     * Get the timestamp the file was last altered.
     *
     * @return The timestamp or {@link #UNKNOWN_INT} if it was not available on the header.
     */
    public int getTimestampLastWritten() {
        return timestampLastWritten;
    }

    /**
     * Get the minimum id of the entries in the file.
     *
     * @return The minimum id or {@link #UNKNOWN_INT} if it was not available on the header.
     */
    public int getMinimumEntryId() {
        return minimumEntryId;
    }

    /**
     * Get the maximum id of the entries in the file.
     *
     * @return The maximum id or {@link #UNKNOWN_INT} if it was not available on the header.
     */
    public int getMaximumEntryId() {
        return maximumEntryId;
    }

    /**
     * Get the id of the locale the file is in.
     *
     * @return The locale id or {@link #UNKNOWN_INT} if it was not available on the header.
     */
    public int getLocale() {
        return locale;
    }

    /**
     * Get values of the unknown 4-byte data segment.
     * <p/>
     * TODO This is probably an uint32, figure out where it's used for.
     *
     * @return The data segment or {@link #UNKNOWN_BYTE_ARRAY} if it was not available on the header.
     */
    public byte[] getUnknown() {
        return unknown;
    }

    /**
     * Get row indexes.
     * <p/>
     * TODO Determine the exact usage.
     *
     * @return The indexes or {@link #UNKNOWN_INT_ARRAY} if it was not available on the header.
     */
    public int[] getRowIndexes() {
        return rowIndexes;
    }

    /**
     * Get row string lengths.
     * <p/>
     * TODO Determine the exact usage.
     *
     * @return The string lengths or {@link #UNKNOWN_SHORT_ARRAY} if it was not available on the header.
     */
    public short[] getRowStringLength() {
        return rowStringLength;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Builder for creating a DbcHeader instance.
     */
    public static class Builder {
        private String magicString;
        private int headerSize;
        private int entryCount;
        private int entryFieldCount;
        private int entrySize;
        private int stringTableBlockSize;
        private int stringTableBlockHash;
        private int buildNumber;
        private int timestampLastWritten;
        private int minId;
        private int maxId;
        private int locale;
        private byte[] unknown;
        private int[] rowIndexes;
        private short[] rowStringLength;

        public Builder() {
            stringTableBlockHash = UNKNOWN_INT;
            buildNumber = UNKNOWN_INT;
            timestampLastWritten = UNKNOWN_INT;
            minId = UNKNOWN_INT;
            maxId = UNKNOWN_INT;
            locale = UNKNOWN_INT;
            unknown = UNKNOWN_BYTE_ARRAY;
            rowIndexes = UNKNOWN_INT_ARRAY;
            rowStringLength = UNKNOWN_SHORT_ARRAY;
        }

        public Builder withMagicString(String magicString) {
            this.magicString = magicString;
            return this;
        }

        public Builder withHeaderSize(int headerSize) {
            this.headerSize = headerSize;
            return this;
        }

        public Builder withEntryCount(int entryCount) {
            this.entryCount = entryCount;
            return this;
        }

        public Builder withEntryFieldCount(int entryFieldCount) {
            this.entryFieldCount = entryFieldCount;
            return this;
        }

        public Builder withSingleEntrySize(int recordSize) {
            this.entrySize = recordSize;
            return this;
        }

        public Builder withStringTableBlockSize(int stringTableBlockSize) {
            this.stringTableBlockSize = stringTableBlockSize;
            return this;
        }

        public Builder withStringTableBlockHash(int stringTableBlockHash) {
            this.stringTableBlockHash = stringTableBlockHash;
            return this;
        }

        public Builder withBuildNumber(int buildNumber) {
            this.buildNumber = buildNumber;
            return this;
        }

        public Builder withTimestampLastWritten(int timestampLastWritten) {
            this.timestampLastWritten = timestampLastWritten;
            return this;
        }

        public Builder withMinEntryId(int minId) {
            this.minId = minId;
            return this;
        }

        public Builder withMaxEntryId(int maxId) {
            this.maxId = maxId;
            return this;
        }

        public Builder withLocaleId(int locale) {
            this.locale = locale;
            return this;
        }

        public Builder withUnknownDataBlock(byte[] unknown) {
            this.unknown = unknown;
            return this;
        }

        public Builder withRowIndexes(int[] rowIndexes) {
            this.rowIndexes = rowIndexes;
            return this;
        }

        public Builder withRowStringLength(short[] rowStringLength) {
            this.rowStringLength = rowStringLength;
            return this;
        }

        public DbcHeader build() {
            return new DbcHeader(magicString, headerSize, entryCount, entryFieldCount, entrySize, stringTableBlockSize, stringTableBlockHash, buildNumber, timestampLastWritten, minId, maxId, locale, unknown, rowIndexes, rowStringLength);
        }
    }
}
