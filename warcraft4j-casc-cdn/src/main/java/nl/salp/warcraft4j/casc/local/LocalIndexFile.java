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
package nl.salp.warcraft4j.casc.local;

import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.file.Path;
import java.util.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class LocalIndexFile {
    /** The path to the index file. */
    private final Path file;
    /** The file number. */
    private final int fileNumber;
    /** The file version. */
    private final int fileVersion;
    /** The parsed index entries. */
    private final Map<FileKey, IndexEntry> entries;

    public LocalIndexFile(Path file, int fileNumber, int fileVersion, List<IndexEntry> entries) {
        this.file = Optional.ofNullable(file).orElseThrow(() -> new IllegalArgumentException("Can't create an LocalIndexFile instance for an empty file path."));
        this.fileNumber = fileNumber;
        this.fileVersion = fileVersion;
        this.entries = new HashMap<>();
        entries.stream()
                .filter(e -> e != null)
                .filter(e -> e.getFileKey() != null)
                .forEach(e -> {
                    if (!this.entries.containsKey(e.getFileKey())) {
                        this.entries.put(e.getFileKey(), e);
                    }
                });
    }

    /**
     * Get the file number.
     *
     * @return The file number.
     */
    public int getFileNumber() {
        return fileNumber;
    }

    /**
     * Get the file version.
     *
     * @return The file version.
     */
    public int getFileVersion() {
        return fileVersion;
    }

    public Path getFile() {
        return file;
    }

    public Optional<IndexEntry> getEntry(FileKey fileKey) {
        return Optional.ofNullable(entries.get(fileKey));
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
        return ToStringBuilder.reflectionToString(this);
    }
}
