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
package nl.salp.warcraft4j.neo4j.file;

import nl.salp.warcraft4j.neo4j.Neo4jException;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public interface Neo4jFileProvider {
    void persist(Neo4jFile file, byte[] data) throws Neo4jException, IllegalArgumentException;

    byte[] load(Neo4jFile file) throws Neo4jException, IllegalArgumentException;

    boolean isNotExisting(Neo4jFile file) throws Neo4jException, IllegalArgumentException;

    boolean isExisting(Neo4jFile file) throws Neo4jException, IllegalArgumentException;

}
