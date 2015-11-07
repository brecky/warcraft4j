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
package nl.salp.warcraft4j.dev.casc.dbc;

import nl.salp.warcraft4j.fileformat.dbc.DbcFile;
import nl.salp.warcraft4j.fileformat.dbc.DbcHeader;
import nl.salp.warcraft4j.fileformat.dbc.DbcStringTable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class File {
    private final DbcFile dbcFile;
    private final Map<Integer, Entry> entries;

    public File(DbcFile dbcFile) {
        this.dbcFile = dbcFile;
        entries = new HashMap<>();
    }

    public long getFilenameHash() {
        return dbcFile.getFilenameHash();
    }

    public Optional<String> getFilename() {
        return Optional.ofNullable(dbcFile.getFilename());
    }

    public DbcHeader getHeader() {
        return dbcFile.getHeader();
    }

    public Set<Integer> getIds() {
        return Collections.unmodifiableSet(getEntries().keySet());
    }

    public boolean isIdPresent(int id) {
        return getEntries().containsKey(id);
    }

    public Optional<Entry> getEntry(int id) {
        return Optional.ofNullable(getEntries().get(id));
    }

    public int getNumberOfEntries() {
        return dbcFile.getNumberOfEntries();
    }

    public Map<Integer, Entry> getEntries() {
        if (entries.isEmpty()) {
            entries.putAll(dbcFile.getEntries().stream()
                    .map(entry -> new Entry(entry, getStringTable()))
                    .collect(Collectors.toMap(entry -> entry.getId(), entry -> entry)));
        }
        return Collections.unmodifiableMap(entries);
    }

    public DbcStringTable getStringTable() {
        return dbcFile.getStringTable();
    }
}
