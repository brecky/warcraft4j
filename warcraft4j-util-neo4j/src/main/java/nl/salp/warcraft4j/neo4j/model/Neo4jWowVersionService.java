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

import com.google.inject.Inject;
import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.neo4j.Neo4jProvider;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.*;

import static java.lang.String.format;
import static nl.salp.warcraft4j.neo4j.model.Neo4jWowVersion.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Neo4jWowVersionService {
    private static final String NODE_COLUMN = "n";
    private final Neo4jProvider neo4jProvider;

    @Inject
    public Neo4jWowVersionService(Neo4jProvider neo4jProvider) {
        this.neo4jProvider = neo4jProvider;
    }

    public Optional<Neo4jWowVersion> findVersion(Branch branch, Region region, String version) {
        try (Transaction tx = neo4jProvider.beginTx();
             Result result = query()
                     .withBranch(branch)
                     .withRegion(region)
                     .withVersion(version)
                     .execute(neo4jProvider);
             ResourceIterator<Node> nodes = result.columnAs(NODE_COLUMN)) {
            Optional<Neo4jWowVersion> ver;
            if (nodes.hasNext()) {
                ver = Optional.ofNullable(nodes.next())
                        .map(Neo4jWowVersion::new);
                if (nodes.hasNext()) {
                    throw new IllegalArgumentException("Found more then one node for branch %s, region %s and version %s");
                }
            } else {
                ver = Optional.empty();
            }
            return ver;
        }
    }

    public Set<Neo4jWowVersion> findAll() {
        try (Transaction tx = neo4jProvider.beginTx();
             Result result = query().execute(neo4jProvider);
             ResourceIterator<Node> nodes = result.columnAs(NODE_COLUMN)) {
            final Set<Neo4jWowVersion> versions = new HashSet<>();
            nodes.forEachRemaining(node -> Optional.ofNullable(node)
                    .filter(n -> n != null)
                    .map(Neo4jWowVersion::new)
                    .ifPresent(versions::add));
            return versions;
        }
    }

    public Set<Neo4jWowVersion> findForBranchInRegion(Branch branch, Region region) {
        try (Transaction tx = neo4jProvider.beginTx();
             Result result = query()
                     .withBranch(branch)
                     .withRegion(region)
                     .execute(neo4jProvider);
             ResourceIterator<Node> nodes = result.columnAs(NODE_COLUMN)) {
            final Set<Neo4jWowVersion> versions = new HashSet<>();
            nodes.forEachRemaining(node -> Optional.ofNullable(node)
                    .filter(n -> n != null)
                    .map(Neo4jWowVersion::new)
                    .ifPresent(versions::add));
            return versions;
        }
    }

    public Set<Neo4jWowVersion> findForBranch(Branch branch) {
        try (Transaction tx = neo4jProvider.beginTx();
             Result result = query()
                     .withBranch(branch)
                     .execute(neo4jProvider);
             ResourceIterator<Node> nodes = result.columnAs(NODE_COLUMN)) {
            final Set<Neo4jWowVersion> versions = new HashSet<>();
            nodes.forEachRemaining(node -> Optional.ofNullable(node)
                    .filter(n -> n != null)
                    .map(Neo4jWowVersion::new)
                    .ifPresent(versions::add));
            return versions;
        }
    }

    public Set<Neo4jWowVersion> findForRegion(Branch branch, Region region) {
        try (Transaction tx = neo4jProvider.beginTx();
             Result result = query()
                     .withRegion(region)
                     .execute(neo4jProvider);
             ResourceIterator<Node> nodes = result.columnAs(NODE_COLUMN)) {
            final Set<Neo4jWowVersion> versions = new HashSet<>();
            nodes.forEachRemaining(node -> Optional.ofNullable(node)
                    .filter(n -> n != null)
                    .map(Neo4jWowVersion::new)
                    .ifPresent(versions::add));
            return versions;
        }
    }

    private static Query query() {
        return new Query();
    }

    private static class Query {
        private static final String CQL_FIND_VERSION = "MATCH (%s) WHERE %s:%s%s RETURN %s";

        private Branch branch;
        private Region region;
        private String version;

        public Query withBranch(Branch branch) {
            this.branch = branch;
            return this;
        }

        public Query withRegion(Region region) {
            this.region = region;
            return this;
        }

        public Query withVersion(String version) {
            this.version = version;
            return this;
        }

        public Result execute(Neo4jProvider provider) {
            return provider.execute(queryString(), properties());
        }

        public Map<String, Object> properties() {
            Map<String, Object> properties = new HashMap<>();
            if (branch != null) {
                properties.put(String.valueOf(PROPERTY_BRANCH), String.valueOf(branch));
            }
            if (region != null) {
                properties.put(String.valueOf(PROPERTY_REGION), String.valueOf(region));
            }
            if (isNotEmpty(version)) {
                properties.put(String.valueOf(PROPERTY_VERSION), version);
            }
            return properties;
        }

        public String queryString() {
            StringBuilder whereClause = new StringBuilder();
            if (branch != null) {
                whereClause.append(" AND ").append(NODE_COLUMN).append('.').append(PROPERTY_BRANCH).append(" = {").append(PROPERTY_BRANCH).append("}");
            }
            if (region != null) {
                whereClause.append(" AND ").append(NODE_COLUMN).append('.').append(PROPERTY_REGION).append(" = {").append(PROPERTY_REGION).append("}");
            }
            if (isNotEmpty(version)) {
                whereClause.append(" AND ").append(NODE_COLUMN).append('.').append(PROPERTY_VERSION).append(" = {").append(PROPERTY_VERSION).append("}");
            }
            return format(CQL_FIND_VERSION, NODE_COLUMN, NODE_COLUMN, Neo4jWowVersion.LABEL, whereClause.toString(), NODE_COLUMN);
        }

    }
}
