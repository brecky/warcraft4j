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

import nl.salp.warcraft4j.data.casc.*;
import nl.salp.warcraft4j.dev.casc.neo4j.Neo4jCascException;
import nl.salp.warcraft4j.dev.casc.neo4j.ReferenceEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascLabel.CASC_FILE;
import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascLabel.ENCODING_ENTRY;
import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascProperty.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jRootEntry extends Neo4jCascEntry implements RootEntry {
    public Neo4jRootEntry(Node node) {
        super(node, CascLabel.ROOT_ENTRY);
    }

    @Override
    public ContentChecksum getContentChecksum() {
        return getContentChecksum(CONTENT_CHECKSUM)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the content checksum."));
    }

    @Override
    public long getFilenameHash() {
        return getLong(FILENAME_HASH)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the filename hash."));

    }

    @Override
    public long getFlags() {
        return getLong(FILE_FLAGS)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the file flags."));
    }

    @Override
    public Optional<CascLocale> getLocale() {
        Optional<CascLocale> loc;
        if (getNode().hasProperty(FILE_LOCALE.getName()) && !"unknown".equals(getNode().getProperty(FILE_LOCALE.getName()))) {
            loc = CascLocale.getLocale((String) getNode().getProperty(FILE_LOCALE.getName()));
        } else {
            loc = CascLocale.getLocale(getFlags());
            loc.ifPresent(l -> getNode().setProperty(FILE_LOCALE.getName(), l.name()));
        }
        return loc;
    }

    public Collection<EncodingEntry> getReferencedEncodingEntries(GraphDatabaseService service) {
        return getReferencedEntries(Neo4jEncodingEntry::new, Direction.OUTGOING, new ReferenceEvaluator(1, ENCODING_ENTRY), service);
    }

    public Collection<Neo4jCascFile> getReferencingCascFiles(GraphDatabaseService service) {
        return getReferencedEntries(Neo4jCascFile::new, Direction.OUTGOING, new ReferenceEvaluator(1, CASC_FILE), service);
    }

    public static Map<String, Object> toNodeProperties(RootEntry rootEntry, CascContext cascContext) {
        Map<String, Object> props = new HashMap<>();
        props.put(WOW_VERSION.getName(), cascContext.getVersion());
        props.put(WOW_REGION.getName(), cascContext.getRegion());
        props.put(WOW_LOCALE.getName(), cascContext.getLocale());
        props.put(WOW_BRANCH.getName(), cascContext.getBranch());
        props.put(FILE_KEY.getName(), rootEntry.getContentChecksum());
        props.put(DATAFILE_SIZE.getName(), rootEntry.getFilenameHash());
        props.put(DATAFILE_NUMBER.getName(), rootEntry.getFlags());
        return props;
    }
}
