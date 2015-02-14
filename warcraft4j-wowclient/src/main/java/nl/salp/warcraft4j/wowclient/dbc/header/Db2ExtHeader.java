package nl.salp.warcraft4j.wowclient.dbc.header;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
class Db2ExtHeader extends Db2Header {
    public static final int LAST_BUILD_PRE_EXTENDED_HEADER = 12880;
    private static final int HEADER_SIZE_BASE = 48;
    private int minId;
    private int maxId;
    private int locale;
    // 4-bytes.
    private byte[] unknown;
    private int[] rowIndexes;
    private short[] rowStringLength;

    public Db2ExtHeader(String magicString, int recordCount, int fieldCount, int recordSize, int stringBlockSize, int stringBlockHash, int build, int timestampLastWritten, int minId, int maxId, int locale, byte[] unknown, int[] rowIndexes, short[] rowStringLength) {
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
     * Get the unknown value.
     *
     * @return The unknown value.
     */
    public byte[] getUnknown() {
        return unknown;
    }

    public int[] getRowIndexes() {
        return rowIndexes;
    }

    public short[] getRowStringLength() {
        return rowStringLength;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
