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
package nl.salp.warcraft4j.clientdata.casc.config;

import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.clientdata.io.parser.DataParser;
import nl.salp.warcraft4j.clientdata.io.parser.DataParsingException;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
abstract class ConfigFileParser<T> implements DataParser<T> {
    private static final char KEY_VALUE_SPLIT = '=';
    private static final String COMMENT = "#";

    @Override
    public final T parse(DataReader reader) throws IOException, DataParsingException {
        Map<String, String> entries = readFile(reader).stream()
                .filter(StringUtils::isNotEmpty)
                .filter(ConfigFileParser::isEntry)
                .collect(Collectors.toMap(ConfigFileParser::getConfigKey, ConfigFileParser::getConfigValue));
        return parse(entries);
    }

    protected abstract T parse(Map<String, String> fields);

    @Override
    public int getInstanceDataSize() {
        return 0;
    }

    protected static <T> List<T> toList(String value, Function<String, T> mapper) {
        return isEmpty(value) ? Collections.emptyList() : Stream.of(value.split("\\s"))
                .filter(StringUtils::isNoneEmpty)
                .map(mapper)
                .collect(Collectors.toList());
    }

    private static String getConfigKey(String line) {
        return line.substring(0, line.indexOf(KEY_VALUE_SPLIT)).trim();
    }

    private static String getConfigValue(String line) {
        return line.substring(line.indexOf(KEY_VALUE_SPLIT) + 1).trim();
    }

    private static boolean isEntry(String line) {
        return isNotEmpty(line) && !line.startsWith(COMMENT) && line.indexOf(KEY_VALUE_SPLIT) > 0 && line.indexOf(KEY_VALUE_SPLIT) < line.length();
    }

    private List<String> readFile(DataReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        DataType<String> dataType = DataTypeFactory.getStringLine();
        while (reader.hasRemaining()) {
            lines.add(reader.readNext(dataType));
        }
        return lines;
    }
}
