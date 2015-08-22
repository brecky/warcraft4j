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
package nl.salp.warcraft4j.dev.casc.neo4j.model;

import nl.salp.warcraft4j.data.casc.CascContext;
import nl.salp.warcraft4j.dev.casc.FileHeader;
import nl.salp.warcraft4j.dev.casc.neo4j.Neo4jCascException;
import nl.salp.warcraft4j.hash.JenkinsHash;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascProperty.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jCascFile extends Neo4jCascEntry {
    private static final CascLabel LABEL = CascLabel.CASC_FILE;

    public Neo4jCascFile(Node node) {
        super(node, LABEL);
    }

    public Neo4jCascFile(String filename, long hash, FileHeader fileHeader, CascContext cascContext, GraphDatabaseService graphDb, Transaction tx) throws IllegalArgumentException {
        super(graphDb.createNode(LABEL), LABEL);
        if (hash == 0 || hash == JenkinsHash.HASH_EMPTY_VALUE_HASHLITTLE2) {
            throw new IllegalArgumentException("Can't create a Neo4jCascFile from a non-initialised filename hash.");
        }
        if (cascContext == null) {
            throw new IllegalArgumentException("Can't create a Neo4jCascFile from a null casc context.");
        }
        setWowVersion(cascContext.getVersion());
        setWowRegion(cascContext.getRegion());
        setWowLocale(cascContext.getLocale());
        setWowBranch(cascContext.getBranch());
        setFilenameHash(hash);
        setFilename(filename);
        setHeader(fileHeader);
    }

    public Optional<String> getFilename() {
        return getString(FILENAME);
    }

    public void setFilename(String filename) {
        setString(FILENAME, filename);
    }

    public long getFilenameHash() {
        return getLong(FILENAME_HASH)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the filename hash."));
    }

    public void setFilenameHash(long hash) throws IllegalArgumentException {
        if (hash == 0 || hash == JenkinsHash.HASH_EMPTY_VALUE_HASHLITTLE2) {
            throw new IllegalArgumentException("Can't set the filename hash to a non-initialised filename hash.");
        }
        setLong(FILENAME_HASH, hash);
    }

    public Optional<FileHeader> getHeader() {
        return getByteArray(FILE_HEADER)
                .map(FileHeader::new);
    }

    public void setHeader(FileHeader header) {
        Optional.ofNullable(header)
                .map(FileHeader::getHeader)
                .ifPresent(h -> setByteArray(FILE_HEADER, h));
    }

    public static Map<String, Object> toNodeProperties(String filename, long hash, FileHeader fileHeader, CascContext cascContext) {
        Map<String, Object> props = new HashMap<>();
        props.put(WOW_VERSION.getName(), cascContext.getVersion());
        props.put(WOW_REGION.getName(), cascContext.getRegion());
        props.put(WOW_LOCALE.getName(), cascContext.getLocale());
        props.put(WOW_BRANCH.getName(), cascContext.getBranch());
        Optional.ofNullable(filename)
                .filter(StringUtils::isNotEmpty)
                .ifPresent(file -> props.put(FILENAME.getName(), file));
        props.put(FILENAME_HASH.getName(), hash);
        Optional.ofNullable(fileHeader)
                .map(FileHeader::toHexString)
                .filter(StringUtils::isNotEmpty)
                .ifPresent(h -> props.put(FILE_HEADER.getName(), h));
        return props;
    }
}
