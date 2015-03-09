package nl.salp.warcraft4j.wowclient.dbc.header;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
class Db2Header extends DbcHeader {
    public static final String MAGICSTRING = "WDB2";
    private static int HEADER_SIZE = 32;

    private int stringBlockHash;
    private int build;
    private int timestampLastWritten;

    public Db2Header(String magicString, int recordCount, int fieldCount, int recordSize, int stringBlockSize, int stringBlockHash, int build, int timestampLastWritten) {
        super(magicString, recordCount, fieldCount, recordSize, stringBlockSize);
        this.stringBlockHash = stringBlockHash;
        this.build = build;
        this.timestampLastWritten = timestampLastWritten;

    }

    @Override
    public int getHeaderSize() {
        return super.getHeaderSize();
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
     * Get the timestamp the DBC was last written.
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
