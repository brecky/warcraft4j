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
package nl.salp.warcraft4j.casc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

/**
 * {@link RootEntry} for a CDN based CASC, relating the hash of a filename to the checksum of the content of a file.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.RootEntry
 */
public class CascRootEntry implements RootEntry {
    /** The hash of the filename. */
    private final long filenameHash;
    /** The checksum of the file content. */
    private final ContentChecksum contentChecksum;
    /** The flags for the entry. */
    private final long blockFlags;
    /** The parsed block of unknown data. */
    private final long blockUnknown;
    /** The parsed block of unknown entry. */
    private final long entryUnknown;

    /**
     * Create a new root entry instance.
     *
     * @param filenameHash    The hash of the filename.
     * @param contentChecksum The checksum of the file content.
     * @param blockFlags      The flags for the entry.
     * @param blockUnknown    The parsed block of unknown data.
     * @param entryUnknown    The parsed block of unknown entry.
     */
    public CascRootEntry(long filenameHash, ContentChecksum contentChecksum, long blockFlags, long blockUnknown, long entryUnknown) {
        this.filenameHash = filenameHash;
        this.contentChecksum =
                Optional.ofNullable(contentChecksum).orElseThrow(() -> new IllegalArgumentException("Unable to create a root file entry with a null content checksum"));

        this.blockFlags = blockFlags;
        this.blockUnknown = blockUnknown;
        this.entryUnknown = entryUnknown;
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
    public long getFilenameHash() {
        return filenameHash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFlags() {
        return blockFlags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CascLocale> getLocale() {
        return CascLocale.getLocale(blockFlags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
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