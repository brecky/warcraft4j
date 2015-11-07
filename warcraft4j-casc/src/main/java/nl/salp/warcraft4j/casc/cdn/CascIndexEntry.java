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

import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

/**
 * {@link IndexEntry} implementation for a CDN based CASC, relating a file key to a actual file (segment).
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.IndexEntry
 */
public class CascIndexEntry implements IndexEntry {
    /** The file key of the entry. */
    private final FileKey fileKey;
    /** The data file number containing the file. */
    private final int fileNumber;
    /** The offset in the data file where the file data starts. */
    private final int dataFileOffset;
    /** The size of the file data in the data file. */
    private final long fileSize;
    /** The cached hashcode of the index entry instance. */
    private final int hash;

    /**
     * Create a new index entry instance.
     *
     * @param fileKey        The file key of the entry.
     * @param fileNumber     The data file number containing the file.
     * @param dataFileOffset The offset in the data file where the file data starts.
     * @param fileSize       The size of the file data in the data file.
     *
     * @throws IllegalArgumentException When invalid data was provided.
     */
    public CascIndexEntry(FileKey fileKey, int fileNumber, int dataFileOffset, long fileSize) {
        this.fileKey = Optional.ofNullable(fileKey)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a CascIndexEntry for a null file key."));
        this.fileSize = fileSize;
        this.fileNumber = fileNumber;
        this.dataFileOffset = dataFileOffset;
        this.hash = fileKey.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileKey getFileKey() {
        return fileKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFileSize() {
        return fileSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFileNumber() {
        return fileNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDataFileOffset() {
        return dataFileOffset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}