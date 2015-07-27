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
package nl.salp.warcraft4j.clientdata.dbc.mapper.mappings;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.mapper.DbcFileMapping;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document!
 *
 * @author Barre Dijkstra
 */
public class DbcFileMappingMappings implements DbcMappings {
    private final Map<String, DbcFileMapping> mappings;
    private final Map<DbcType, String> dbcTypeMappings;
    private final Map<Class<? extends DbcEntry>, String> entryTypeMappings;

    public DbcFileMappingMappings(Supplier<DbcFileMapping[]> mappingsSupplier) {
        mappings = new HashMap<>();
        dbcTypeMappings = new HashMap<>();
        entryTypeMappings = new HashMap<>();
        Stream.of(mappingsSupplier.get()).forEach(mapping -> {
            String name = mapping.getDbcFilename();
            mappings.put(name, mapping);
            dbcTypeMappings.put(mapping.getDbcType(), name);
            entryTypeMappings.put(mapping.getTargetClass(), name);
        });
    }

    @Override
    public DbcFileMapping getForDbcFile(String filename) {
        DbcFileMapping mapping = null;
        if (isNotEmpty(filename)) {
            mapping = mappings.get(filename);
        }
        return mapping;
    }

    @Override
    public DbcFileMapping getForDbcType(DbcType dbcType) {
        DbcFileMapping mapping = null;
        if (dbcType != null) {
            mapping = getForDbcFile(dbcTypeMappings.get(dbcType));
        }
        return mapping;
    }

    @Override
    public DbcFileMapping getForDbcEntryType(Class<? extends DbcEntry> entryType) {
        DbcFileMapping mapping = null;
        if (entryType != null) {
            mapping = getForDbcFile(entryTypeMappings.get(entryType));
        }
        return mapping;
    }
}
