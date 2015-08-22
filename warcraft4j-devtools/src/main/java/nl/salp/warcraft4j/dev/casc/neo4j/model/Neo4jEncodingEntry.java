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

import nl.salp.warcraft4j.DataTypeUtil;
import nl.salp.warcraft4j.data.casc.CascContext;
import nl.salp.warcraft4j.data.casc.ContentChecksum;
import nl.salp.warcraft4j.data.casc.EncodingEntry;
import nl.salp.warcraft4j.data.casc.FileKey;
import nl.salp.warcraft4j.dev.casc.neo4j.Neo4jCascException;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascProperty.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jEncodingEntry extends Neo4jCascEntry implements EncodingEntry {
    private static final CascLabel LABEL = CascLabel.ENCODING_ENTRY;

    public Neo4jEncodingEntry(Node node) {
        super(node, LABEL);
    }

    public Neo4jEncodingEntry(EncodingEntry entry, CascContext cascContext, GraphDatabaseService graphDb, Transaction tx) throws IllegalArgumentException {
        super(graphDb.createNode(LABEL), LABEL);
        if (entry == null) {
            throw new IllegalArgumentException("Can't create a Neo4jEncodingEntry from a null encoding entry.");
        }
        if (cascContext == null) {
            throw new IllegalArgumentException("Can't create a Neo4jEncodingEntry from a null casc context.");
        }
        setWowVersion(cascContext.getVersion());
        setWowRegion(cascContext.getRegion());
        setWowLocale(cascContext.getLocale());
        setWowBranch(cascContext.getBranch());
        setContentChecksum(entry.getContentChecksum());
        setFileSize(entry.getFileSize());
        setFirstFileKey(entry.getFirstFileKey());
        setFileKeys(entry.getFileKeys());
    }

    @Override
    public long getFileSize() {
        return getLong(FILE_SIZE)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the file size."));
    }

    public void setFileSize(long fileSize) {
        setLong(FILE_SIZE, fileSize);
    }

    @Override
    public ContentChecksum getContentChecksum() {
        return getContentChecksum(CONTENT_CHECKSUM)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the content checksum."));
    }

    public void setContentChecksum(ContentChecksum contentChecksum) {
        setContentChecksum(CONTENT_CHECKSUM, contentChecksum);
    }

    @Override
    public FileKey getFirstFileKey() {
        return getFileKey(FILE_KEY)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the first file key."));
    }

    public void setFirstFileKey(FileKey fileKey) {
        setFileKey(FILE_KEY, fileKey);
    }

    @Override
    public List<FileKey> getFileKeys() {
        return get(FILE_KEYS)
                .map(String[].class::cast)
                .map(Stream::of)
                .map(s -> s
                        .filter(StringUtils::isNotEmpty)
                        .map(k -> k.substring(0, 18))
                        .map(DataTypeUtil::hexStringToByteArray)
                        .map(FileKey::new)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new Neo4jCascException("Unable to get the file keys."));
    }

    public void setFileKeys(List<FileKey> fileKeys) {
        if (fileKeys != null) {
            setFileKeys(fileKeys.toArray(new FileKey[fileKeys.size()]));
        }
    }

    public void setFileKeys(FileKey... fileKeys) {
        set(FILE_KEYS, Stream.of(fileKeys)
                .filter(k -> k != null)
                .map(FileKey::getFileKey)
                .map(DataTypeUtil::byteArrayToHexString)
                .toArray(String[]::new));
    }
}
