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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabaseFileParser<T extends ClientDatabaseEntry> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDatabaseFileParser.class);

    private final Class<T> template;
    private final DbcFile dbcFile;
    private final SortedMap<Integer, Field> fields;

    public ClientDatabaseFileParser(Class<T> template) {
        if (template == null) {
            throw new IllegalArgumentException("Unable to parse a client database file from a null template class.");
        }
        this.template = template;
        dbcFile = template.getAnnotation(DbcFile.class);
        if (dbcFile == null) {
            throw new IllegalArgumentException(format("Unable to parse the template class %s with no DbcFile annotation.", template.getName()));
        }
        this.fields = parseFields(template);
    }

    private SortedMap<Integer, Field> parseFields(Class<T> template) {
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


    public List<T> parse(String basePath) throws IOException, IllegalArgumentException {
        LOGGER.debug(format("[parse::%s] Parsing %s from %s in %s", template.getSimpleName(), template.getName(), dbcFile.file(), basePath));
        File file = new File(basePath, dbcFile.file());
        try (DataReader reader = new FileDataReader(file)) {
            return parse(reader);
        }
    }

    private List<T> parse(DataReader reader) throws IOException {
        ClientDatabaseHeader header = new ClientDatabaseHeaderParser().parse(reader);
        LOGGER.debug(format("[parse::%s] Parsing type %s with %d records with %d fields per record. StringBlock present: %s", template.getSimpleName(), header.getMagicString(), header.getRecordCount(), header.getFieldCount(), (header.getRecordBlockSize() > 0)));
        byte[] entryData = reader.readNextBytes(header.getRecordBlockSize());
        byte[] stringBlockData = reader.readNextBytes(header.getStringBlockSize());
        ClientDatabaseStringBlock stringBlock = new ClientDatabaseStringBlockParser().parse(stringBlockData);
        return parseEntries(entryData, header, stringBlock);
    }

    private List<T> parseEntries(byte[] data, ClientDatabaseHeader header, ClientDatabaseStringBlock stringBlock) throws IOException {
        List<T> entries = new ArrayList<>(header.getRecordCount());

        DataReader reader = new ByteArrayDataReader(data);

        for (int i = 0; i < header.getRecordCount(); i++) {
            T instance = instantiate();
            populate(instance, reader, stringBlock);
            entries.add(instance);
        }
        return entries;
    }

    private void populate(T instance, DataReader reader, ClientDatabaseStringBlock stringBlock) throws IOException {
        for (int fieldIndex : fields.keySet()) {
            Field field = fields.get(fieldIndex);
            DbcField fieldInfo = field.getAnnotation(DbcField.class);
            DataType<?> dataType = fieldInfo.dataType().getDataType(fieldInfo);
            Object value = reader.readNext(dataType);

            if (DbcDataType.STRINGTABLE_REFERENCE == fieldInfo.dataType()) {
                String stringValue;
                if (stringBlock.isEntryAvailableForPosition((int) value)) {
                    stringValue = stringBlock.getEntry((int) value);
                } else if ((int) value > 0) {
                    stringValue = String.valueOf(value);
                } else {
                    stringValue = null;
                }
                setValue(field, stringValue, instance);
            } else {
                setValue(field, value, instance);
            }

        }
    }

    private T instantiate() {
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
            throw new IllegalArgumentException("Unable to set value " + value + " on " + template.getName() + "#" + field.getName(), e);
        } finally {
            field.setAccessible(accessible);
        }
    }
}
