package nl.salp.warcraft4j.wowclient.dbc.header;

/**
 * TODO Document class.
 */
public interface Header {
    /**
     * Get the magic string of the DBC file.
     *
     * @return The magic string.
     */
    String getMagicString();

    /**
     * Get the size of the header block in bytes.
     *
     * @return The size.
     */
    public int getHeaderSize();

    /**
     * Get the number of records in the DBC file.
     *
     * @return The number of records.
     */
    int getRecordCount();

    /**
     * Get the number of fields per record.
     *
     * @return The number of fields.
     */
    int getFieldCount();

    /**
     * Get the size of a single record in bytes.
     *
     * @return The size.
     */
    int getRecordSize();

    /**
     * Get the offset where the block of records start.
     *
     * @return The offset.
     */
    int getRecordBlockOffset();

    /**
     * Get the size of the block of records in bytes.
     *
     * @return The size.
     */
    int getRecordBlockSize();

    /**
     * Get the offset where the StringTable block starts.
     *
     * @return The offset.
     */
    int getStringBlockOffset();

    /**
     * Get the size of the StringTable block in bytes.
     *
     * @return The size.
     */
    int getStringBlockSize();
}
