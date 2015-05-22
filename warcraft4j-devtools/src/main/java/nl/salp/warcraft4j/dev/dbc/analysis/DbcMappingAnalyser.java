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
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Analyser
 *
 * @author Barre Dijkstra
 */
public class DbcMappingAnalyser {
    /** The configuration. */
    private final DevToolsConfig config;

    /**
     * Create a new instance using the default configuration file.
     *
     * @throws IOException When the configuration file couldn't be read.
     */
    public DbcMappingAnalyser() throws IOException {
        this(new DevToolsConfig());
    }

    /**
     * Create a new instance.
     *
     * @param config The configuration.
     */
    public DbcMappingAnalyser(DevToolsConfig config) {
        this.config = config;
    }

    /**
     * Find all DBC mapping types on the classpath for which the referred DBC file is missing in the configured DBC directory.
     *
     * @return All mapping types where the DBC file is missing for.
     */
    public Collection<Class<? extends DbcEntry>> findRedundantDbcMappings() {
        return new RedundantDbcMappingAnalysis(config).analyse();
    }

    /**
     * Find all DBC files on the configured path that have no mapping type on the classpath.
     *
     * @return The parsed meta-data of the DBC files without a mapping type.
     */
    public Collection<DbcFile> findMissingDbcMappings() {
        return new MissingDbcMappingAnalysis(config, () -> DbcUtils.getAllClientDatabaseEntryMappings().stream(), () -> Stream.of(DbcUtils.getAllDbcFilePaths(config.getDbcDirectoryPath()))).analyse();
    }

    /**
     * Find all DBC mapping types which have fields mapped with an unknown meaning.
     *
     * @return The {@link UnknownDbcMappingResult} instances for all mapping types with unknown mappings.
     */
    public Collection<UnknownDbcMappingResult> findDbcMappingsWithUnknownFields() {
        return new UnknownDbcMappingAnalysis().analyse();
    }
}
