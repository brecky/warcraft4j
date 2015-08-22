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
import nl.salp.warcraft4j.data.casc.FileKey;
import nl.salp.warcraft4j.data.casc.IndexEntry;
import nl.salp.warcraft4j.dev.casc.neo4j.Neo4jCascException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static nl.salp.warcraft4j.dev.casc.neo4j.model.CascProperty.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Neo4jIndexEntry extends Neo4jCascEntry implements IndexEntry {
    private static final CascLabel LABEL = CascLabel.INDEX_ENTRY;

    public Neo4jIndexEntry(Node node) throws IllegalArgumentException {
        super(node, LABEL);
    }

    public Neo4jIndexEntry(IndexEntry entry, CascContext cascContext, GraphDatabaseService graphDb, Transaction tx) throws IllegalArgumentException {
        super(graphDb.createNode(LABEL), LABEL);
        if (entry == null) {
            throw new IllegalArgumentException("Can't create a Neo4jIndexEntry from a null encoding entry.");
        }
        if (cascContext == null) {
            throw new IllegalArgumentException("Can't create a Neo4jIndexEntry from a null casc context.");
        }
        setWowVersion(cascContext.getVersion());
        setWowRegion(cascContext.getRegion());
        setWowLocale(cascContext.getLocale());
        setWowBranch(cascContext.getBranch());
        setFileKey(entry.getFileKey());
        setFileSize(entry.getFileSize());
        setFileNumber(entry.getFileNumber());
        setDataFileOffset(entry.getDataFileOffset());
    }

    @Override
    public FileKey getFileKey() {
        return getFileKey(FILE_KEY)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the file key."));
    }

    public void setFileKey(FileKey fileKey) {
        setFileKey(FILE_KEY, fileKey);
    }

    @Override
    public long getFileSize() {
        return getLong(DATAFILE_SIZE)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the data file size."));
    }

    public void setFileSize(long fileSize) {
        setLong(DATAFILE_SIZE, fileSize);
    }

    @Override
    public int getFileNumber() {
        return getInt(DATAFILE_NUMBER)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the data file number."));
    }

    public void setFileNumber(int fileNumber) {
        setInt(DATAFILE_NUMBER, fileNumber);
    }

    @Override
    public int getDataFileOffset() {
        return getInt(DATAFILE_OFFSET)
                .orElseThrow(() -> new Neo4jCascException("Unable to get the data file offset."));
    }

    public void setDataFileOffset(int dataFileOffset) {
        setInt(DATAFILE_OFFSET, dataFileOffset);
    }
}
