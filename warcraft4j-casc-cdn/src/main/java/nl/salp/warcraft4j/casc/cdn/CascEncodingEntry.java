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

import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.EncodingEntry;
import nl.salp.warcraft4j.casc.FileKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Optional;

/**
 * {@link EncodingEntry} for a CDN based CASC, relating the checksum of the content of a file to the file keys referencing the file (segement) data.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.EncodingEntry
 */
public class CascEncodingEntry implements EncodingEntry {
    /** The size of the referenced file. */
    private final long fileSize;
    /** The checksum of the file content. */
    private final ContentChecksum contentChecksum;
    /** The file keys referencing the file segments. */
    private final List<FileKey> fileChecksums;
    /** The cached hashcode of the encoding entry instance. */
    private final int hash;

    /**
     * Create a new encoding entry instance.
     *
     * @param fileSize        The size of the referenced file.
     * @param contentChecksum The checksum of the file content.
     * @param fileChecksums   The file keys referencing the file segments.
     *
     * @throws IllegalArgumentException When the provided values were incorrect.
     */
    public CascEncodingEntry(long fileSize, ContentChecksum contentChecksum, List<FileKey> fileChecksums) throws IllegalArgumentException {
        this.fileSize = fileSize;
        this.contentChecksum = Optional.ofNullable(contentChecksum).orElseThrow(() -> new IllegalArgumentException("Can't create an encoding file entry with no content checksum"));
        this.fileChecksums = Optional.ofNullable(fileChecksums)
                .filter(f -> !f.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Can't create an encoding file entry with no file checksums"));
        this.hash = contentChecksum.hashCode();
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
    public ContentChecksum getContentChecksum() {
        return contentChecksum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileKey getFirstFileKey() {
        return fileChecksums.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileKey> getFileKeys() {
        return fileChecksums;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMultiBlock() {
        return fileChecksums.size() > 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBlockCount() {
        return fileChecksums.size();
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