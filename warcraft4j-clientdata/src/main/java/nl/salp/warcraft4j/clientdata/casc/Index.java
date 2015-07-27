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
package nl.salp.warcraft4j.clientdata.casc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Index {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class);
    private final Map<Checksum, IndexEntry> entries;

    public Index(Collection<IndexFile> indexFiles) {
        this.entries = new HashMap<>();
        LOGGER.trace("Creating index from {} index files", indexFiles.size());
        addFiles(indexFiles);
        LOGGER.trace("Creating index with {} entries", entries.size());
    }

    public Index(Collection<IndexFile> latestIndexFiles, Collection<IndexFile> olderFiles) {
        this.entries = new HashMap<>();
        LOGGER.trace("Creating index from {} index files, appending with {} older index file versions", latestIndexFiles.size(), olderFiles.size());
        addFiles(latestIndexFiles);
        addFiles(olderFiles);
        LOGGER.trace("Creating index with {} entries", entries.size());
    }

    private void addFiles(Collection<IndexFile> indexFiles) {
        long failCnt = indexFiles.stream().map(IndexFile::getEntries).flatMap(Collection::stream)
                .mapToInt(IndexEntry::getFileNumber)
                .filter(i -> i < 0 || i > 32)
                .count();
        if (failCnt > 0) {
            throw new CascFileParsingException(String.format("Got %d entries with negative keys...", failCnt));
        }
        indexFiles.stream()
                .map(IndexFile::getEntries)
                .flatMap(Collection::stream)
                .forEach(this::addEntry);
    }

    private void addEntry(IndexEntry entry) {
        if (!entries.containsKey(entry.getFileKey())) {
            entries.put(entry.getFileKey(), entry);
        }
    }

    public Optional<IndexEntry> getEntry(Checksum fileKey) {
        Checksum cs = fileKey;
        if (cs.length() > 9) {
            cs = fileKey.trim(9);
        }
        return Optional.ofNullable(entries.get(cs));
    }

    public Optional<Integer> getDataFileNumber(Checksum fileKey) {
        return getEntry(fileKey).map(e -> (int) e.getFileNumber());
    }

    public Optional<Integer> getDataOffset(Checksum fileKey) {
        return getEntry(fileKey).map(e -> e.getDataFileOffset());
    }

    public Optional<Long> getDataSize(Checksum fileKey) {
        return getEntry(fileKey).map(e -> e.getFileSize());
    }

    public Collection<IndexEntry> getEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }

    public int getEntryCount() {
        return entries.size();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entries", entries.size())
                .build();
    }
}
