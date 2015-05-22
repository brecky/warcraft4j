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
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcMapping;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Collection<Class<? extends DbcEntry>> analyse() {
        Set<String> files = Stream.of(DbcUtils.getAllDbcFilePaths(config.getDbcDirectoryPath())).distinct().collect(Collectors.toSet());
        return DbcUtils.getAllClientDatabaseEntryMappings().parallelStream()
                .filter(type -> type.isAnnotationPresent(DbcMapping.class))
                .filter(type -> !files.contains(type.getAnnotation(DbcMapping.class).file()))
                .collect(Collectors.toSet());
    }
}
