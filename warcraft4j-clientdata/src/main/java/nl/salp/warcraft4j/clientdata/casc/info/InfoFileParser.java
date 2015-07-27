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
package nl.salp.warcraft4j.clientdata.casc.info;

import nl.salp.warcraft4j.clientdata.casc.CascFileParsingException;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.clientdata.io.parser.DataParser;
import nl.salp.warcraft4j.clientdata.io.parser.DataParsingException;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataType;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
abstract class InfoFileParser<T> implements DataParser<T> {

    @Override
    public final T parse(DataReader reader) throws IOException, DataParsingException {
        List<String> lines = readFile(reader);
        if (lines.size() < 2) {
            throw new CascFileParsingException(format("Only %d lines in info file, including header.", lines.size()));
        }
        String headerLine = lines.get(0);
        List<String> dataLines = lines.subList(1, lines.size());

        List<Field> fields = parseHeader(headerLine);
        dataLines.forEach(l -> fields.forEach(f -> f.withData(getData(f, l))));
        return parse(fields.stream().collect(Collectors.toMap(f -> f.getName(), f -> f)));
    }

    protected abstract T parse(Map<String, Field> fields);

    protected static List<String> getData(String field, Map<String, Field> fields) {
        return Optional.ofNullable(fields.get(field))
                .map(Field::getData)
                .orElseGet(Collections::emptyList);
    }

    protected static <T> List<T> getData(String field, Function<String, T> mapper, Map<String, Field> fields) {
        return getData(field, fields).stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    protected static Optional<String> firstEntry(String field, Map<String, Field> fields) {
        return firstEntry(getData(field, fields));
    }

    protected static <T> Optional<T> firstEntry(String field, Function<String, T> mapper, Map<String, Field> fields) {
        return firstEntry(getData(field, mapper, fields));
    }

    protected static <T> Optional<T> firstEntry(List<T> dataSet) {
        return getEntry(dataSet, 0);
    }

    protected static Optional<String> getEntry(String field, Map<String, Field> fields, int entryIndex) {
        return getEntry(getData(field, fields), entryIndex);
    }

    protected static <T> Optional<T> getEntry(String field, Function<String, T> mapper, Map<String, Field> fields, int entryIndex) {
        return getEntry(getData(field, mapper, fields), entryIndex);
    }

    protected static <T> Optional<T> getEntry(List<T> dataSet, int entryIndex) {
        T val = null;
        if (dataSet != null && entryIndex >= 0 && entryIndex < dataSet.size()) {
            val = dataSet.get(entryIndex);
        }
        return Optional.ofNullable(val);
    }

    private List<String> readFile(DataReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        DataType<String> dataType = DataTypeFactory.getStringLine();
        while (reader.hasRemaining()) {
            lines.add(reader.readNext(dataType));
        }
        return lines;
    }

    private List<Field> parseHeader(String line) {
        String[] headers = line.split("\\|");
        List<Field> parsedFields = new ArrayList<>(headers.length);
        for (int index = 0; index < headers.length; index++) {
            String header = headers[index];
            int nameTypeSplit = header.indexOf('!');
            int typeLengthSplit = header.indexOf(':');
            String name = header.substring(0, nameTypeSplit);
            InfoFieldDataType fieldType = InfoFieldDataType.valueOf(header.substring(nameTypeSplit + 1, typeLengthSplit));
            int length = Integer.parseInt(header.substring(typeLengthSplit + 1));
            parsedFields.add(new Field(index, name, fieldType, length));
        }
        return parsedFields;
    }

    private String getData(Field field, String line) {
        String[] dataSegments = line.split("\\|");
        if (field.getIndex() >= dataSegments.length) {
            throw new CascFileParsingException(format("Data for field %s not available.", field.getName()));
        }
        return dataSegments[field.getIndex()];
    }

    protected static class Field {
        public static final String EMPTY = "";
        private final String name;
        private final InfoFieldDataType fieldType;
        private final int length;
        private final int index;
        private final List<String> data;

        public Field(int index, String name, InfoFieldDataType fieldType, int length) {
            this.index = index;
            this.name = name;
            this.fieldType = fieldType;
            this.length = length;
            this.data = new ArrayList<>();
        }

        public void withData(String data) {
            if (isEmpty(data)) {
                this.data.add(EMPTY);
            } else {
                this.data.add(data);
            }
        }

        public List<String> getData() {
            return data;
        }

        public int getDataCount() {
            return data.size();
        }

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public InfoFieldDataType getFieldType() {
            return fieldType;
        }

        public int getLength() {
            return length;
        }
    }

    protected enum InfoFieldDataType {
        STRING,
        DEC,
        HEX
    }
}
