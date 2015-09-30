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
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.TraversalDescription;

import java.util.Map;
import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public interface Neo4jProvider extends AutoCloseable {
    /**
     * Start a new Neo4J transaction.
     *
     * @return The started transaction.
     */
    Transaction beginTx();

    /**
     * Get the index manager for the Neo4J instance.
     *
     * @return The index manager.
     */
    IndexManager getIndex();

    /**
     * Get the schema for the Neo4J instance.
     *
     * @return The schema.
     */
    Schema getSchema();

    /**
     * Create a new node.
     *
     * @param labels The label(s) for the node.
     *
     * @return The created node.
     */
    Node createNode(Label... labels);

    /**
     * Execute a Neo4J Cypher query.
     *
     * @param query The query.
     *
     * @return The result object.
     */
    Result execute(String query);

    /**
     * Execute a Neo4J Cypher query.
     *
     * @param query      The query.
     * @param parameters The query parameters.
     *
     * @return The result object.
     */
    Result execute(String query, Map<String, Object> parameters);

    /**
     * Find a single node.
     *
     * @param label The label of the node.
     * @param key   The property to filter on.
     * @param value The value of the property to filter on.
     *
     * @return Optional of the node.
     *
     * @throws MultipleFoundException When more then one node match the label, key and value combination.
     */
    Optional<Node> findNode(Label label, String key, Object value) throws MultipleFoundException;

    /**
     * Find all nodes for a label.
     *
     * @param label The label of the nodes.
     *
     * @return The nodes for the label.
     */
    ResourceIterator<Node> findNodes(Label label);

    /**
     * Find all nodes for a label with a specific property value.
     *
     * @param label The label of the nodes.
     * @param key   The property.
     * @param value The value of the property to filter on.
     *
     * @return The nodes for the label.
     */
    ResourceIterator<Node> findNodes(Label label, String key, Object value);

    /**
     * Get the traversal description for the Neo4J instance.
     *
     * @return The traversal description.
     */
    TraversalDescription getTraversal();

    /**
     * Get the bi-directional traversal description for the Neo4J instance.
     *
     * @return The traversal description.
     */
    BidirectionalTraversalDescription getBidirectionalTraversal();

    /**
     * Get the contents associated with a {@link Neo4jFile} instance.
     *
     * @param file The file to get the contents for.
     *
     * @return The file contents.
     *
     * @throws Neo4jException When reading the file contents failed.
     */
    byte[] getFileContents(Neo4jFile file) throws Neo4jException;

    /**
     * Set the file contents for a {@link Neo4jFile} instance.
     *
     * @param file The file to set the data for.
     * @param data The file contents.
     *
     * @throws Neo4jException When writing the file contents failed.
     */
    void persistFile(Neo4jFile file, byte[] data) throws Neo4jException;

    /**
     * Check if the contents for a {@link Neo4jFile} has been persisted.
     *
     * @param file The file to check.
     *
     * @return {@code true} if the file contents has been persisted.
     *
     * @throws Neo4jException When checking the file failed.
     */
    boolean isFilePersisted(Neo4jFile file) throws Neo4jException;

    /**
     * Check if the contents for a {@link Neo4jFile} has not been persisted.
     *
     * @param file The file to check.
     *
     * @return {@code true} if the file contents has not been persisted.
     *
     * @throws Neo4jException When checking the file failed.
     */
    boolean isFileNotPersisted(Neo4jFile file) throws Neo4jException;

    @Override
    void close() throws Exception;
}
