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
package nl.salp.warcraft4j.casc.neo4j;

import nl.salp.warcraft4j.casc.CascFile;
import nl.salp.warcraft4j.casc.FileHeader;
import nl.salp.warcraft4j.hash.JenkinsHash;
import nl.salp.warcraft4j.neo4j.Neo4jException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nl.salp.warcraft4j.casc.neo4j.Neo4jCascProperty.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jCascFile extends Neo4jCascEntry implements CascFile {
    private static final Neo4jCascLabel LABEL = Neo4jCascLabel.CASC_FILE;

    public Neo4jCascFile(Node node) {
        super(node, LABEL);
    }

    @Override
    public long getFilenameHash() throws Neo4jException {
        return getLong(String.valueOf(FILENAME_HASH))
                .orElseThrow(() -> new Neo4jException("Unable to get the filename hash."));
    }

    public void setFilenameHash(long hash) throws IllegalArgumentException {
        if (hash == 0 || hash == JenkinsHash.HASH_EMPTY_VALUE_HASHLITTLE2) {
            throw new IllegalArgumentException("Can't set the filename hash to a non-initialised filename hash.");
        }
        setLong(String.valueOf(FILENAME_HASH), hash);
    }

    @Override
    public Optional<String> getFilename() {
        return getString(String.valueOf(FILENAME));
    }

    @Override
    public void setFilename(String filename) {
        setString(String.valueOf(FILENAME), filename);
    }

    @Override
    public Optional<FileHeader> getHeader() {
        return getByteArray(String.valueOf(FILE_HEADER))
                .map(FileHeader::new);
    }

    @Override
    public void setHeader(FileHeader header) {
        Optional.ofNullable(header)
                .map(FileHeader::getHeader)
                .ifPresent(h -> setByteArray(String.valueOf(FILE_HEADER), h));
    }

    public static String getIndexProperty() {
        return String.valueOf(FILENAME_HASH);
    }

    public static Label getTypeLabel() {
        return LABEL;
    }

    public static Map<String, Object> toNodeProperties(CascFile cascFile) {
        Map<String, Object> props = new HashMap<>();
        if (cascFile != null) {
            cascFile.getFilenameHash();
            cascFile.getFilename().ifPresent(filename -> props.put(String.valueOf(FILENAME), filename));
            cascFile.getHeader()
                    .map(FileHeader::getHeader)
                    .filter(header -> header != null)
                    .filter(header -> header.length > 0)
                    .ifPresent(header -> props.put(String.valueOf(FILE_HEADER), header));
        }
        return props;
    }
}
