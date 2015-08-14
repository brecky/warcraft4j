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

package nl.salp.warcraft4j.data.dbc.mapping;

import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * DBC data type used for specifying and reading DBC-based files.
 *
 * @author Barre Dijkstra
 */
public enum DbcDataType {
    /** 8-bit signed byte, which is also used for non-numeric data. */
    BYTE(() -> Byte.class, () -> byte[].class, (field) -> DataTypeFactory.getByte(), (field) -> DataTypeFactory.getByteArray(field.numberOfEntries())),
    /** 8-bit unsigned integer {@code java.lang.Byte} wrapped in a 16-bit signed short. */
    UINT8(() -> Short.class, () -> Short[].class, (field) -> DataTypeFactory.getUnsignedByte()),
    /** 16-bit signed integer {@code java.lang.Short}. */
    INT16(() -> Short.class, () -> Short[].class, (field) -> DataTypeFactory.getShort()),
    /** 16-bit unsigned natural number ({@code java.lang.Short}) wrapped in a 32-bit signed integer. */
    UINT16(() -> Integer.class, () -> Integer[].class, (field) -> DataTypeFactory.getUnsignedShort()),
    /** 32-bit signed integer {@code java.lang.Integer}. */
    INT32(() -> Integer.class, () -> Integer[].class, (field) -> DataTypeFactory.getInteger()),
    /** 32-bit unsigned integer wrapped in a 64-bit signed long. */
    UINT32(() -> Integer.class, () -> Integer[].class, (field) -> DataTypeFactory.getInteger()),
    /** 64-bit signed integer {@code java.lang.Long}. */
    INT64(() -> Long.class, () -> Long[].class, (field) -> DataTypeFactory.getLong()),
    /**
     * 64-bit unsigned natural number (long).
     * <p/>
     * TODO {@link DataType} has data type available for unsigned longs at the moment, change this to use a datatype based on BigDecimal at a later stage.
     */
    UINT64(() -> Long.class, () -> Long[].class, (field) -> DataTypeFactory.getLong()),
    /** 32-bit signed floating point. */
    FLOAT(() -> Float.class, () -> Float[].class, (field) -> DataTypeFactory.getFloat()),
    /** 64-bit signed floating point. */
    DOUBLE(() -> Double.class, () -> Double[].class, (field) -> DataTypeFactory.getDouble()),
    /** String (either 0-terminated or fixed-length). */
    STRING(() -> String.class, () -> String[].class, (field) -> (field.length() > 0) ? DataTypeFactory.getFixedLengthString(field.length()) : DataTypeFactory.getTerminatedString()),
    /** StringTable reference. */
    STRINGTABLE_REFERENCE(() -> Integer.class, () -> Integer[].class, (field) -> DataTypeFactory.getInteger()),
    /** 8-bit Boolean. */
    BOOLEAN(() -> Integer.class, () -> Integer[].class, (field) -> DataTypeFactory.getInteger());

    private final Supplier<Class<?>> baseJavaTypeSupplier;
    private final Supplier<Class<?>> arrayJavaTypeSupplier;
    private final Function<DbcFieldMapping, DataType<?>> baseDataTypeFactory;
    private final Function<DbcFieldMapping, DataType<?>> arrayDataTypeFactory;

    /**
     * Create a new DbcDataType instance.
     *
     * @param baseJavaTypeSupplier  Supplier for the base Java type of the DbcDataType.
     * @param arrayJavaTypeSupplier Supplier for the array Java type of the DbcDataType.
     * @param baseDataTypeFactory       The function for creating the {@link DataType} instance of the DbcDataType.
     */
    DbcDataType(Supplier<Class<?>> baseJavaTypeSupplier, Supplier<Class<?>> arrayJavaTypeSupplier, Function<DbcFieldMapping, DataType<?>> baseDataTypeFactory) {
        this(baseJavaTypeSupplier, arrayJavaTypeSupplier, baseDataTypeFactory, (field) -> baseDataTypeFactory.apply(field).asArrayType(field.numberOfEntries()));
    }

    /**
     * Create a new DbcDataType instance.
     *
     * @param baseJavaTypeSupplier  Supplier for the base Java type of the DbcDataType.
     * @param arrayJavaTypeSupplier Supplier for the array Java type of the DbcDataType.
     * @param baseDataTypeFactory       The function for creating the {@link DataType} instance of the DbcDataType.
     */
    DbcDataType(Supplier<Class<?>> baseJavaTypeSupplier, Supplier<Class<?>> arrayJavaTypeSupplier, Function<DbcFieldMapping, DataType<?>> baseDataTypeFactory, Function<DbcFieldMapping, DataType<?>> arrayDataTypeFactory) {
        this.baseJavaTypeSupplier = baseJavaTypeSupplier;
        this.arrayJavaTypeSupplier = arrayJavaTypeSupplier;
        this.baseDataTypeFactory = baseDataTypeFactory;
        this.arrayDataTypeFactory = arrayDataTypeFactory;
    }

    /**
     * Get the Java type representation used for a single value.
     *
     * @return The Java type.
     */
    public Class<?> getBaseJavaType() {
        return baseJavaTypeSupplier.get();
    }

    /**
     * Get the Java type representation used for an array.
     *
     * @return The Java type.
     */
    public Class<?> getArrayJavaType() {
        return arrayJavaTypeSupplier.get();
    }

    /**
     * Get the {@link DataType} instance for a field used for a single value.
     *
     * @param field The DbcFieldMapping.
     *
     * @return The data type.
     */
    public DataType<?> getBaseDataType(DbcFieldMapping field) {
        return baseDataTypeFactory.apply(field);
    }

    /**
     * Get the {@link DataType} instance for a field used for an array.
     *
     * @param field The DbcFieldMapping.
     *
     * @return The data type.
     */
    public DataType<?> getArrayData(DbcFieldMapping field) {
        return arrayDataTypeFactory.apply(field);
    }
}
