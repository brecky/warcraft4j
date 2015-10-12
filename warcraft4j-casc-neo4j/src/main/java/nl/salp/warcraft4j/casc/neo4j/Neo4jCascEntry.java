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

import nl.salp.warcraft4j.util.DataTypeUtil;
import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.neo4j.Neo4jDataSource;
import nl.salp.warcraft4j.neo4j.Neo4jEntry;
import nl.salp.warcraft4j.neo4j.model.Neo4jWowVersion;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Optional;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public abstract class Neo4jCascEntry extends Neo4jEntry {
    protected Neo4jCascEntry(Node node, Label label) throws IllegalArgumentException {
        super(node, label);
    }

    protected final Optional<FileKey> getFileKey(Neo4jCascProperty property) {
        return node()
                .filter(n -> n.hasProperty(String.valueOf(property)))
                .map(n -> (String) n.getProperty(String.valueOf(property)))
                .map(k -> k.substring(0, 18))
                .map(DataTypeUtil::hexStringToByteArray)
                .map(FileKey::new);
    }

    protected final void setFileKey(Neo4jCascProperty property, FileKey fileKey) {
        Optional<FileKey> fk = Optional.ofNullable(fileKey);
        fk.filter(f -> f.length() > 0)
                .map(FileKey::toHexString)
                .ifPresent(f -> getNode().setProperty(String.valueOf(property), f));
        if (!fk.isPresent()) {
            getNode().removeProperty(String.valueOf(property));
        }
    }

    protected final Optional<ContentChecksum> getContentChecksum(Neo4jCascProperty property) {
        return getByteArray(String.valueOf(property))
                .map(ContentChecksum::new);
    }

    protected final void setContentChecksum(Neo4jCascProperty property, ContentChecksum checksum) {
        Optional<ContentChecksum> cs = Optional.ofNullable(checksum);
        cs.filter(c -> c.length() > 0)
                .map(ContentChecksum::toHexString)
                .ifPresent(c -> getNode().setProperty(String.valueOf(property), c));
        if (!cs.isPresent()) {
            remove(String.valueOf(property));
        }
    }

    public final void forWowVersion(Neo4jWowVersion wowVersion) throws IllegalArgumentException {
        if (wowVersion == null) {
            throw new IllegalArgumentException("Can't relate the node to a null wow version.");
        }
        wowVersion.addVersion(this);
    }

    public final void fromSource(Neo4jDataSource source) throws IllegalArgumentException {
        if (source == null) {
            throw new IllegalArgumentException("Can't relate the node to a null data source.");
        }
        source.addSource(this);
    }
}
