package nl.salp.warcraft4j.wowclient.dbc;

import nl.salp.warcraft4j.wowclient.io.DataType;

/**
 * DBC data type.
 *
 * @author Barre Dijkstra
 */
public enum DbcDataType {
    /** 32bit signed integer. */
    INT32("int32", DataType.getInt32()),
    /** 32bit unsigned integer. */
    UINT32("uint32_t", DataType.getInt32()),
    /** 32bit floating point. */
    FLOAT("float", DataType.getFloat()),
    /** String. */
    STRING("string", DataType.getTerminatedString()),
    /** StringTable reference. */
    STRINGTABLE_REFERENCE("stringref", DataType.getInt32()),
    /** Boolean. */
    BOOLEAN("boolean", DataType.getBoolean()),
    /** Byte. */
    BYTE("byte", DataType.getByte());

    /** The DataType associated with the type. */
    private final DataType<?> dataType;
    /** The DBC specification name. */
    private final String dbcName;

    /**
     * Create a new DbcDataType instance.
     *
     * @param dbcName  The DBC specification name.
     * @param dataType The DataType associated with the type.
     */
    private DbcDataType(String dbcName, DataType<?> dataType) {
        this.dbcName = dbcName;
        this.dataType = dataType;
    }

    /**
     * Get the DBC specification name of the data type.
     *
     * @return The DBC name.
     */
    public String getDbcName() {
        return dbcName;
    }

    /**
     * Get the {@link DataType} that is associated with the DBC data type.
     *
     * @return The DataType.
     */
    public DataType<?> getDataType() {
        return dataType;
    }

    /**
     * Get the DbcDataType instance for the given DBC type name.
     *
     * @param dbcName The name.
     *
     * @return The DbcDataType instance or {@code null} if none was found for the given name.
     */
    public static DbcDataType getDbcDataType(String dbcName) {
        DbcDataType type = null;
        for (DbcDataType t : values()) {
            if (t.getDbcName().equals(dbcName)) {
                type = t;
                break;
            }
        }
        return type;
    }
}
