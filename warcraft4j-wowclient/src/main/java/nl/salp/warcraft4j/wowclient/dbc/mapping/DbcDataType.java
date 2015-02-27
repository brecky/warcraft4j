package nl.salp.warcraft4j.wowclient.dbc.mapping;

import nl.salp.warcraft4j.wowclient.io.DataType;

/**
 * DBC data type.
 *
 * @author Barre Dijkstra
 */
public enum DbcDataType {
    /** 32-bit signed integer. */
    INT32(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Integer.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Integer[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getInteger();
        }
    }),
    /** 32-bit unsigned integer. */
    UINT32(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Integer.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Integer[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getInteger();
        }
    }),
    /** 32-bit floating point. */
    FLOAT(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Float.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Float[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getFloat();
        }

    }),
    /** String (either 0-terminated or fixed-length). */
    STRING(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return String.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return String[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return (field.length() > 0) ? DataType.getFixedLengthString(field.length()) : DataType.getTerminatedString();
        }
    }),
    /** StringTable reference. */
    STRINGTABLE_REFERENCE(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Integer.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Integer[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getInteger();
        }
    }),
    /** 8-bit Boolean. */
    BOOLEAN(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Integer.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Integer[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getInteger();
        }
    }),
    /** 8-bit Byte. */
    BYTE(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Byte.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return byte[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getByte();
        }

        @Override
        protected DataType<?> getArray(DataType<?> dataType, DbcField field) {
            return DataType.getByteArray(field.length());
        }
    }),
    /** 16-bit signed Short. */
    SHORT(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Short.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Short[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getShort();
        }
    });

    private final DataTypeAdapter adapter;

    DbcDataType(DataTypeAdapter adapter) {
        this.adapter = adapter;
    }

    public DataType<?> getDataType(DbcField field) {
        return adapter.getDataType(field);
    }

    public Class<?> getJavaType(DbcField field) {
        return adapter.getJavaType(field);
    }

    public boolean isArray(DbcField field) {
        return adapter.isArray(field);
    }

    private static abstract class DataTypeAdapter {

        public Class<?> getJavaType(DbcField field) {
            return (field.numberOfEntries() > 1) ? getArrayClass() : getBaseClass();
        }

        protected abstract Class<?> getBaseClass();

        protected abstract Class<?> getArrayClass();

        public boolean isArray(DbcField field) {
            return field.numberOfEntries() > 0;
        }

        protected abstract DataType<?> getDataTypeInstance(DbcField field);

        protected DataType<?> getArray(DataType<?> dataType, DbcField field) {
            return dataType.asArrayType(field.numberOfEntries());
        }

        public DataType<?> getDataType(DbcField field) {
            DataType dt = getDataTypeInstance(field);
            return (field.numberOfEntries() > 1) ? getArray(dt, field) : dt;
        }


    }
}
