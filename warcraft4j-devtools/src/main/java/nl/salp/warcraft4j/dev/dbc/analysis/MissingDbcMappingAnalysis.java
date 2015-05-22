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
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcMapping;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link DbcMappingAnalysis} implementation for finding DBC files that have no mapping entry type on the classpath.
 *
 * @author Barre Dijkstra
 */
class MissingDbcMappingAnalysis implements DbcMappingAnalysis<Collection<DbcFile>> {
    /** The configuration. */
    private final DevToolsConfig config;
    private final Supplier<Stream<Class<? extends DbcEntry>>> mappingTypeSupplier;
    private final Supplier<Stream<String>> dbcFilesSupplier;

    /**
     * Create a new analysis instance.
     *
     * @param config              The configuration.
     * @param mappingTypeSupplier Supplier for a stream of the mapping types.
     * @param dbcFilesSupplier    Supplier for a stream of the DBC files.
     */
    public MissingDbcMappingAnalysis(DevToolsConfig config, Supplier<Stream<Class<? extends DbcEntry>>> mappingTypeSupplier, Supplier<Stream<String>> dbcFilesSupplier) {
        this.config = config;
        this.mappingTypeSupplier = mappingTypeSupplier;
        this.dbcFilesSupplier = dbcFilesSupplier;
    }

    @Override
    public Collection<DbcFile> analyse() {
        Set<String> mappedFile = mappingTypeSupplier.get().parallel()
                .filter(type -> type.isAnnotationPresent(DbcMapping.class))
                .map(type -> type.getAnnotation(DbcMapping.class).file()).collect(Collectors.toSet());
        return dbcFilesSupplier.get().parallel()
                .filter(filename -> !mappedFile.contains(filename))
                .map(filename -> DbcUtils.getDbcFile(filename, config.getDbcDirectoryPath())).collect(Collectors.toSet());
    }
}
