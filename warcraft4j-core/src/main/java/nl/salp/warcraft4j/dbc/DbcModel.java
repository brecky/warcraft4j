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

package nl.salp.warcraft4j.dbc;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class DbcModel<T> {
    private Class<T> modelClass;
    private SortedMap<Integer, Field> fields;

    public DbcModel(Class<T> modelClass) {
        this.modelClass = modelClass;
        if (!modelClass.isAnnotationPresent(Dbc.class)) {
            throw new IllegalArgumentException("Can't create a DbcModel for a non @Dbc annotated class.");
        }
        fields = new TreeMap<>();
        parseFields();
    }

    private void parseFields() {
        for (Field f : modelClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(DbcField.class)) {
                DbcField meta = f.getAnnotation(DbcField.class);
                if (fields.containsKey(meta.column())) {
                    throw new IllegalArgumentException("Double mapping found for column " + meta.column() + " in model " + modelClass.getName());
                }
                fields.put(meta.column(), f);
            }
        }
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("No fields annotated with @DbcField mapping information.");
        }
    }

    public List<T> parse(DbcHeader header, byte[] entryData, byte[] stringsData) {
        List<T> entries = new ArrayList<>(header.getRecordCount());
// TODO Compare mapped field count of model with header.fieldSize
// TODO Compare last column of model with header.recordSize
// TODO Compare entryData.length with (header.recordCount * header.recordSize)
        Map<Integer, String> strings = parseStrings(stringsData);
        ByteBuffer byteBuffer = ByteBuffer.wrap(entryData).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < header.getRecordCount(); i++) {
            T entry = instantiate();
            setData(entry, byteBuffer, strings);
            entries.add(entry);
        }
        return entries;
    }

    private Map<Integer, String> parseStrings(byte[] stringData) {
        Map<Integer, String> strings = new HashMap<>();

        ByteBuffer byteBuffer = ByteBuffer.wrap(stringData).order(ByteOrder.LITTLE_ENDIAN);
        while (byteBuffer.hasRemaining()) {
            int position = byteBuffer.position();
            String value = readNextString(byteBuffer);
            if (!value.isEmpty()) {
                strings.put(position, value);
            }
        }
        return strings;
    }

    private String readNextString(ByteBuffer buffer) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte c;
        while ((c = buffer.get()) != 0) {
            baos.write(c);
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }


    private void setData(T entry, ByteBuffer byteBuffer, Map<Integer, String> strings) {
        for (int fieldIndex : fields.keySet()) {
            Field field = fields.get(fieldIndex);
            DbcField meta = field.getAnnotation(DbcField.class);
            Object value = getData(meta, byteBuffer, strings);

            setValue(field, value, entry);
        }
    }

    private Object getData(DbcField meta, ByteBuffer data, Map<Integer, String> strings) {
        Object value;
        switch (meta.dataType()) {
            case FLOAT:
                value = data.getFloat();
                break;
            case UINT32:
                // TODO Implement difference between unsigned and signed integers.
            case INT32:
                value = data.getInt();
                break;
            case STRING:
                int length = meta.length();
                byte[] stringData = new byte[length];
                data.get(stringData);
                value = new String(stringData, StandardCharsets.UTF_8);
                break;
            case BOOLEAN:
                value = (data.get() != -1);
                break;
            case BYTE:
                value = data.get();
                break;
            case STRING_REFERENCE:
                int reference = data.getInt();
                if (strings.containsKey(reference)) {
                    value = strings.get(reference);
                } else {
                    // TODO Add error handling.
                    value = "<< REF " + String.valueOf(reference) + ">>";
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown data-type: " + meta.dataType());
        }
        return value;
    }

    private T instantiate() {
        T instance;
        try {
            instance = modelClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Unable to instantiate " + modelClass.getName() + " with a zero-argument constructor.", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to instantiate " + modelClass.getName() + " with a zero-argument constructor.", e);
        }
        return instance;
    }

    private void setValue(Field field, Object value, T instance) {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            if (field.getType() == byte.class) {
                field.setByte(instance, ((Byte) value).byteValue());
            } else if (field.getType() == boolean.class) {
                field.setBoolean(instance, ((Boolean) value).booleanValue());
            } else if (field.getType() == int.class) {
                field.setInt(instance, ((Integer) value).intValue());
            } else if (field.getType() == float.class) {
                field.setFloat(instance, ((Float) value).floatValue());
            } else {
                field.set(instance, value);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to set value " + value + " on " + modelClass.getName() + "#" + field.getName(), e);
        } finally {
            field.setAccessible(accessible);
        }

    }


}
