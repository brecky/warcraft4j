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

import java.io.IOException;
import java.util.Map;

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
     * Find all mappings that are invalid.
     *
     * @return Map containing the invalid mapping entry type with the corresponding parsed DBC file.
     *
     * @throws IOException When reading the data failed.
     */
    public Map<Class<? extends DbcEntry>, DbcFile> findInvalidMappings() throws IOException {

        return null;
    }
}
