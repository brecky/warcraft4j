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

import nl.salp.warcraft4j.casc.CascLocale;
import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.RootEntry;
import nl.salp.warcraft4j.neo4j.Neo4jException;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jRootEntry extends Neo4jCascEntry implements RootEntry {
    public Neo4jRootEntry(Node node) {
        super(node, Neo4jCascLabel.ROOT_ENTRY);
    }

    @Override
    public ContentChecksum getContentChecksum() {
        return getContentChecksum(Neo4jCascProperty.CONTENT_CHECKSUM)
                .orElseThrow(() -> new Neo4jException("Unable to get the content checksum."));
    }

    @Override
    public long getFilenameHash() {
        return getLong(String.valueOf(Neo4jCascProperty.FILENAME_HASH))
                .orElseThrow(() -> new Neo4jException("Unable to get the filename hash."));

    }

    @Override
    public long getFlags() {
        return getLong(String.valueOf(Neo4jCascProperty.FILE_FLAGS))
                .orElseThrow(() -> new Neo4jException("Unable to get the file flags."));
    }

    @Override
    public Optional<CascLocale> getLocale() {
        Optional<CascLocale> loc;
        if (getNode().hasProperty(String.valueOf(Neo4jCascProperty.FILE_LOCALE)) && !"unknown".equals(getNode().getProperty(String.valueOf(Neo4jCascProperty.FILE_LOCALE)))) {
            loc = CascLocale.getLocale((String) getNode().getProperty(String.valueOf(Neo4jCascProperty.FILE_LOCALE)));
        } else {
            loc = CascLocale.getLocale(getFlags());
            loc.ifPresent(l -> getNode().setProperty(String.valueOf(Neo4jCascProperty.FILE_LOCALE), l.name()));
        }
        return loc;
    }

    public static Map<String, Object> toNodeProperties(RootEntry rootEntry) {
        Map<String, Object> props = new HashMap<>();
        props.put(String.valueOf(Neo4jCascProperty.CONTENT_CHECKSUM), rootEntry.getContentChecksum());
        props.put(String.valueOf(Neo4jCascProperty.FILENAME_HASH), rootEntry.getFilenameHash());
        props.put(String.valueOf(Neo4jCascProperty.FILE_FLAGS), rootEntry.getFlags());
        rootEntry.getLocale()
                .map(CascLocale::name)
                .ifPresent(l -> props.put(String.valueOf(Neo4jCascProperty.FILE_LOCALE), l));
        return props;
    }
}