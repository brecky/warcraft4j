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
import nl.salp.warcraft4j.data.casc.EncodingEntry;
import nl.salp.warcraft4j.data.casc.IndexEntry;
import nl.salp.warcraft4j.data.casc.RootEntry;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.casc.ListFile;
import nl.salp.warcraft4j.dev.casc.neo4j.model.*;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascLabel.*;
import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascProperty.*;
import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascRelationship.REFERENCES;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jCascBulkInserter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jCascBulkInserter.class);
    private final DevToolsConfig config;

    public Neo4jCascBulkInserter(DevToolsConfig config) {
        this.config = config;
    }

    public void insertInstallationFiles() throws IOException {
        Path dataPath = config.getWowInstallationDirectory().resolve("Data");
        LOGGER.info("Adding files from World of Waracraft installation data directory {}", dataPath);
        Set<Path> files = new HashSet<>();
        Files.walkFileTree(dataPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                LOGGER.debug("Adding directory {}", dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path p = dataPath.relativize(file);
                if (!attrs.isRegularFile() || !Files.isReadable(file) || !files.add(p)) {
                    LOGGER.warn("File {} in installation data directory {} either already exists, is not a file or is not readable.", p, dataPath);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                LOGGER.warn(format("Error adding file %s", file), exc);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    LOGGER.warn(format("Error adding directory %s", dir), exc);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        LOGGER.info("Collected {} files from {}, adding them to Neo4J", files.size(), config.getWowInstallationDirectory());
        Map<Long, Object> insertedObjects = insert(FILE, files, "path", (path) -> singletonMap("path", String.valueOf(path)));
        LOGGER.info("Inserted {} files to Neo4J", insertedObjects.size());
    }

    public void insertListFile(ListFile listFile, CascContext cascContext) {
        Map<String, Long> entries = listFile.getFilename().stream()
                .collect(Collectors.toMap((filename) -> filename, (filename) -> listFile.getHash(filename)));
        LOGGER.info("Adding {} list file entries to Neo4J", entries.size());
        Map<Long, Object> insertedObjects = insert(CASC_FILE, entries.entrySet(), FILENAME.getName(),
                (entry) -> Neo4jCascFile.toNodeProperties(entry.getKey(), entry.getValue(), null, cascContext));
        LOGGER.info("Inserted {} list files to Neo4J", insertedObjects.size());
    }

    public void insertCascContext(CascContext cascContext) {
        LOGGER.info("Adding CascContext for WoW branch {}, region {}, locale, {} and version {}",
                cascContext.getBranch(), cascContext.getVersion(), cascContext.getLocale(), cascContext.getVersion());
        insertRootEntries(cascContext);
        insertEncodingEntries(cascContext);
        insertIndexEntries(cascContext);
        LOGGER.info("Added all CascContext entries to Neo4J");
    }

    public void insertRootEntries(CascContext cascContext) {
        Collection<RootEntry> rootEntries = cascContext.getRootEntries();
        LOGGER.info("Adding {} root entries for WoW branch {}, region {}, locale, {} and version {}",
                rootEntries.size(), cascContext.getBranch(), cascContext.getVersion(), cascContext.getLocale(), cascContext.getVersion());
        Map<Long, Object> insertedObjects = insert(ROOT_ENTRY, rootEntries, FILENAME_HASH.getName(), (entry) -> Neo4jRootEntry.toNodeProperties(entry, cascContext));
        LOGGER.info("Inserted {} root entries to Neo4J", insertedObjects.size());

    }

    public void insertEncodingEntries(CascContext cascContext) {
        Collection<EncodingEntry> encodingEntries = cascContext.getEncodingEntries();
        LOGGER.info("Adding {} encoding entries for WoW branch {}, region {}, locale, {} and version {}",
                encodingEntries.size(), cascContext.getBranch(), cascContext.getVersion(), cascContext.getLocale(), cascContext.getVersion());
        Map<Long, Object> insertedObjects = insert(ENCODING_ENTRY, encodingEntries, CONTENT_CHECKSUM.getName(), (entry) -> Neo4jEncodingEntry.toNodeProperties(entry, cascContext));
        LOGGER.info("Inserted {} encoding entries to Neo4J", insertedObjects.size());
    }

    public void insertIndexEntries(CascContext cascContext) {
        Collection<IndexEntry> indexEntries = cascContext.getIndexEntries();
        LOGGER.info("Adding {} index entries for WoW branch {}, region {}, locale, {} and version {}",
                indexEntries.size(), cascContext.getBranch(), cascContext.getVersion(), cascContext.getLocale(), cascContext.getVersion());
        Map<Long, Object> insertedObjects = insert(INDEX_ENTRY, indexEntries, FILE_KEY.getName(), (entry) -> Neo4jIndexEntry.toNodeProperties(entry, cascContext));
        LOGGER.info("Inserted {} index entries to Neo4J", insertedObjects.size());
    }

    public void addReferences() {
        GraphDatabaseService graphDb = null;

        try {
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(String.valueOf(config.getNeo4jDataPath()))
                    .newGraphDatabase();
            try (Transaction tx = graphDb.beginTx()) {
                Map<String, Node> cascFileNodes = new HashMap<>();
                try (ResourceIterator<Node> iter = graphDb.findNodes(CASC_FILE)) {
                    LOGGER.debug("Retrieving CASC file entries.");
                    iter.forEachRemaining(n -> cascFileNodes.put(String.valueOf(n.getProperty("filename")), n));
                    LOGGER.debug("Retrieved {} CASC file entries.", cascFileNodes.size());
                }
                Map<Long, Node> rootNodes = new HashMap<>();
                try (ResourceIterator<Node> iter = graphDb.findNodes(ROOT_ENTRY)) {
                    LOGGER.debug("Retrieving root entries.");
                    iter.forEachRemaining(n -> rootNodes.put((Long) n.getProperty("filenameHash"), n));
                    LOGGER.debug("Retrieved {} root entries.", rootNodes.size());
                }
                Map<String, Node> encodingNodes = new HashMap<>();
                try (ResourceIterator<Node> iter = graphDb.findNodes(ENCODING_ENTRY)) {
                    LOGGER.debug("Retrieving encoding entries.");
                    iter.forEachRemaining(n -> encodingNodes.put(String.valueOf(n.getProperty("contentChecksum")), n));
                    LOGGER.debug("Retrieved {} encoding entries.", encodingNodes.size());
                }
                Map<String, Node> indexNodes = new HashMap<>();
                try (ResourceIterator<Node> iter = graphDb.findNodes(INDEX_ENTRY)) {
                    LOGGER.debug("Retrieving index entries.");
                    iter.forEachRemaining(n -> indexNodes.put(String.valueOf(n.getProperty("fileKey")), n));
                    LOGGER.debug("Retrieved {} index entries.", indexNodes.size());
                }
                LOGGER.debug("Adding references from {} CASC files to {} root entries.", cascFileNodes.size(), rootNodes.size());
                cascFileNodes.values().stream()
                        .filter(cascFile -> rootNodes.containsKey(cascFile.getProperty("filenameHash")))
                        .forEach(cascFile -> cascFile.createRelationshipTo(rootNodes.get(cascFile.getProperty("filenameHash")), REFERENCES));

                LOGGER.debug("Adding references from {} root entries to {} encoding entries.", rootNodes.size(), encodingNodes.size());
                rootNodes.values().stream()
                        .filter(rootEntry -> encodingNodes.containsKey(String.valueOf(rootEntry.getProperty("contentChecksum"))))
                        .forEach(rootEntry -> rootEntry.createRelationshipTo(encodingNodes.get(String.valueOf(rootEntry.getProperty("contentChecksum"))), REFERENCES));

                LOGGER.debug("Adding references from {} encoding entries to {} index entries.", encodingNodes.size(), indexNodes.size());
                encodingNodes.values().stream()
                        .forEach(encodingEntry ->
                                        Stream.of((String[]) encodingEntry.getProperty("fileKeys"))
                                                .map(encodingKey -> encodingKey.substring(0, 18))
                                                .filter(encodingKey -> indexNodes.containsKey(encodingKey))
                                                .forEach(encodingKey -> encodingEntry.createRelationshipTo(indexNodes.get(encodingEntry), REFERENCES))
                        );
                tx.success();
                LOGGER.debug("Done adding references.");
            }
        } finally {
            if (graphDb != null) {
                graphDb.shutdown();
            }
        }
    }

    public void addIndexEntryReferences() {
        GraphDatabaseService graphDb = null;

        try {
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(String.valueOf(config.getNeo4jDataPath()))
                    .newGraphDatabase();
            try (Transaction tx = graphDb.beginTx()) {
                Map<String, Node> encodingNodes = new HashMap<>();
                try (ResourceIterator<Node> iter = graphDb.findNodes(ENCODING_ENTRY)) {
                    LOGGER.debug("Retrieving encoding entries.");
                    iter.forEachRemaining(n -> encodingNodes.put(String.valueOf(n.getProperty("contentChecksum")), n));
                    LOGGER.debug("Retrieved {} encoding entries.", encodingNodes.size());
                }
                Map<String, Node> indexNodes = new HashMap<>();
                try (ResourceIterator<Node> iter = graphDb.findNodes(INDEX_ENTRY)) {
                    LOGGER.debug("Retrieving index entries.");
                    iter.forEachRemaining(n -> indexNodes.put(String.valueOf(n.getProperty("fileKey")), n));
                    LOGGER.debug("Retrieved {} index entries.", indexNodes.size());
                }
                LOGGER.debug("Adding references from {} encoding entries to {} index entries.", encodingNodes.size(), indexNodes.size());
                encodingNodes.values().stream()
                        .forEach(encodingEntry ->
                                        Stream.of((String[]) encodingEntry.getProperty("fileKeys"))
                                                .map(encodingKey -> encodingKey.substring(0, 18))
                                                .filter(encodingKey -> indexNodes.containsKey(encodingKey))
                                                .forEach(encodingKey -> encodingEntry.createRelationshipTo(indexNodes.get(encodingKey), REFERENCES))
                        );
                tx.success();
                LOGGER.debug("Done adding references.");
            }
        } finally {
            if (graphDb != null) {
                graphDb.shutdown();
            }
        }
    }

    private <T> Map<Long, Object> insert(CascLabel label, Collection<T> entries, String indexProperty, Function<T, Map<String, Object>> mapper) {
        Map<Long, Object> nodes = new HashMap<>(entries.size());
        BatchInserter inserter = null;
        try {
            inserter = BatchInserters.inserter(config.getNeo4jDataPath().normalize().toAbsolutePath().toString());
            final Label l = label;
            final BatchInserter i = inserter;
            inserter.createDeferredSchemaIndex(l).on(indexProperty).create();

            entries.stream()
                    .filter(e -> e != null)
                    .map(mapper)
                    .forEach(p -> nodes.put(i.createNode(p, l), p.get(indexProperty)));
        } finally {
            if (inserter != null) {
                inserter.shutdown();
            }
        }
        return nodes;
    }
}
