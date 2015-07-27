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
package nl.salp.warcraft4j.clientdata.dbc.mapping;

import nl.salp.warcraft4j.clientdata.io.datatype.DataType;

import java.nio.ByteOrder;

/**
 * {@link DbcFieldMapping} data type.
 *
 * @author Barre Dijkstra
 */
public class DbcFieldType {
    /** The {@link DbcFieldMapping} the field type is created for. */
    private final DbcFieldMapping field;
    /** The {@link DbcDataType} of the field the type is created for. */
    private final DbcDataType dataType;

    /**
     * Create a new DbcFieldType.
     *
     * @param field The field to create the type for.
     *
     * @throws IllegalArgumentException When an invalid {@link DbcFieldMapping} was provided.
     */
    public DbcFieldType(DbcFieldMapping field) throws IllegalArgumentException {
        if (field == null) {
            throw new IllegalArgumentException("Can't create a DbcFieldType for a null DbcFieldMapping.");
        }
        this.field = field;
        this.dataType = field.dataType();
    }

    /**
     * Get the {@link DbcDataType} for the field.
     *
     * @return The {@link DbcDataType}.
     */
    public DbcDataType getDbcDataType() {
        return dataType;
    }

    /**
     * Check if the field is an array.
     *
     * @return {@code true} if the field is an array.
     */
    public boolean isArray() {
        return field.numberOfEntries() > 1;
    }

    /**
     * Get the array size of the field.
     *
     * @return The array size or {@code 1} when the field is not an array.
     */
    public int getArraySize() {
        return field.numberOfEntries();
    }

    /**
     * Get the Java type for the field that is used for single values.
     *
     * @return The Java type.
     *
     * @see DbcFieldType#getJavaType()
     */
    public Class<?> getBaseJavaType() {
        return dataType.getBaseJavaType();
    }

    /**
     * Get the Java type for the field that is used for arrays.
     *
     * @return The Java type.
     *
     * @see DbcFieldType#getJavaType()
     */
    public Class<?> getArrayJavaType() {
        return dataType.getArrayJavaType();
    }

    /**
     * Get the Java type of the field, being either an array type or a base type.
     *
     * @return The Java type.
     *
     * @see DbcFieldType#getBaseJavaType()
     * @see DbcFieldType#getArrayJavaType()
     */
    public Class<?> getJavaType() {
        return isArray() ? dataType.getArrayJavaType() : dataType.getBaseJavaType();
    }

    /**
     * Get the {@link DataType} for the field that is used for single values.
     *
     * @return The {@link DataType}.
     *
     * @see DbcFieldType#getDataType()
     */
    public DataType<?> getBaseDataType() {
        return dataType.getBaseDataType(field);
    }

    /**
     * Get the {@link DataType} for the field that is used for arrays.
     *
     * @return The {@link DataType}.
     *
     * @see DbcFieldType#getDataType()
     */
    public DataType<?> getArrayDataType() {
        return dataType.getArrayData(field);
    }

    /**
     * Get the {@link DataType} of the field, being either an array type or a base type.
     *
     * @return The {@link DataType}.
     *
     * @see DbcFieldType#getBaseDataType()
     * @see DbcFieldType#getArrayDataType()
     */
    public DataType<?> getDataType() {
        return isArray() ? getArrayDataType() : getBaseDataType();
    }

    /**
     * Get the {@code ByteOrder} for the field.
     *
     * @return The ByteOrder.
     */
    public ByteOrder getByteOrder() {
        ByteOrder order;
        if (field.endianess() < 0) {
            order = ByteOrder.LITTLE_ENDIAN;
        } else if (field.endianess() > 0) {
            order = ByteOrder.BIG_ENDIAN;
        } else {
            order = getDataType().getDefaultByteOrder();
        }
        return order;
    }

    /**
     * Check if the field is a padding field (e.g. is not being mapped).
     *
     * @return {@code true} if the field is a padding field.
     */
    public boolean isPadding() {
        return field.padding();
    }

    /**
     * Get the DBC data size of a field.
     *
     * @return The data size in bytes or {@code 0} for a variable sized data size.
     */
    public int getDataSize() {
        int size;
        if (field.length() > 0) {
            size = field.length() * field.numberOfEntries();
        } else {
            size = (getBaseDataType().getLength() * field.numberOfEntries());
        }
        return size;
    }
}
