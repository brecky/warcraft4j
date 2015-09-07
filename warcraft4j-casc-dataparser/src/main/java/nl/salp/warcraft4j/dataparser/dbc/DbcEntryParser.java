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
package nl.salp.warcraft4j.dataparser.dbc;

import nl.salp.warcraft4j.dataparser.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.dataparser.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.dataparser.dbc.mapping.DbcFieldType;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.parser.RandomAccessDataParser;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Parser to parse {@link DbcEntry} instances from DBC/DB2/ADB file data.
 *
 * @author Barre Dijkstra
 */
class DbcEntryParser<T extends DbcEntry> extends RandomAccessDataParser<T> {
    /** The mapping type to create the entries for. */
    private final Class<T> mappingType;
    /** The parsed DBC file header. */
    private final DbcHeader header;
    /** The StringTable for the DBC file. */
    private final DbcStringTable stringTable;

    /**
     * Create a new entry parser.
     *
     * @param mappingType The mapping type to create the entries for.
     * @param header      The parsed DBC file header.
     * @param stringTable The StringTable for the DBC file.
     */
    public DbcEntryParser(Class<T> mappingType, DbcHeader header, DbcStringTable stringTable) {
        this.mappingType = mappingType;
        this.header = header;
        this.stringTable = stringTable;
    }

    @Override
    public T parse(DataReader reader) throws IOException, DbcParsingException {
        T instance = instantiate();
        SortedMap<Integer, Field> fields = parseFields(mappingType);
        for (int fieldIndex : fields.keySet()) {
            Field field = fields.get(fieldIndex);
            DbcFieldType fieldType = new DbcFieldType(field.getAnnotation(DbcFieldMapping.class));

            Object value = reader.readNext(fieldType.getDataType(), fieldType.getByteOrder());

            if (fieldType.isPadding()) {
                // Ignore.
            } else if (DbcDataType.STRINGTABLE_REFERENCE == fieldType.getDbcDataType()) {
                if (fieldType.isArray()) {
                    List<String> entries = new ArrayList<>(fieldType.getArraySize());
                    for (int i : (Integer[]) value) {
                        entries.add(getStringTableReference(i, stringTable));
                    }
                    setValue(field, entries.toArray(new String[fieldType.getArraySize()]), instance);
                } else {
                    setValue(field, getStringTableReference((int) value, stringTable), instance);
                }
            } else {
                setValue(field, value, instance);
            }
        }
        return instance;
    }

    @Override
    public int getInstanceDataSize() {
        return header.getEntrySize();
    }

    /**
     * Get the referenced value from a string table.
     *
     * @param value       The position of the value in the string table.
     * @param stringTable The string table to get the value from.
     *
     * @return The value if it could be found, the value position if no entry was found but the position > 0 or {@code null} if the position <= 0.
     */
    private String getStringTableReference(int value, DbcStringTable stringTable) {
        String stringValue;
        if (stringTable.isEntryAvailableForPosition(value)) {
            stringValue = stringTable.getEntry(value);
        } else if (value > 0) {
            stringValue = String.valueOf(value);
        } else {
            stringValue = null;
        }
        return stringValue;
    }

    /**
     * Create a new instance of the mapping type.
     *
     * @return The instance.
     */
    private T instantiate() {
        T instance;
        try {
            instance = mappingType.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Unable to instantiate " + mappingType.getName() + " with a zero-argument constructor.", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Not allowed to instantiate " + mappingType.getName() + " with a zero-argument constructor.", e);
        }
        return instance;
    }

    /**
     * Set the value of a field on a DbcEntry instance.
     * <p>
     * TODO Quick version and needs to be refactored heavily; use delegates, allow for field/method/constructor setting of values, clean-up.
     *
     * @param field    The field to set the value on.
     * @param value    The value to set.
     * @param instance The entry instance to set the value on.
     *
     * @throws DbcParsingException If the value could not be set.
     */
    private void setValue(Field field, Object value, T instance) throws DbcParsingException {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            if (field.getType() == byte.class) {
                field.setByte(instance, ((Byte) value).byteValue());
            } else if (field.getType() == char.class) {
                field.setChar(instance, ((Character) value).charValue());
            } else if (field.getType() == char[].class) {
                field.set(instance, ArrayUtils.toPrimitive((Character[]) value));
            } else if (field.getType() == boolean.class) {
                field.setBoolean(instance, ((Boolean) value).booleanValue());
            } else if (field.getType() == boolean[].class) {
                field.set(instance, ArrayUtils.toPrimitive(((Boolean[]) value)));
            } else if (field.getType() == short.class) {
                field.setShort(instance, ((Short) value).shortValue());
            } else if (field.getType() == short[].class) {
                field.set(instance, ArrayUtils.toPrimitive(((Short[]) value)));
            } else if (field.getType() == int.class) {
                if (Long.class.isAssignableFrom(value.getClass())) {
                    System.out.println("Long -> Int cast failure inc.");
                }
                field.setInt(instance, ((Integer) value).intValue());
            } else if (field.getType() == int[].class) {
                field.set(instance, ArrayUtils.toPrimitive(((Integer[]) value)));
            } else if (field.getType() == long.class) {
                field.setLong(instance, ((Long) value).longValue());
            } else if (field.getType() == long[].class) {
                field.set(instance, ArrayUtils.toPrimitive(((Long[]) value)));
            } else if (field.getType() == float.class) {
                field.setFloat(instance, ((Float) value).floatValue());
            } else if (field.getType() == float[].class) {
                field.set(instance, ArrayUtils.toPrimitive(((Float[]) value)));
            } else if (field.getType() == double.class) {
                field.setDouble(instance, ((Double) value).doubleValue());
            } else if (field.getType() == double[].class) {
                field.set(instance, ArrayUtils.toPrimitive(((Double[]) value)));
            } else {
                field.set(instance, value);
            }
        } catch (IllegalAccessException e) {
            throw new DbcParsingException("Unable to set value " + value + " on " + mappingType.getName() + "#" + field.getName(), e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    /**
     * Parse all mapped fields from the mapping type.
     *
     * @param template The mapping type.
     * @param <K>      The type of the mapping type.
     *
     * @return The mapped fields, indexed by the parsing order.
     */
    private <K extends DbcEntry> SortedMap<Integer, Field> parseFields(Class<K> template) {
        SortedMap<Integer, Field> fields = new TreeMap<>();
        for (Field f : template.getDeclaredFields()) {
            if (f.isAnnotationPresent(DbcFieldMapping.class)) {
                DbcFieldMapping meta = f.getAnnotation(DbcFieldMapping.class);
                if (fields.containsKey(meta.order())) {
                    throw new IllegalArgumentException("Double mapping found for column " + meta.order() + " in model " + template.getName());
                }
                fields.put(meta.order(), f);
            }
        }
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("No fields annotated with @DbcFieldMapping mapping information.");
        }
        return fields;
    }
}
