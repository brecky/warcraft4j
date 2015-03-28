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

package nl.salp.warcraft4j.files.clientdatabase.parser;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.io.ByteArrayDataReader;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataType;
import nl.salp.warcraft4j.io.FileDataReader;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabaseFileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDatabaseFileParser.class);

    private <T extends ClientDatabaseEntry> DbcFile getDbcFile(Class<T> template) {
        DbcFile dbcFile = template.getAnnotation(DbcFile.class);
        if (dbcFile == null) {
            throw new IllegalArgumentException(format("Unable to parse the template class %s with no DbcFile annotation.", template.getName()));
        }
        return dbcFile;
    }

    private <T extends ClientDatabaseEntry> SortedMap<Integer, Field> parseFields(Class<T> template) {
        SortedMap<Integer, Field> fields = new TreeMap<>();
        for (Field f : template.getDeclaredFields()) {
            if (f.isAnnotationPresent(DbcField.class)) {
                DbcField meta = f.getAnnotation(DbcField.class);
                if (fields.containsKey(meta.order())) {
                    throw new IllegalArgumentException("Double mapping found for column " + meta.order() + " in model " + template.getName());
                }
                fields.put(meta.order(), f);
            }
        }
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("No fields annotated with @DbcField mapping information.");
        }
        return fields;
    }

    public ClientDatabaseFile parseFile(String filename, String basePath) throws IOException, IllegalArgumentException {
        LOGGER.debug(format("[parse::%s] Parsing %s in %s", filename, filename, basePath));
        File file = new File(basePath, filename);
        try (DataReader reader = new FileDataReader(file)) {
            ClientDatabaseHeader header = new ClientDatabaseHeaderParser().parse(reader);
            LOGGER.debug(format("[parseFile::%s] Parsing type %s with %d records with %d fields and %d bytes per record.", filename, header.getMagicString(), header.getRecordCount(), header.getFieldCount(), header.getRecordSize()));
            byte[] entryData = reader.readNextBytes(header.getRecordBlockSize());
            byte[] stringBlockData = reader.readNextBytes(header.getStringBlockSize());
            ClientDatabaseStringBlock stringBlock = new ClientDatabaseStringBlockParser().parse(stringBlockData);
            LOGGER.debug(format("[parseFile::%s] Parsed %d bytes of StringBlock data to %d StringBlock entries.", filename, stringBlockData.length, stringBlock.getAvailablePositions().size()));
            return new ClientDatabaseFile(filename, header, entryData, stringBlock);
        }
    }


    public <T extends ClientDatabaseEntry> Set<T> parse(Class<T> template, String basePath) throws IOException, IllegalArgumentException {
        LOGGER.debug(format("[parse::%s] Parsing %s from %s in %s", template.getSimpleName(), template.getName(), getDbcFile(template).file(), basePath));
        File file = new File(basePath, getDbcFile(template).file());
        try (DataReader reader = new FileDataReader(file)) {
            return parse(template, reader);
        }
    }

    private <T extends ClientDatabaseEntry> Set<T> parse(Class<T> template, DataReader reader) throws IOException {
        ClientDatabaseHeader header = new ClientDatabaseHeaderParser().parse(reader);
        LOGGER.debug(format("[parse::%s] Parsing type %s with %d records with %d fields and %d bytes per record.", template.getSimpleName(), header.getMagicString(), header.getRecordCount(), header.getFieldCount(), header.getRecordSize()));
        byte[] entryData = reader.readNextBytes(header.getRecordBlockSize());
        byte[] stringBlockData = reader.readNextBytes(header.getStringBlockSize());
        ClientDatabaseStringBlock stringBlock = new ClientDatabaseStringBlockParser().parse(stringBlockData);
        LOGGER.debug(format("[parse::%s] Parsed %d bytes of StringBlock data to %d StringBlock entries.", template.getSimpleName(), stringBlockData.length, stringBlock.getAvailablePositions().size()));
        return parseEntries(template, entryData, header, stringBlock);
    }

    private <T extends ClientDatabaseEntry> Set<T> parseEntries(Class<T> template, byte[] data, ClientDatabaseHeader header, ClientDatabaseStringBlock stringBlock) throws IOException {
        Set<T> entries = new HashSet<>(header.getRecordCount());

        DataReader reader = new ByteArrayDataReader(data);

        for (int i = 0; i < header.getRecordCount(); i++) {
            T instance = instantiate(template);
            populate(template, instance, reader, stringBlock);
            entries.add(instance);
        }
        return entries;
    }

    private <T extends ClientDatabaseEntry> void populate(Class<T> template, T instance, DataReader reader, ClientDatabaseStringBlock stringBlock) throws IOException {
        SortedMap<Integer, Field> fields = parseFields(template);
        for (int fieldIndex : fields.keySet()) {
            Field field = fields.get(fieldIndex);
            DbcField fieldInfo = field.getAnnotation(DbcField.class);
            DataType<?> dataType = fieldInfo.dataType().getDataType(fieldInfo);
            Object value = reader.readNext(dataType);

            if (fieldInfo.padding()) {
                LOGGER.debug(format("[parse::%s] Ignoring padded field with order %d, type %s[%d] and value '%s'.", template.getSimpleName(), fieldInfo.order(), fieldInfo.dataType(), fieldInfo.numberOfEntries(), value));
            } else if (DbcDataType.STRINGTABLE_REFERENCE == fieldInfo.dataType()) {
                if (fieldInfo.numberOfEntries() > 1) {
                    List<String> entries = new ArrayList<>(fieldInfo.numberOfEntries());
                    for (int i : (Integer[]) value) {
                        entries.add(getStringTableReference(i, stringBlock));
                    }
                    setValue(template, field, entries.toArray(new String[fieldInfo.numberOfEntries()]), instance, fieldInfo);
                } else {
                    setValue(template, field, getStringTableReference((int) value, stringBlock), instance, fieldInfo);
                }
            } else {
                setValue(template, field, value, instance, fieldInfo);
            }

        }
    }

    private String getStringTableReference(int value, ClientDatabaseStringBlock stringBlock) {
        String stringValue;
        if (stringBlock.isEntryAvailableForPosition(value)) {
            stringValue = stringBlock.getEntry(value);
        } else if (value > 0) {
            stringValue = String.valueOf(value);
        } else {
            stringValue = null;
        }
        return stringValue;
    }

    private <T extends ClientDatabaseEntry> T instantiate(Class<T> template) {
        T instance;
        try {
            instance = template.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Unable to instantiate " + template.getName() + " with a zero-argument constructor.", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Not allowed to instantiate " + template.getName() + " with a zero-argument constructor.", e);
        }
        return instance;
    }

    private <T extends ClientDatabaseEntry> void setValue(Class<T> template, Field field, Object value, T instance, DbcField fieldInfo) {
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
            throw new IllegalArgumentException("Unable to set value " + value + " on " + template.getName() + "#" + field.getName(), e);
        } finally {
            field.setAccessible(accessible);
        }
    }
}
