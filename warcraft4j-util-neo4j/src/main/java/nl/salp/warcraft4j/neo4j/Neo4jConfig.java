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

import java.nio.file.Path;

/**
 * Warcraft4J Neo4J configuration.
 *
 * @author Barre Dijkstra
 */
public interface Neo4jConfig {
    /**
     * Get the path to the directory to store files.
     *
     * @return The path directory to store files.
     */
    Path getNeo4jFileStoragePath();

    /**
     * Check if Neo4J should use an embedded instance and is mutually exclusive with {@link #isNeo4jExternal()}.
     *
     * @return {@code true} if Neo4J should run on an embedded instance.
     */
    boolean isNeo4jEmbedded();

    /**
     * Check if Neo4J should use an external instance and is mutually exclusive with {@link #isNeo4jEmbedded()}.
     *
     * @return {@code true} if Neo4J should run against an external instance.
     */
    boolean isNeo4jExternal();

    /**
     * Get the path to the Neo4J database files for running an embedded Neo4J server.
     *
     * @return The path to the database files or {@code null} for a non-embedded Neo4J server.
     *
     * @see #isNeo4jEmbedded()
     */
    Path getNeo4jDatabasePath();

    /**
     * Get the URI to the external Neo4J server to use.
     *
     * @return The URI or {@code null} when not running against an external server.
     *
     * @see #isNeo4jExternal()
     */
    String getNeo4jExternalUri();

    /**
     * Get the username of the user to use to connect to an external Neo4J server.
     *
     * @return The username or {@code null} when not running against an external server.
     *
     * @see #isNeo4jExternal()
     */
    String getNeo4jExternalUser();

    /**
     * Get the password of the user to use to connect to an external Neo4J server.
     *
     * @return The password or {@code null} when not running against an external server.
     *
     * @see #isNeo4jExternal()
     */
    String getNeo4jExternalPassword();
}