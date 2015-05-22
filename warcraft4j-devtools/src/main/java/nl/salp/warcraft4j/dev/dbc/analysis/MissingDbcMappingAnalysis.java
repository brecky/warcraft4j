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
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.io.IOException;
import java.util.*;

/**
 * {@link DbcMappingAnalysis} implementation for finding DBC files that have no mapping entry type on the classpath.
 *
 * @author Barre Dijkstra
 */
class MissingDbcMappingAnalysis implements DbcMappingAnalysis<Collection<DbcFile>> {
    /** The configuration. */
    private final DevToolsConfig config;

    /**
     * Create a new analysis instance.
     *
     * @param config The configuration.
     */
    public MissingDbcMappingAnalysis(DevToolsConfig config) {
        this.config = config;
    }

    @Override
    public Collection<DbcFile> analyse() throws IOException {
        Set<String> dbcFiles = new HashSet<>(Arrays.asList(DbcUtils.getAllDbcFilePaths(config.getDbcDirectoryPath())));
        Set<String> mappedFiles = new HashSet<>();
        for (Class<? extends DbcEntry> type : DbcUtils.getAllClientDatabaseEntryMappings()) {
            DbcMapping f = type.getAnnotation(DbcMapping.class);
            if (f != null) {
                mappedFiles.add(f.file());
            }
        }
        dbcFiles.removeAll(mappedFiles);

        SortedSet<DbcFile> missingMappings = new TreeSet<>(DbcUtils.getDbcFileComparator());
        for (String mf : dbcFiles) {
            missingMappings.add(DbcUtils.getDbcFile(mf, config.getDbcDirectoryPath()));
        }

        return missingMappings;
    }
}
