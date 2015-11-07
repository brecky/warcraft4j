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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;
import nl.salp.warcraft4j.util.Checksum;
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
    private final Map<FileKey, IndexEntry> entries;

    public Index(Collection<IndexEntry> indexEntries) {
        this.entries = new HashMap<>();
        Optional.ofNullable(indexEntries)
                .orElseThrow(() -> new CascParsingException("Can't create a file index from null index entries."))
                .forEach(this::addEntry);
    }

    private void addEntry(IndexEntry entry) {
        if (!entries.containsKey(entry.getFileKey())) {
            entries.put(entry.getFileKey(), entry);
        }
    }

    public Optional<IndexEntry> getEntry(FileKey fileKey) {
        Checksum cs = fileKey;
        if (cs.length() > 9) {
            cs = fileKey.trim(9);
        }
        return Optional.ofNullable(entries.get(cs));
    }

    public Collection<IndexEntry> getEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }

    public Optional<Integer> getDataFileNumber(FileKey fileKey) {
        return getEntry(fileKey).map(IndexEntry::getFileNumber);
    }

    public Optional<Integer> getDataOffset(FileKey fileKey) {
        return getEntry(fileKey).map(IndexEntry::getDataFileOffset);
    }

    public Optional<Long> getDataSize(FileKey fileKey) {
        return getEntry(fileKey).map(IndexEntry::getFileSize);
    }

    public Collection<FileKey> getFileKeys() {
        return Collections.unmodifiableSet(entries.keySet());
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
