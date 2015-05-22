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
package nl.salp.warcraft4j.dev.dbc.analysis;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.util.DbcUtil;
import nl.salp.warcraft4j.clientdata.io.RandomAccessDataReader;
import nl.salp.warcraft4j.clientdata.io.RandomAccessFileDataReader;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import static nl.salp.warcraft4j.dev.dbc.DbcUtils.getMappedFile;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class ParsedEntryStore {
    private final DevToolsConfig config;

    private final Map<DbcFile, String> files;
    private final Map<Class<? extends DbcEntry>, String> fileMappings;
    private final Map<String, Class<? extends DbcEntry>> mappingFiles;
    private final Map<String, Entries<? extends DbcEntry>> entries;

    public ParsedEntryStore(DevToolsConfig config, Supplier<RandomAccessDataReader> dataReaderSupplier) throws IOException {
        this.config = config;
        files = new HashMap<>();
        fileMappings = new HashMap<>();
        mappingFiles = new HashMap<>();
        entries = new WeakHashMap<>();
        initialise(dataReaderSupplier);
    }

    private void initialise(Supplier<RandomAccessDataReader> dataReaderSupplier) throws IOException {
        for (Class<? extends DbcEntry> mappingType : DbcUtils.getAllClientDatabaseEntryMappings()) {
            String file = getMappedFile(mappingType);
            fileMappings.put(mappingType, file);
            mappingFiles.put(file, mappingType);
        }
        for (String fileName : DbcUtils.getAllDbcFilePaths(config.getDbcDirectoryPath())) {
            DbcFile dbcFile = new DbcFile(fileName, dataReaderSupplier);
            files.put(dbcFile, fileName);
        }
    }

    public <T extends DbcEntry> Collection<T> add(Class<T> mappingType) throws IOException {
        Collection<T> instances;
        if (mappingType == null) {
            instances = Collections.emptySet();
        } else {
            Entries<T> entries = getEntriesCollection(getMappedFile(mappingType));
            instances = entries.getEntries();
        }
        return instances;
    }

    public <T extends DbcEntry> T getEntry(Class<T> mappingType, int id) throws IOException {
        T entry;
        Entries<T> entriesCollection = getEntriesCollection(getMappedFile(mappingType));
        if (entriesCollection == null) {
            entry = null;
        } else {
            entry = entriesCollection.getEntry(id);
        }
        return entry;
    }

    public <T extends DbcEntry> Collection<T> getEntries(Class<T> mappingType) throws IOException {
        Collection<T> entries;
        Entries<T> entriesCollection = getEntriesCollection(getMappedFile(mappingType));
        if (entriesCollection == null) {
            entries = Collections.emptySet();
        } else {
            entries = entriesCollection.getEntries();
        }
        return entries;
    }

    private <T extends DbcEntry> Entries<T> getEntriesCollection(String filename) throws IOException {
        Entries<T> entries = null;
        if (isNotEmpty(filename)) {
            if (this.entries.containsKey(filename)) {
                entries = (Entries<T>) this.entries.get(filename);
            } else {
                Class<T> mappingType = (Class<T>) this.mappingFiles.get(filename);
                entries = new Entries<>(mappingType, createDataReaderSupplier(filename));
                this.entries.put(filename, entries);
            }
        }
        return entries;
    }

    private Supplier<RandomAccessDataReader> createDataReaderSupplier(String filename) {
        return () -> new RandomAccessFileDataReader(new File(config.getDbcDirectoryPath(), filename));
    }

    private static class Entries<T extends DbcEntry> {
        private final DbcFile dbcFile;
        private final Class<T> type;
        private final Set<Integer> ids;
        private final Map<Integer, T> entries;
        private final Supplier<RandomAccessDataReader> dataReaderSupplier;

        public Entries(Class<T> type, Supplier<RandomAccessDataReader> dataReaderSupplier) {
            this.type = type;
            this.dataReaderSupplier = dataReaderSupplier;
            this.ids = new HashSet<>();
            this.entries = new HashMap<>();
            this.dbcFile = new DbcFile(DbcUtil.getMappedFile(type), dataReaderSupplier);
        }

        public Class<T> getType() {
            return type;
        }

        public Collection<T> getEntries() {
            return entries.values();
        }

        public Collection<Integer> getIds() {
            return entries.keySet();
        }

        public T getEntry(int id) {
            return entries.get(id);
        }

        public boolean isEntryAvailable(int id) {
            return entries.containsKey(id);
        }

        private void loadIds() {
        }

        private void loadEntry(int id) {
        }

        private void loadEntries() {
        }

        public int size() {
            return entries.size();
        }
    }


}
