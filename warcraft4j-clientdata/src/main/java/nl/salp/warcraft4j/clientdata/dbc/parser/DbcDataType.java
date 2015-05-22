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

package nl.salp.warcraft4j.clientdata.dbc.parser;

import nl.salp.warcraft4j.clientdata.io.DataType;

import java.nio.ByteOrder;

/**
 * DBC data type used for specifying and reading DBC-based files.
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
            return DataType.getByteArray(field.numberOfEntries());
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
    }),
    /** 64-bit unsigned Long. */
    ULONG(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Long.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Long[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getLong();
        }
    }),
    /** 64-bit signed Long. */
    LONG(new DataTypeAdapter() {
        @Override
        protected Class<?> getBaseClass() {
            return Long.class;
        }

        @Override
        protected Class<?> getArrayClass() {
            return Long[].class;
        }

        @Override
        protected DataType<?> getDataTypeInstance(DbcField field) {
            return DataType.getLong();
        }
    });

    /** The adapter to use for mapping between data types. */
    private final DataTypeAdapter adapter;

    /**
     * Create a new DbcDataType instance.
     *
     * @param adapter The {@link DataTypeAdapter} instance to use for mapping between data types.
     */
    private DbcDataType(DataTypeAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Get the data type for the given field.
     *
     * @param field The field.
     *
     * @return The data type.
     */
    public DataType<?> getDataType(DbcField field) {
        return adapter.getDataType(field);
    }

    /**
     * Get the byte order for the field.
     * <p/>
     * <p/>
     * If non is explicitly specified, the default byte order for the data type is returned.
     *
     * @param field The field.
     *
     * @return The byte order.
     */
    public ByteOrder getByteOrder(DbcField field) {
        ByteOrder bo;
        if (field.endianess() < 0) {
            bo = ByteOrder.LITTLE_ENDIAN;
        } else if (field.endianess() > 0) {
            bo = ByteOrder.BIG_ENDIAN;
        } else {
            bo = getDataType(field).getDefaultByteOrder();
        }
        return bo;
    }

    /**
     * Get the Java type representing the DBC field data type.
     *
     * @param field The field.
     *
     * @return The Java type.
     */
    public Class<?> getJavaType(DbcField field) {
        return adapter.getJavaType(field);
    }

    /**
     * Check if the field represents an array.
     *
     * @param field The field.
     *
     * @return {@code true} if the field is an array.
     */
    public boolean isArray(DbcField field) {
        return adapter.isArray(field);
    }

    /**
     * Data type adapter, for mapping between the DbcDataType instances and the data type parsers.
     */
    private static abstract class DataTypeAdapter {
        /**
         * Get the Java type for the given field.
         *
         * @param field The field.
         *
         * @return The Java type for the field.
         */
        public Class<?> getJavaType(DbcField field) {
            return (field.numberOfEntries() > 1) ? getArrayClass() : getBaseClass();
        }

        /**
         * Get the Java type for the DbcDataType.
         *
         * @return The Java type.
         */
        protected abstract Class<?> getBaseClass();

        /**
         * Get the Java array type for the DbcDataType.
         *
         * @return The Java array type.
         */
        protected abstract Class<?> getArrayClass();

        /**
         * Check if the given field is an array.
         *
         * @param field The field.
         *
         * @return {@code true} if the field is an array, {@code false} if not.
         */
        public boolean isArray(DbcField field) {
            return field.numberOfEntries() > 0;
        }

        /**
         * Get a {@link DataType} instance for the field.
         *
         * @param field The field to get the DataType instance for.
         *
         * @return The DataType instance.
         */
        protected abstract DataType<?> getDataTypeInstance(DbcField field);

        /**
         * Get an array DataType instance for the given field.
         *
         * @param dataType The data type.
         * @param field    The field to get the array for.
         *
         * @return The array DataType.
         */
        protected DataType<?> getArray(DataType<?> dataType, DbcField field) {
            return dataType.asArrayType(field.numberOfEntries());
        }

        /**
         * Get the data type for the given field.
         *
         * @param field The field.
         *
         * @return The DataType for the field.
         */
        public DataType<?> getDataType(DbcField field) {
            DataType dt = getDataTypeInstance(field);
            return (field.numberOfEntries() > 1) ? getArray(dt, field) : dt;
        }
    }
}
