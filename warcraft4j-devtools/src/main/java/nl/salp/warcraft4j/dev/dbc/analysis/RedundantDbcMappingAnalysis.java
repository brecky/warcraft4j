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
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link DbcMappingAnalysis} implementation for no mapping entry types on the classpath of which the referenced DBC/DB2 file is not in the configured directory.
 *
 * @author Barre Dijkstra
 */
class RedundantDbcMappingAnalysis implements DbcMappingAnalysis<Collection<Class<? extends DbcEntry>>> {
    private final DevToolsConfig config;

    public RedundantDbcMappingAnalysis(DevToolsConfig config) {
        this.config = config;
    }

    @Override
    public Collection<Class<? extends DbcEntry>> analyse() throws IOException {
        Set<Class<? extends DbcEntry>> dbcMappings = new HashSet<>(DbcUtils.getAllClientDatabaseEntryMappings());
        Set<String> dbcFiles = new HashSet<>(Arrays.asList(DbcUtils.getAllDbcFilePaths(config.getDbcDirectoryPath())));
        Set<Class<? extends DbcEntry>> mappedEntries = new HashSet<>();
        for (Class<? extends DbcEntry> type : DbcUtils.getAllClientDatabaseEntryMappings()) {
            DbcMapping f = type.getAnnotation(DbcMapping.class);
            if (f != null && dbcFiles.contains(f.file())) {
                mappedEntries.add(type);
            }
        }
        dbcMappings.removeAll(mappedEntries);

        return dbcMappings;
    }
}
