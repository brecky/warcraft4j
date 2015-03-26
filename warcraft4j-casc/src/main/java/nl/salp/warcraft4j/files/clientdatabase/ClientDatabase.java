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

package nl.salp.warcraft4j.files.clientdatabase;

import nl.salp.warcraft4j.files.clientdatabase.parser.ClientDatabaseEntryClasspathScanner;
import nl.salp.warcraft4j.files.clientdatabase.parser.ClientDatabaseFileParser;
import nl.salp.warcraft4j.files.clientdatabase.parser.ClientDatabaseParsingException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Client database container which holds parsed files and their relations.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabase {
    /** The mapping between the Java type and the client database entry type. */
    private Map<Class<? extends ClientDatabaseEntry>, ClientDatabaseEntryType> typeMappings;
    /** The parsed instances, indexed by their entry type. */
    private Map<ClientDatabaseEntryType, ValuesMapping<? extends ClientDatabaseEntry>> instances;

    /**
     * Create a new ClientDatabase.
     */
    public ClientDatabase() {
        this.typeMappings = new HashMap<>();
        this.instances = new HashMap<>();
    }

    /**
     * Add a collection of client database entries to the ClientDatabase.
     *
     * @param entries The entries.
     */
    public void add(Collection<? extends ClientDatabaseEntry> entries) {
        if (entries != null) {
            for (ClientDatabaseEntry entry : entries) {
                ValuesMapping mapping = getMapping(entry);
                mapping.add(entry);
            }
        }
    }

    /**
     * Add a client database entries to the ClientDatabase.
     *
     * @param entries The entries.
     */
    public void add(ClientDatabaseEntry... entries) {
        for (ClientDatabaseEntry entry : entries) {
            ValuesMapping mapping = getMapping(entry);
            mapping.add(entry);
        }
    }

    /**
     * Parse a file and add the entries to the client database.
     *
     * @param type      The type of file to parse.
     * @param directory The directory the client database file is residing in.
     *
     * @throws IOException                    When reading the file failed.
     * @throws ClientDatabaseParsingException When the file could not be parsed.
     */
    public <T extends ClientDatabaseEntry> void add(Class<T> type, String directory) throws IOException, ClientDatabaseParsingException {
        ClientDatabaseFileParser<T> parser = new ClientDatabaseFileParser<>(type);
        List<T> entries = parser.parse(directory);
        add(entries);
    }

    public void addFromClasspath(String directory) throws IOException, ClientDatabaseParsingException {
        ClientDatabaseEntryClasspathScanner scanner = new ClientDatabaseEntryClasspathScanner(this);
        for (Class<? extends ClientDatabaseEntry> c : scanner.scan()) {
            add(c, directory);
        }
    }

    /**
     * Get the entry type for the Java type.
     *
     * @param type The Java type.
     *
     * @return The entry type or {@code null} if no entry type is mapped to it.
     */
    public ClientDatabaseEntryType getEntryType(Class<? extends ClientDatabaseEntry> type) {
        ClientDatabaseEntryType entryType = null;
        if (type != null) {
            entryType = typeMappings.get(type);
        }
        return entryType;
    }

    /**
     * Get the entry types for which instances are registered.
     *
     * @return The entry types.
     */
    public Collection<ClientDatabaseEntryType> getRegisteredEntryTypes() {
        return Collections.unmodifiableCollection(instances.keySet());
    }

    /**
     * Get the Java types of which instances are registered.
     *
     * @return The Java types.
     */
    public Collection<Class<? extends ClientDatabaseEntry>> getRegisteredTypes() {
        return Collections.unmodifiableCollection(typeMappings.keySet());
    }

    /**
     * Get all the registered instances for the given type.
     *
     * @param type The type.
     * @param <T>  The type of the instance/
     *
     * @return The instances.
     */
    public <T extends ClientDatabaseEntry> Collection<T> getInstances(Class<T> type) {
        Collection<T> instances = Collections.emptySet();
        if (type != null) {
            ClientDatabaseEntryType entryType = getEntryType(type);
            if (entryType != null && this.instances.containsKey(entryType)) {
                ValuesMapping<T> mappings = getMapping(type, entryType);
                instances = mappings.getInstances();
            }
        }
        return instances;
    }

    /**
     * Get all the registered instances for the given entry type.
     *
     * @param entryType The entry type.
     *
     * @return The instances.
     */
    public Collection<? extends ClientDatabaseEntry> getInstances(ClientDatabaseEntryType entryType) {
        Collection<? extends ClientDatabaseEntry> instances = Collections.emptySet();
        if (entryType != null && this.instances.containsKey(entryType)) {
            ValuesMapping<? extends ClientDatabaseEntry> mappings = this.instances.get(entryType);
            instances = mappings.getInstances();
        }
        return instances;
    }

    /**
     * Get the instance mappings for the entry type, creating a new one if non exists.
     *
     * @param entry The entry.
     *
     * @return The mapping.
     *
     * @throws NullPointerException When a {@code null} entry is provided.
     */
    private ValuesMapping getMapping(ClientDatabaseEntry entry) {
        return getMapping(entry.getClass(), entry.getEntryType());
    }

    /**
     * Get the instance mappings for the given type and entry type, creating a new one if non exists.
     *
     * @param type      The Java type.
     * @param entryType The entry type.
     * @param <T>       The type of the entry.
     *
     * @return The mapping.
     */
    private <T extends ClientDatabaseEntry> ValuesMapping<T> getMapping(Class<T> type, ClientDatabaseEntryType entryType) {
        if (!instances.containsKey(entryType)) {
            typeMappings.put(type, entryType);
            instances.put(entryType, new ValuesMapping<>(type, entryType));
        }
        return (ValuesMapping<T>) instances.get(entryType);
    }

    /**
     * Resolve an instance based on the Java type and its id.
     *
     * @param type The Java type of the entry to resolve.
     * @param id   The id.
     * @param <T>  The type of the entry.
     *
     * @return The entry or {@code null} when no entry was found with the given id for the given type.
     */
    public <T extends ClientDatabaseEntry> T resolve(Class<T> type, int id) {
        T instance = null;
        if (type != null) {
            ClientDatabaseEntryType entryType = typeMappings.get(type);
            instance = (T) resolve(entryType, id);
        }
        return instance;
    }

    /**
     * Resolve an instance based on the entry type and its id.
     *
     * @param type The entry type of the entry to resolve.
     * @param id   The id.
     *
     * @return The entry or {@code null} when no entry was found with the given id for the given type.
     */
    public ClientDatabaseEntry resolve(ClientDatabaseEntryType type, int id) {
        ClientDatabaseEntry instance = null;
        if (type != null && instances.containsKey(type)) {
            ValuesMapping mapping = instances.get(type);
            instance = mapping.getInstance(id);
        }
        return instance;
    }

    /**
     * Mapping of entry instances.
     *
     * @param <T> The Java type of the entry instances.
     */
    private static class ValuesMapping<T extends ClientDatabaseEntry> {
        /** The entry type of the instances. */
        private ClientDatabaseEntryType entryType;
        /** The Java type of the instances. */
        private Class<T> type;
        /** The instances, indexed by their id. */
        private Map<Integer, T> instances;

        /**
         * Create a new values mapping.
         *
         * @param type      The Java type of the mapped instances.
         * @param entryType The entry type of the mapped instances.
         */
        public ValuesMapping(Class<T> type, ClientDatabaseEntryType entryType) {
            if (type == null) {
                throw new IllegalArgumentException("Unable to create a client database mapping for a null Java type.");
            }
            this.type = type;
            if (entryType == null) {
                throw new IllegalArgumentException("Unable to create a client database mapping for a null entry type.");
            }
            this.entryType = entryType;
            this.instances = new HashMap<>();
        }

        /**
         * Add instances to the mapping.
         *
         * @param instances The instances to add.
         */
        public void add(T... instances) {
            for (T instance : instances) {
                this.instances.put(instance.getId(), instance);
            }
        }

        /**
         * Add instances to the mapping.
         *
         * @param instances The instances to add.
         */
        public void add(Collection<T> instances) {
            if (instances != null) {
                for (T instance : instances) {
                    this.instances.put(instance.getId(), instance);
                }
            }
        }

        /**
         * Get the entry type that is mapped.
         *
         * @return The entry type.
         */
        public ClientDatabaseEntryType getEntryType() {
            return entryType;
        }

        /**
         * Get the Java type of the mapped entry.
         *
         * @return The Java type.
         */
        public Class<T> getType() {
            return type;
        }

        /**
         * Get the ids of all available mapped instances.
         *
         * @return The ids.
         */
        public Set<Integer> getIds() {
            return Collections.unmodifiableSet(instances.keySet());
        }

        /**
         * Get all the mapped instances.
         *
         * @return The instances.
         */
        public Collection<T> getInstances() {
            return Collections.unmodifiableCollection(instances.values());
        }

        /**
         * Check if an instance is available with the given id.
         *
         * @param id The id.
         *
         * @return {@code true} if an instance is available for the id.
         */
        public boolean isInstanceAvailable(int id) {
            return instances.containsKey(id);
        }

        /**
         * Get the instance for the given id.
         *
         * @param id The id of the instance.
         *
         * @return The instance or {@code null} if no instance is available with the given id.
         */
        public T getInstance(int id) {
            return instances.get(id);
        }
    }
}
