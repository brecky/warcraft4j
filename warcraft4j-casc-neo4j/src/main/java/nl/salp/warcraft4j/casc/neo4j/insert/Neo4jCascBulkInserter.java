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
package nl.salp.warcraft4j.casc.neo4j.insert;

import com.google.inject.Inject;
import nl.salp.warcraft4j.WowVersion;
import nl.salp.warcraft4j.neo4j.Neo4jConfig;
import nl.salp.warcraft4j.neo4j.model.Neo4jWowVersion;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Neo4jCascBulkInserter {
    private final Path databasePath;
    private Map<Class<?>, TypeInsertionAction<?>> typeInsertionActions;
    private WowVersion version;

    @Inject
    public Neo4jCascBulkInserter(Neo4jConfig config) throws IllegalArgumentException, IOException {
        if (config == null) {
            throw new IllegalArgumentException("Can't create a Neo4jCascBulkInserter with a null Neo4jConfig");
        }
        if (config.getNeo4jDatabasePath() == null) {
            throw new IllegalArgumentException("Can't create a Neo4jCascBulkInserter with no neo4j database path configured.");
        }
        this.databasePath = config.getNeo4jDatabasePath().normalize().toAbsolutePath();
        if (Files.notExists(databasePath)) {
            Files.createDirectories(databasePath);
        } else if (!Files.isWritable(databasePath) || !Files.isReadable(databasePath) || !Files.isDirectory(databasePath)) {
            throw new IllegalArgumentException(format("Can't create a Neo4jCascBulkInserter for database path %s, either lacking read/write rights or is not a directory.", databasePath));
        }
        typeInsertionActions = new HashMap<>();
    }

    public Neo4jCascBulkInserter withVersion(WowVersion wowVersion) {
        if (wowVersion != null) {
            this.version = wowVersion;
        }
        return this;
    }


    private <T> TypeInsertionAction<T> with(Class<T> type, Label label, Collection<T> nodes, Function<T, Map<String, Object>> mapper) {
        return new TypeInsertionAction<T>(type, label, nodes, mapper);
    }

    public void insert() {
        BatchInserter inserter = null;
        try {
            inserter = BatchInserters.inserter(String.valueOf(databasePath));
            final BatchInserter i = inserter;
            typeInsertionActions.values().forEach(action -> action.insert(i));
        } finally {
            if (inserter != null) {
                inserter.shutdown();
            }
        }
    }

    public void createRelations() {
        GraphDatabaseService graphDb = null;
        try {
            graphDb = new GraphDatabaseFactory()
                    .newEmbeddedDatabaseBuilder(String.valueOf(this.databasePath))
                    .newGraphDatabase();
            try (Transaction tx = graphDb.beginTx()) {
                for (Class<?> type : typeInsertionActions.keySet()) {
                    // TODO
                }
                tx.success();
            }
        } finally {
            if (graphDb != null) {
                graphDb.shutdown();
            }

        }
    }

    private WowVersion resolveOrCreateVersion(WowVersion version, GraphDatabaseService graphDb) {
        Neo4jWowVersion ver;
        if (version == null) {
            ver = null;
        } else if (Neo4jWowVersion.class.isAssignableFrom(version.getClass())) {
            ver = (Neo4jWowVersion) version;
        } else {
            Node verNode = null;
        }
        return null;
    }

    private void addVersion(Class<?> type, GraphDatabaseService graphDb) {
        if (version != null && typeInsertionActions.containsKey(type)) {
            typeInsertionActions.get(type)
                    .getInsertedIds()
                    .forEach(nodeId -> {
                        try {
                            Node node = graphDb.getNodeById(nodeId);

                        } catch (NotFoundException e) {
                            // Ignore.
                        }
                    });
        }
    }

    private void createIndexIfNotExisting(Label label, String indexProperty, GraphDatabaseService graphDb) {
        if (isNotIndexed(label, indexProperty, graphDb)) {
            graphDb.schema().indexFor(label).on(indexProperty).create();
        }
    }

    private boolean isNotIndexed(Label label, String indexProperty, GraphDatabaseService graphDb) {
        return !isIndexed(label, indexProperty, graphDb);
    }

    private boolean isIndexed(Label label, String indexProperty, GraphDatabaseService graphDb) {
        boolean present = false;
        if (label != null && isNotEmpty(indexProperty) && graphDb != null) {
            Schema schema = graphDb.schema();
            for (IndexDefinition idx : schema.getIndexes(label)) {
                for (String prop : idx.getPropertyKeys()) {
                    if (indexProperty.equals(prop)) {
                        present = true;
                        break;
                    }
                }
                if (present) {
                    break;
                }
            }
        }
        return present;
    }

    private static class TypeInsertionAction<T> {
        private final Class<T> type;
        private final Label label;
        private final List<NodeInsertionAction<T>> nodeActions;

        public TypeInsertionAction(Class<T> type, Label label, Collection<T> nodes, Function<T, Map<String, Object>> mapper) throws IllegalArgumentException {
            this(type, label, nodes.stream()
                    .filter(n -> n != null)
                    .distinct()
                    .map(n -> new NodeInsertionAction<T>(n, mapper, label))
                    .collect(Collectors.toList()));
        }

        public TypeInsertionAction(Class<T> type, Label label, List<NodeInsertionAction<T>> nodeActions) throws IllegalArgumentException {
            this.type = Optional.ofNullable(type).orElseThrow(() -> new IllegalArgumentException("Unable to insert nodes for a null type."));
            this.label = Optional.ofNullable(label).orElseThrow(() -> new IllegalArgumentException("Unable to insert nodes with a null label."));
            this.nodeActions = Optional.ofNullable(nodeActions).orElse(new ArrayList<>());
            if (this.nodeActions.stream()
                    .anyMatch(n -> !n.getLabel().equals(label))) {
                throw new IllegalArgumentException("Unable to insert nodes with different labels.");
            }
        }

        public Class<T> getType() {
            return type;
        }

        public Label getLabel() {
            return label;
        }

        public List<T> getInstances() {
            return nodeActions.stream()
                    .filter(n -> n != null)
                    .map(NodeInsertionAction::getInstance)
                    .collect(Collectors.toList());
        }

        public List<Long> insert(BatchInserter inserter) {
            return nodeActions.stream()
                    .filter(n -> n != null)
                    .filter(n -> !n.isInserted())
                    .map(n -> n.insert(inserter))
                    .collect(Collectors.toList());
        }

        public List<Long> getInsertedIds() {
            return nodeActions.stream()
                    .filter(n -> n != null)
                    .filter(NodeInsertionAction::isInserted)
                    .map(NodeInsertionAction::getNodeId)
                    .collect(Collectors.toList());
        }

        public List<Long> getNotInsertedIds() {
            return nodeActions.stream()
                    .filter(n -> n != null)
                    .filter(n -> !n.isInserted())
                    .map(NodeInsertionAction::getNodeId)
                    .collect(Collectors.toList());
        }

        public List<T> getNotInsertedInstances() {
            return nodeActions.stream()
                    .filter(n -> n != null)
                    .filter(n -> !n.isInserted())
                    .map(NodeInsertionAction::getInstance)
                    .collect(Collectors.toList());
        }

        public List<T> getInsertedInstances() {
            return nodeActions.stream()
                    .filter(n -> n != null)
                    .filter(NodeInsertionAction::isInserted)
                    .map(NodeInsertionAction::getInstance)
                    .collect(Collectors.toList());
        }
    }

    private static class NodeInsertionAction<T> {
        private final T instance;
        private final Function<T, Map<String, Object>> mapper;
        private final Label label;
        private long nodeId;
        boolean inserted;

        public NodeInsertionAction(T instance, Function<T, Map<String, Object>> mapper, Label label) {
            this.instance = instance;
            this.mapper = mapper;
            this.label = label;
        }

        public long insert(BatchInserter batchInserter) {
            if (!inserted) {
                this.nodeId = batchInserter.createNode(mapper.apply(instance), label);
                inserted = true;
            }
            return this.nodeId;
        }

        public T getInstance() {
            return instance;
        }

        public Label getLabel() {
            return label;
        }

        public long getNodeId() {
            return nodeId;
        }

        public boolean isInserted() {
            return inserted;
        }
    }
}
