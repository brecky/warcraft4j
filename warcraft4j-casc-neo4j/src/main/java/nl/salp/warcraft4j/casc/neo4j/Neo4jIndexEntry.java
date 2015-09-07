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

import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;
import nl.salp.warcraft4j.neo4j.Neo4jException;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jIndexEntry extends Neo4jCascEntry implements IndexEntry {
    private static final Neo4jCascLabel LABEL = Neo4jCascLabel.INDEX_ENTRY;

    public Neo4jIndexEntry(Node node) throws IllegalArgumentException {
        super(node, LABEL);
    }

    @Override
    public FileKey getFileKey() {
        return getFileKey(Neo4jCascProperty.FILE_KEY)
                .orElseThrow(() -> new Neo4jException("Unable to get the file key."));
    }

    public void setFileKey(FileKey fileKey) {
        setFileKey(Neo4jCascProperty.FILE_KEY, fileKey);
    }

    @Override
    public long getFileSize() {
        return getLong(String.valueOf(String.valueOf(Neo4jCascProperty.DATAFILE_SIZE)))
                .orElseThrow(() -> new Neo4jException("Unable to get the data file size."));
    }

    public void setFileSize(long fileSize) {
        setLong(String.valueOf(Neo4jCascProperty.DATAFILE_SIZE), fileSize);
    }

    @Override
    public int getFileNumber() {
        return getInt(String.valueOf(Neo4jCascProperty.DATAFILE_NUMBER))
                .orElseThrow(() -> new Neo4jException("Unable to get the data file number."));
    }

    public void setFileNumber(int fileNumber) {
        setInt(String.valueOf(Neo4jCascProperty.DATAFILE_NUMBER), fileNumber);
    }

    @Override
    public int getDataFileOffset() {
        return getInt(String.valueOf(Neo4jCascProperty.DATAFILE_OFFSET))
                .orElseThrow(() -> new Neo4jException("Unable to get the data file offset."));
    }

    public void setDataFileOffset(int dataFileOffset) {
        setInt(String.valueOf(Neo4jCascProperty.DATAFILE_OFFSET), dataFileOffset);
    }

    public static Map<String, Object> toNodeProperties(IndexEntry indexEntry) {
        Map<String, Object> props = new HashMap<>();
        props.put(String.valueOf(Neo4jCascProperty.FILE_KEY), indexEntry.getFileKey().toHexString());
        props.put(String.valueOf(Neo4jCascProperty.DATAFILE_SIZE), indexEntry.getFileSize());
        props.put(String.valueOf(Neo4jCascProperty.DATAFILE_NUMBER), indexEntry.getFileNumber());
        props.put(String.valueOf(Neo4jCascProperty.DATAFILE_OFFSET), indexEntry.getDataFileOffset());
        return props;
    }
}
