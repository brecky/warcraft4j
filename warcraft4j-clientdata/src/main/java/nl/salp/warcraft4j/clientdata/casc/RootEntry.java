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

import nl.salp.warcraft4j.clientdata.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class RootEntry {
    private final long filenameHash;
    private final Checksum contentChecksum;
    private final long blockFlags;
    private final long blockUnknown;
    private final long entryUnknown;

    public RootEntry(long filenameHash, Checksum contentChecksum, long blockFlags, long blockUnknown, long entryUnknown) {
        this.filenameHash = filenameHash;
        this.contentChecksum = Optional.ofNullable(contentChecksum).orElseThrow(() -> new IllegalArgumentException("Unable to create a root file entry with a null content checksum"));

        this.blockFlags = blockFlags;
        this.blockUnknown = blockUnknown;
        this.entryUnknown = entryUnknown;
    }

    public Checksum getContentChecksum() {
        return contentChecksum;
    }

    public long getFilenameHash() {
        return filenameHash;
    }

    public long getFlags() {
        return blockFlags;
    }

    public Optional<Locale> getLocale() {
        return Locale.getLocale(blockFlags);
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