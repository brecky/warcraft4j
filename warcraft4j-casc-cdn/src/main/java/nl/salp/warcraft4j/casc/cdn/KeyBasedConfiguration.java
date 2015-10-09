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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.io.DataReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Parsed key based configuration from a file.
 *
 * @author Barre Dijkstra
 */
public class KeyBasedConfiguration {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCdnCascConfig.class);
    /** The configuration parser to use. */
    private final ConfigParser configParser;
    /** The supplier for the {@link DataReader} to use to read the configuration file. */
    private final Supplier<DataReader> dataReaderSupplier;
    /** The parsed values. */
    private Map<String, List<String>> values;

    /**
     * Create a new instance.
     *
     * @param configParser       The parser for the configuration file.
     * @param dataReaderSupplier The supplier for the {@link DataReader} to use to read the configuration file.
     *
     * @throws IllegalArgumentException When an invalid parser or data reader have been provided.
     */
    public KeyBasedConfiguration(ConfigParser configParser, Supplier<DataReader> dataReaderSupplier) {
        this.configParser = Optional.ofNullable(configParser)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a configuration instance with a null parser."));
        this.dataReaderSupplier = Optional.ofNullable(dataReaderSupplier)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a configuration instance with a null DataReader supplier."));
    }

    /**
     * Safe get the first entry from a list, allowing for empty and {@code null} lists.
     *
     * @param list The list to get the entry from.
     * @param <T>  The type of the entry.
     *
     * @return Optional containing the first entry of the list if available.
     */
    private static <T> Optional<T> getFirstEntry(List<T> list) {
        return Optional.ofNullable(list)
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(0));
    }

    /**
     * Safe get the last entry from a list, allowing for empty and {@code null} lists.
     *
     * @param list The list to get the entry from.
     * @param <T>  The type of the entry.
     *
     * @return Optional containing the last entry of the list if available.
     */
    private static <T> Optional<T> getLastEntry(List<T> list) {
        return Optional.ofNullable(list)
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(l.size() - 1));

    }

    /**
     * Transform a list of String to another type.
     *
     * @param strings     The list of strings.
     * @param transformer The function to transform the string with.
     * @param <T>         The type to transform the strings to.
     *
     * @return The list of the transformed strings.
     */
    private static <T> List<T> transform(List<String> strings, Function<String, T> transformer) {
        return strings.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }

    /**
     * Get a configuration instance based on key/(multi-) value pairs.
     *
     * @param dataReaderSupplier The supplier for the {@link DataReader} to use to read the configuration file.
     *
     * @return The configuration instance.
     *
     * @throws IllegalArgumentException When no datareader supplier was provided.
     */
    public static KeyBasedConfiguration keyValueConfig(Supplier<DataReader> dataReaderSupplier) throws IllegalArgumentException {
        return new KeyBasedConfiguration(new KeyValueConfigParser(), dataReaderSupplier);
    }

    /**
     * Get a configuration instance based on a table with header.
     *
     * @param dataReaderSupplier The supplier for the {@link DataReader} to use to read the configuration file.
     *
     * @return The configuration instance.
     *
     * @throws IllegalArgumentException When no datareader supplier was provided.
     */
    public static KeyBasedConfiguration tableConfig(Supplier<DataReader> dataReaderSupplier) throws IllegalArgumentException {
        return new KeyBasedConfiguration(new TableConfigParser(), dataReaderSupplier);
    }

    /**
     * Parse all values from the configuration, index by their key.
     *
     * @return The values, index by their keys.
     *
     * @throws CascParsingException When parsing of the configuration file failed.
     */
    private Map<String, List<String>> values() throws CascParsingException {
        if (values == null) {
            try (DataReader reader = dataReaderSupplier.get()) {
                values = configParser.parse(reader);
            } catch (IOException e) {
                throw new CascParsingException("Error parsing configuration file", e);
            }
        }
        return values;
    }

    /**
     * Get the keys of the configuration values.
     *
     * @return The keys.
     */
    public Set<String> keys() {
        return Collections.unmodifiableSet(values().keySet());
    }

    /**
     * Get the number of values available for a key.
     *
     * @param key The key.
     *
     * @return The number of values for the key.
     */
    public int getEntryCount(String key) {
        return getValues(key)
                .map(List::size)
                .orElse(0);
    }

    /**
     * Get the values for a key.
     *
     * @param key The key.
     *
     * @return Optional with the values for the key if the key is present in the configuration.
     */
    public Optional<List<String>> getValues(String key) {
        return Optional.ofNullable(values())
                .map(m -> m.get(key));
    }

    /**
     * Get the value for a key linked to a value of an index column.
     *
     * @param key     The key to get the value for.
     * @param idKey   The key of the value to use as index column.
     * @param idValue The value of the index.
     *
     * @return Optional with the value of the key if available.
     */
    public Optional<String> getValue(String key, String idKey, String idValue) {
        return getIndex(idKey, idValue)
                .filter(i -> values().containsKey(key) && values().get(key).size() > i)
                .map(i -> values().get(key).get(i));
    }

    /**
     * Get a single value of a key.
     *
     * @param key   The key.
     * @param index The index of the value.
     *
     * @return Optional with the value if available.
     */
    public Optional<String> getValue(String key, int index) {
        return Optional.ofNullable(values().get(key))
                .filter(values -> values.size() > index)
                .map(values -> values.get(index));
    }

    /**
     * Get the index of a value for a key.
     *
     * @param idKey   The key of the column.
     * @param idValue The value to look for.
     *
     * @return Optional with the index if the value is available for the key.
     */
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

    /**
     * Get the first value of a key.
     *
     * @param key The key.
     *
     * @return Optional with the first value if the key is present and has at least 1 value.
     */
    public Optional<String> getFirstValue(String key) {
        return getValues(key)
                .flatMap(KeyBasedConfiguration::getFirstEntry);
    }

    /**
     * Get the first value of a key, transformed to a different type.
     *
     * @param key         The key.
     * @param transformer The method to transform the value to if present.
     * @param <T>         The type of the value to be transformed to.
     *
     * @return Optional of the transformed value if for the key.
     */
    public <T> Optional<T> getFirstValue(String key, Function<String, T> transformer) {
        return getValues(key)
                .flatMap(KeyBasedConfiguration::getFirstEntry)
                .map(transformer);
    }

    /**
     * Get the last value for a key.
     *
     * @param key The key.
     *
     * @return Optional with the last value if the key is present and has at least 1 value.
     */
    public Optional<String> getLastValue(String key) {
        return getValues(key)
                .flatMap(KeyBasedConfiguration::getLastEntry);
    }

    /**
     * Get the last value of a key, transformed to a different type.
     *
     * @param key         The key.
     * @param transformer The method to transform the value to if present.
     * @param <T>         The type of the value to be transformed to.
     *
     * @return Optional of the transformed value if for the key.
     */
    public <T> Optional<T> getLastValue(String key, Function<String, T> transformer) {
        return getValues(key)
                .flatMap(KeyBasedConfiguration::getLastEntry)
                .map(transformer);
    }

    /**
     * Get all values for a key, transformed to a different type.
     *
     * @param key         The key to get the values for.
     * @param transformer The method to transform the values to if present.
     * @param <T>         The type of the values to be transformed to.
     *
     * @return Optional of the transformed values if the key is present.
     */
    public <T> Optional<List<T>> getValues(String key, Function<String, T> transformer) {
        return getValues(key)
                .map(v -> transform(v, transformer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Parser for parsing configuration data.
     */
    interface ConfigParser {
        /**
         * Parse data into a list of values index by their key.
         *
         * @param reader The data reader to read the configuration data from.
         *
         * @return The list of values index by their key
         *
         * @throws IOException          When reading the data failed.
         * @throws CascParsingException When the data could not be parsed.
         */
        Map<String, List<String>> parse(DataReader reader) throws IOException, CascParsingException;
    }

    /**
     * {@link ConfigParser} for parsing key-value based data with support for space seperated multi-values.
     */
    static class KeyValueConfigParser implements ConfigParser {
        /**
         * {@inheritDoc}
         */
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
                    LOGGER.trace("Parsed line {} to [key: {}, values: {}]", line, key, values.get(key));
                }
            }
            return values;
        }
    }

    /**
     * {@link ConfigParser} for parsing table based data with support for space seperated multi-values.
     */
    static class TableConfigParser implements ConfigParser {
        /**
         * {@inheritDoc}
         */
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
