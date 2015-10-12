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
import nl.salp.warcraft4j.casc.EncodingEntry;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.neo4j.Neo4jException;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.salp.warcraft4j.casc.neo4j.Neo4jCascProperty.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jEncodingEntry extends Neo4jCascEntry implements EncodingEntry {
    private static final Neo4jCascLabel LABEL = Neo4jCascLabel.ENCODING_ENTRY;

    public Neo4jEncodingEntry(Node node) {
        super(node, LABEL);
    }

    @Override
    public long getFileSize() {
        return getLong(String.valueOf(FILE_SIZE))
                .orElseThrow(() -> new Neo4jException("Unable to get the file size."));
    }

    public void setFileSize(long fileSize) {
        setLong(String.valueOf(FILE_SIZE), fileSize);
    }

    @Override
    public ContentChecksum getContentChecksum() {
        return getContentChecksum(CONTENT_CHECKSUM)
                .orElseThrow(() -> new Neo4jException("Unable to get the content checksum."));
    }

    public void setContentChecksum(ContentChecksum contentChecksum) {
        setContentChecksum(CONTENT_CHECKSUM, contentChecksum);
    }

    @Override
    public FileKey getFirstFileKey() {
        return getFileKey(FILE_KEY)
                .orElseThrow(() -> new Neo4jException("Unable to get the first file key."));
    }

    public void setFirstFileKey(FileKey fileKey) {
        setFileKey(FILE_KEY, fileKey);
    }

    @Override
    public List<FileKey> getFileKeys() {
        return get(String.valueOf(FILE_KEYS))
                .map(String[].class::cast)
                .map(Stream::of)
                .map(s -> s
                        .filter(StringUtils::isNotEmpty)
                        .map(k -> k.substring(0, 18))
                        .map(DataTypeUtil::hexStringToByteArray)
                        .map(FileKey::new)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new Neo4jException("Unable to get the file keys."));
    }

    @Override
    public boolean isMultiBlock() {
        return getFileKeys().size() > 1;
    }

    @Override
    public int getBlockCount() {
        return getFileKeys().size();
    }

    public void setFileKeys(FileKey... fileKeys) {
        set(String.valueOf(FILE_KEYS), Stream.of(fileKeys)
                .filter(k -> k != null)
                .map(FileKey::getFileKey)
                .map(DataTypeUtil::byteArrayToHexString)
                .toArray(String[]::new));
    }

    public void setFileKeys(List<FileKey> fileKeys) {
        if (fileKeys != null) {
            setFileKeys(fileKeys.toArray(new FileKey[fileKeys.size()]));
        }
    }

    public static Map<String, Object> toNodeProperties(EncodingEntry encodingEntry) {
        Map<String, Object> props = new HashMap<>();
        props.put(String.valueOf(CONTENT_CHECKSUM), encodingEntry.getContentChecksum().toHexString());
        props.put(String.valueOf(FILE_KEYS), encodingEntry.getFileKeys().stream()
                .map(FileKey::toHexString)
                .toArray(String[]::new));
        props.put(String.valueOf(FILE_KEY), encodingEntry.getFirstFileKey().toHexString());
        props.put(String.valueOf(FILE_SIZE), encodingEntry.getFileSize());
        return props;
    }
}
