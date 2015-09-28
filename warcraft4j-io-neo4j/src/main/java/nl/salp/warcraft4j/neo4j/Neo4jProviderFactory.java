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
package nl.salp.warcraft4j.neo4j;

import nl.salp.warcraft4j.neo4j.file.DirectoryNeo4JFileProvider;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public final class Neo4jProviderFactory {
    private Neo4jProviderFactory() {
    }

    public Neo4jProvider getNeo4jProvider(Neo4jConfig config) throws IllegalArgumentException, Neo4jException {
        if (config == null) {
            throw new IllegalArgumentException("Can't create a Neo4jProvider for a null Neo4jConfiguration.");
        }
        Neo4jProvider provider;
        if (config.isNeo4jEmbedded()) {
            provider = new EmbeddedNeo4jProvider(config, new DirectoryNeo4JFileProvider(config));
        } else {
            throw new Neo4jException("No Neo4JProvider available for an non-embedded Neo4J instance.");
        }
        return provider;
    }
}
