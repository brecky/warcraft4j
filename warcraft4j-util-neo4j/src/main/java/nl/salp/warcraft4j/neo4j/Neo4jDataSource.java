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

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Optional;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Neo4jDataSource extends Neo4jEntry {
    private static final Label LABEL = Neo4jLabel.DATA_SOURCE;

    public Neo4jDataSource(Node node) throws IllegalArgumentException {
        super(node, LABEL);
    }

    public String getName() throws Neo4jException {
        return getString(String.valueOf(Neo4jProperty.NAME))
                .orElseThrow(() -> new Neo4jException("No name present for data source."));
    }

    public void setName(String name) throws Neo4jException {
        if (isEmpty(name)) {
            throw new Neo4jException("Can't set an empty name name for a data source.");
        }
        setString(String.valueOf(Neo4jProperty.NAME), name);
    }

    public Optional<Boolean> isOnline() {
        return getBoolean(String.valueOf(Neo4jProperty.ONLINE));
    }

    public void setOnline(boolean online) {
        setBoolean(String.valueOf(Neo4jProperty.ONLINE), online);
    }

    public void addSource(Neo4jEntry entry) throws Neo4jException {
        if (entry == null || entry.getNode() == null) {
            throw new IllegalArgumentException(format("Unable to add %s as data source for a null node.", getName()));
        }
        entry.getNode().createRelationshipTo(getNode(), Neo4jRelationship.SOURCE);
    }
}
