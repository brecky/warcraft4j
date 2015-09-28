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
package nl.salp.warcraft4j.neo4j.model;

import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.WowVersion;
import nl.salp.warcraft4j.neo4j.*;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jWowVersion extends Neo4jEntry implements WowVersion {
    public static final Label LABEL = Neo4jLabel.WOW_VERSION;
    public static final Neo4jProperty PROPERTY_VERSION = Neo4jProperty.VERSION;
    public static final Neo4jProperty PROPERTY_BRANCH = Neo4jProperty.BRANCH;
    public static final Neo4jProperty PROPERTY_REGION = Neo4jProperty.REGION;

    public Neo4jWowVersion(Node node) throws IllegalArgumentException {
        super(node, LABEL);
    }

    @Override
    public String getVersion() {
        return getString(String.valueOf(PROPERTY_VERSION))
                .orElseThrow(() -> new Neo4jException("Unable to get the World of Warcraft version."));
    }

    public void setVersion(String version) {
        setString(String.valueOf(PROPERTY_VERSION), version);
    }

    @Override
    public Branch getBranch() {
        return node()
                .filter(n -> n.hasProperty(String.valueOf(PROPERTY_BRANCH)))
                .map(n -> (String) n.getProperty(String.valueOf(PROPERTY_BRANCH)))
                .flatMap(Branch::getBranch)
                .orElseThrow(() -> new Neo4jException("Unable to get the World of Warcraft development branch."));
    }

    public void setBranch(Branch branch) {
        if (branch == null) {
            remove(String.valueOf(PROPERTY_BRANCH));
        } else {
            getNode().setProperty(String.valueOf(PROPERTY_BRANCH), branch.name());
        }
    }

    @Override
    public Region getRegion() {
        return node()
                .filter(n -> n.hasProperty(String.valueOf(PROPERTY_REGION)))
                .map(n -> (String) n.getProperty(String.valueOf(PROPERTY_REGION)))
                .flatMap(Region::getRegion)
                .orElseThrow(() -> new Neo4jException("Unable to get the World of Warcraft region."));
    }

    public void setRegion(Region region) {
        if (region == null) {
            remove(String.valueOf(PROPERTY_REGION));
        } else {
            getNode().setProperty(String.valueOf(PROPERTY_REGION), region.name());
        }
    }

    public void addVersion(Neo4jEntry entry) throws Neo4jException {
        if (entry == null || entry.getNode() == null) {
            throw new IllegalArgumentException(format("Unable to add version for a null node."));
        }
        entry.getNode().createRelationshipTo(getNode(), Neo4jRelationship.WOW_VERSION);
    }
}
