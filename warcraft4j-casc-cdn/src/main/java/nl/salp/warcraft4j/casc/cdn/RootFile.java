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
import nl.salp.warcraft4j.casc.RootEntry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class RootFile {
    private final Map<Long, List<RootEntry>> entries;

    public RootFile(Map<Long, List<RootEntry>> entries) throws IllegalArgumentException {
        this.entries = Optional.ofNullable(entries)
                .filter(m -> !m.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Can't initialise the root with no entries."));
    }

    public long getHashCount() {
        return entries.size();
    }

    public Set<Long> getHashes() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    public boolean isEntryAvailable(long hash) {
        return entries.containsKey(hash) && !getEntries(hash).isEmpty();
    }

    protected List<RootEntry> getEntries(long hash) {
        return entries.getOrDefault(hash, Collections.emptyList());
    }

    public List<ContentChecksum> getContentChecksums(long hash) {
        return getEntries(hash).stream()
                .map(RootEntry::getContentChecksum)
                .collect(Collectors.toList());
    }

    public Collection<RootEntry> getEntries() {
        return entries.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
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
                .toString();
    }
}
