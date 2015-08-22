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
package nl.salp.warcraft4j.dev.casc.neo4j;

import nl.salp.warcraft4j.data.casc.CascContext;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.casc.CascService;
import nl.salp.warcraft4j.dev.casc.neo4j.model.CascLabel;
import nl.salp.warcraft4j.dev.casc.neo4j.model.Neo4jEncodingEntry;
import nl.salp.warcraft4j.dev.casc.neo4j.model.Neo4jIndexEntry;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Neo4jCascService implements CascService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jCascService.class);
    private final Path dataPath;
    private final GraphDatabaseService graphDb;
    private final CascContext cascContext;

    public Neo4jCascService(DevToolsConfig devToolsConfig, CascContext cascContext) throws IllegalArgumentException {
        if (devToolsConfig == null || !devToolsConfig.isNeo4jEmbedded()) {
            throw new IllegalArgumentException("Unable to create an embedded Neo4J casc service for a configuration with no embedded Neo4J configuration settings.");
        }
        if (cascContext == null) {
            throw new IllegalArgumentException("Unable to create an embedded Neo4J casc service with no casc context");
        }
        this.cascContext = cascContext;
        this.dataPath = devToolsConfig.getNeo4jDataPath();
        this.graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(String.valueOf(dataPath))
                .newGraphDatabase();
        // Even though the context is auto-closeable, register a VM shutdown hook, just in case.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
        LOGGER.debug("Initialised embedded neo4j for data directory {}", dataPath);
    }

    public Set<Neo4jEncodingEntry> findAllEncodingEntries() throws Neo4jCascException {
        return findAll(CascLabel.ENCODING_ENTRY, Neo4jEncodingEntry::new);
    }

    public Set<Neo4jIndexEntry> findAllIndexEntries() throws Neo4jCascException {
        return findAll(CascLabel.INDEX_ENTRY, Neo4jIndexEntry::new);
    }

    protected <T> Set<T> findAll(Label label, Function<Node, T> mapper) throws Neo4jCascException {
        try (Transaction tx = graphDb.beginTx()) {
            Set<T> entries = new HashSet<>();
            graphDb.findNodes(label).forEachRemaining(node -> entries.add(mapper.apply(node)));
            tx.success();
            return entries;
        }
    }

    @Override
    public void close() throws Exception {
        if (graphDb != null) {
            graphDb.shutdown();
        }
    }
}