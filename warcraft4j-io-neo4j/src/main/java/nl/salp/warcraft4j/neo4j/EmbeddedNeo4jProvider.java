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

import nl.salp.warcraft4j.neo4j.file.Neo4jFile;
import nl.salp.warcraft4j.neo4j.file.Neo4jFileProvider;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

/**
 * {@link Neo4jProvider} implementation for an embedded Neo4J instance.
 *
 * @author Barre Dijkstra
 */
public class EmbeddedNeo4jProvider implements Neo4jProvider {
    /** Time-out for waiting for the database to become available. */
    private static final long START_TIMEOUT = 10000L;
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedNeo4jProvider.class);
    /** The path to the database files to use. */
    private final Path dbPath;
    /** The Neo4J server GraphDatabaseService instance. */
    private final GraphDatabaseService graphDb;
    /** The {@link Neo4jFileProvider} for managing file data. */
    private final Neo4jFileProvider fileProvider;

    /**
     * Create a new embedded neo4j provider instance from a {@link Neo4jConfig}.
     *
     * @param config       The configuration.
     * @param fileProvider The {@link Neo4jFileProvider} for managing the file data.
     *
     * @throws IllegalArgumentException When the instance could not be created.
     */
    public EmbeddedNeo4jProvider(Neo4jConfig config, Neo4jFileProvider fileProvider) throws IllegalArgumentException {
        this(parseDbPath(config), fileProvider);
    }

    /**
     * Create a new embedded neo4j provider instance for a database path.
     *
     * @param dbPath       The database path.
     * @param fileProvider The {@link Neo4jFileProvider} for managing the file data.
     *
     * @throws IllegalArgumentException When the instance could not be created.
     */
    public EmbeddedNeo4jProvider(Path dbPath, Neo4jFileProvider fileProvider) throws IllegalArgumentException {
        if (dbPath == null) {
            throw new IllegalArgumentException("Unable to create an embedded Neo4J provider for a null database directory.");
        } else if (Files.notExists(dbPath)) {
            try {
                LOGGER.debug("Creating non-existing database directory {} for embedded Neo4J.", dbPath);
                Files.createDirectories(dbPath);
            } catch (IOException e) {
                throw new IllegalArgumentException(format("Unable to create embedded Neo4J database directory %s", dbPath), e);
            }
        } else if (Files.isRegularFile(dbPath) || !Files.isReadable(dbPath) || !Files.isWritable(dbPath)) {
            throw new IllegalArgumentException(format("Embedded Neo4J database directory %s either is not a directory or inaccessible.", dbPath));
        }
        if (fileProvider == null) {
            throw new IllegalArgumentException("Unable to create an embedded Neo4J provider with a null Neo4jFileProvider.");
        }
        this.fileProvider = fileProvider;
        this.dbPath = dbPath.normalize().toAbsolutePath();
        this.graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(String.valueOf(dbPath))
                .newGraphDatabase();
        // Even though the context is auto-closeable, register a VM shutdown hook, just in case.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
        if (graphDb.isAvailable(START_TIMEOUT)) {
            LOGGER.debug("Initialised embedded Neo4J provider for data directory {}", this.dbPath);
        } else {
            throw new IllegalArgumentException(format("Failed initialising embedded Neo4J for directory %s in %d ms.", this.dbPath, START_TIMEOUT));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction beginTx() {
        return graphDb.beginTx();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IndexManager getIndex() {
        return graphDb.index();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return graphDb.schema();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node createNode(Label... labels) {
        return graphDb.createNode(labels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result execute(String query) {
        return graphDb.execute(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result execute(String query, Map<String, Object> parameters) {
        return graphDb.execute(query, parameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Node> findNode(Label label, String key, Object value) throws MultipleFoundException {
        return Optional.ofNullable(graphDb.findNode(label, key, value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceIterator<Node> findNodes(Label label) {
        return graphDb.findNodes(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceIterator<Node> findNodes(Label label, String key, Object value) {
        return graphDb.findNodes(label, key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TraversalDescription getTraversal() {
        return graphDb.traversalDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BidirectionalTraversalDescription getBidirectionalTraversal() {
        return graphDb.bidirectionalTraversalDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getFileContents(Neo4jFile file) throws Neo4jException {
        return fileProvider.load(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void persistFile(Neo4jFile file, byte[] data) throws Neo4jException {
        fileProvider.persist(file, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFilePersisted(Neo4jFile file) throws Neo4jException {
        return fileProvider.isExisting(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFileNotPersisted(Neo4jFile file) throws Neo4jException {
        return fileProvider.isNotExisting(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void close() throws Exception {
        if (graphDb != null) {
            graphDb.shutdown();
        }
    }

    /**
     * Validate the configuration and get the database path from it.
     *
     * @param config The configuration.
     *
     * @return The database path.
     *
     * @throws IllegalArgumentException When the configuration is {@code null} or not configured for embedded Neo4J usage.
     */
    private static Path parseDbPath(Neo4jConfig config) throws IllegalArgumentException {
        if (config == null || !config.isNeo4jEmbedded() || config.getNeo4jDatabasePath() == null) {
            throw new IllegalArgumentException("Unable to create an embedded Neo4J provider for a configuration with no embedded Neo4J configuration settings.");
        }
        return config.getNeo4jDatabasePath();
    }
}