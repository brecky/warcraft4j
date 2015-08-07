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
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class EncodingFile {
    private final byte[] locale;
    private final Map<Checksum, EncodingEntry> entries;

    public EncodingFile(byte[] locale, List<EncodingEntry> entries) {
        this.locale = locale;
        this.entries = new HashMap<>();
        entries.stream()
                .filter(e -> e != null)
                .filter(e -> e.getContentChecksum() != null)
                .forEach(e -> {
                    if (!this.entries.containsKey(e.getContentChecksum())) {
                        this.entries.put(e.getContentChecksum(), e);
                    }
                });
    }

    public byte[] getLocale() {
        return locale;
    }

    protected Optional<EncodingEntry> getEncodingEntry(ContentChecksum contentChecksum) {
        return Optional.ofNullable(entries.get(contentChecksum));
    }

    public Optional<Long> getFileSize(ContentChecksum contentChecksum) {
        return getEncodingEntry(contentChecksum).map(EncodingEntry::getFileSize);
    }

    public Optional<FileKey> getFileKey(ContentChecksum contentChecksum) {
        // FIXME Return first only or return (potentially) all?
        return getEncodingEntry(contentChecksum).map(EncodingEntry::getFirstFileKey);
    }

    protected Collection<EncodingEntry> getEntries() {
        return entries.values();
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
                .append("locale", new String(locale))
                .append("entries", entries.size())
                .append("checksums", entries.values().stream().map(EncodingEntry::getFileKeys).mapToInt(List::size).sum())
                .toString();
    }
}
