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

import nl.salp.warcraft4j.clientdata.casc.CascParsingException;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class Config {
    private final ConfigParser configParser;
    private final Supplier<DataReader> dataReaderSupplier;
    private Map<String, List<String>> values;

    private Config(ConfigParser configParser, Supplier<DataReader> dataReaderSupplier) {
        this.configParser = configParser;
        this.dataReaderSupplier = dataReaderSupplier;
    }

    private Map<String, List<String>> values() {
        if (values == null) {
            try (DataReader reader = dataReaderSupplier.get()) {
                values = configParser.parse(reader);
            } catch (IOException e) {
                throw new CascParsingException("Error parsing configuration file", e);
            }
        }
        return values;
    }

    public Set<String> keys() {
        return values().keySet();
    }

    public int getEntryCount(String key) {
        return getValues(key)
                .map(List::size)
                .orElse(0);
    }

    public Optional<List<String>> getValues(String key) {
        return Optional.ofNullable(values())
                .map(m -> m.get(key));
        //.filter(l -> !l.isEmpty()); // TODO Leave this empty check in? Will screw up the indices of the values...
    }

    public Optional<String> getValue(String key, String idKey, String idValue) {
        return getIndex(idKey, idValue)
                .filter(i -> values().containsKey(key) && values().get(key).size() > i)
                .map(i -> values().get(key).get(i));
    }

    public Optional<String> getValue(String key, int index) {
        return Optional.ofNullable(values().get(key))
                .filter(values -> values.size() > index)
                .map(values -> values.get(index));
    }

    public Optional<Integer> getIndex(String idKey, String idValue) {
        Optional<Integer> idx = Optional.empty();
        if (isNotEmpty(idKey) && isNotEmpty(idValue)) {
            List<String> vals = values().getOrDefault(idKey, Collections.emptyList());
            for (int i = 0; i < vals.size(); i++) {
                if (idValue.equals(vals.get(i))) {
                    idx = Optional.of(i);
                    break;
                }
            }
        }
        return idx;
    }

    public Optional<String> getFirstValue(String key) {
        return getValues(key)
                .flatMap(Config::getFirstEntry);
    }

    public <T> Optional<T> getFirstValue(String key, Function<String, T> transformer) {
        return getValues(key)
                .flatMap(Config::getFirstEntry)
                .map(transformer);
    }

    public Optional<String> getLastValue(String key) {
        return getValues(key)
                .flatMap(Config::getLastEntry);
    }

    public <T> Optional<T> getLastValue(String key, Function<String, T> transformer) {
        return getValues(key)
                .flatMap(Config::getLastEntry)
                .map(transformer);
    }

    public <T> Optional<List<T>> getValues(String key, Function<String, T> transformer) {
        return getValues(key)
                .map(v -> transform(v, transformer));
    }

    private static <T> Optional<T> getFirstEntry(List<T> list) {
        return Optional.ofNullable(list)
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(0));

    }

    private static <T> Optional<T> getLastEntry(List<T> list) {
        return Optional.ofNullable(list)
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(l.size() - 1));

    }

    private static <T> List<T> transform(List<String> strings, Function<String, T> transformer) {
        return strings.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }

    public static Config keyValueConfig(Supplier<DataReader> dataReaderSupplier) {
        return new Config(new KeyValueConfigParser(), dataReaderSupplier);
    }

    public static Config tableConfig(Supplier<DataReader> dataReaderSupplier) {
        return new Config(new TableConfigParser(), dataReaderSupplier);
    }

    interface ConfigParser {
        Map<String, List<String>> parse(DataReader reader) throws IOException, CascParsingException;
    }

    private static class KeyValueConfigParser implements ConfigParser {
        @Override
        public Map<String, List<String>> parse(DataReader reader) throws IOException, CascParsingException {
            Map<String, List<String>> values = new HashMap<>();
            while (reader.hasRemaining()) {
                String line = reader.readNext(DataTypeFactory.getStringLine()).trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String[] tokens = line.split("=");
                    String key = tokens[0].trim();
                    String value = tokens[1].trim();
                    values.put(key, Arrays.asList(value.split(" ")));
                }
            }
            return values;
        }
    }

    private static class TableConfigParser implements ConfigParser {
        @Override
        public Map<String, List<String>> parse(DataReader reader) throws IOException, CascParsingException {
            Map<String, List<String>> values = new HashMap<>();
            Map<Integer, String> headerIndexes = new HashMap<>();
            boolean header = true;
            while (reader.hasRemaining()) {
                String line = reader.readNext(DataTypeFactory.getStringLine()).trim();
                if (!line.isEmpty()) {
                    int idx = 0;
                    String[] tokens = line.split("\\|");
                    for (String token : tokens) {
                        String field = Optional.ofNullable(token)
                                .filter(StringUtils::isNotEmpty)
                                .map(String::trim)
                                .orElse("");
                        if (header) {
                            String name = field.split("!")[0];
                            values.put(name, new ArrayList<>());
                            headerIndexes.put(idx, name);
                        } else {
                            values.get(headerIndexes.get(idx)).add(field);
                        }
                        idx++;
                    }
                    header = false;
                }
            }
            return values;
        }
    }
}
