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
package nl.salp.warcraft4j.model;

import nl.salp.warcraft4j.casc.CascService;
import nl.salp.warcraft4j.dataparser.dbc.DbcEntry;
import nl.salp.warcraft4j.dataparser.dbc.DbcFile;
import nl.salp.warcraft4j.dataparser.dbc.DbcStringTable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public abstract class DbcMapping<T> {
    private final CascService cascService;
    private transient DbcFile dbcFile;

    protected DbcMapping(CascService cascService) {
        this.cascService = cascService;
    }

    protected abstract String getFilename();

    protected abstract T parse(DbcEntry entry) throws DbcMappingException;

    protected final Optional<DbcFile> getDbcFile(String filename) throws DbcMappingException {
        return cascService.getCascFile(filename)
                .map(f -> new DbcFile(f.getFilenameHash(), () -> cascService.getDataReader(f)));
    }

    protected final DbcFile getDbcFile() throws DbcMappingException {
        if (dbcFile == null) {
            dbcFile = getDbcFile(getFilename())
                    .orElseThrow(() -> new DbcMappingException(format("DBC file %s was not present in the CASC.", getFilename())));
        }
        return dbcFile;
    }

    protected final DbcStringTable getStringTable() {
        return getDbcFile().getStringTable();
    }


    public Optional<T> parse(int id) throws DbcMappingException {
        return getDbcFile().getEntryWithId(id)
                .map(this::parse);
    }

    public List<T> parse() throws DbcMappingException {
        return getDbcFile().getEntries().stream()
                .map(this::parse)
                .filter(e -> e != null)
                .collect(Collectors.toList());
    }
}
