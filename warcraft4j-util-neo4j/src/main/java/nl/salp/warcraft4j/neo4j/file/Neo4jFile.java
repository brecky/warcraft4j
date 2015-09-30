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

import nl.salp.warcraft4j.neo4j.Neo4jEntry;
import nl.salp.warcraft4j.neo4j.Neo4jProperty;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Neo4jFile extends Neo4jEntry {

    public Neo4jFile(Node node, Label label) throws IllegalArgumentException {
        super(node, label);
    }

    public Optional<String> getPath() {
        return getString(String.valueOf(Neo4jProperty.FILE_PATH));
    }

    public void setPath(String path) {
        setString(String.valueOf(Neo4jProperty.FILE_PATH), path);
    }

    public Optional<Integer> getFileSize() {
        return getInt(String.valueOf(Neo4jProperty.FILE_SIZE));
    }

    public void setFileSize(int fileSize) {
        setInt(String.valueOf(Neo4jProperty.FILE_SIZE), fileSize);
    }
}
