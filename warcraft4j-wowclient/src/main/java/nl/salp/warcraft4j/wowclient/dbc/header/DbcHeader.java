package nl.salp.warcraft4j.wowclient.dbc.header;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
class DbcHeader implements Header {
    public static final String MAGICSTRING = "WDBC";
    private static final int HEADER_SIZE = 20;

    // 4-byte, UTF-8
    private String magicString;
    private int recordCount;
    private int fieldCount;
    private int recordSize;
    private int stringBlockSize;

    public DbcHeader(String magicString, int recordCount, int fieldCount, int recordSize, int stringBlockSize) {
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
