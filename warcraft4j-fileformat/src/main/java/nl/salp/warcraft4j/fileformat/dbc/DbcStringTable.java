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

package nl.salp.warcraft4j.fileformat.dbc;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * DBC data file StringBlock segment containing the text values to use for fields.
 *
 * @author Barre Dijkstra
 */
public class DbcStringTable {
    /** The strings, indexed by their offset. */
    private final Map<Integer, String> strings;

    /**
     * Create a new StringBlock instance.
     *
     * @param strings The strings, indexed by their offset.
     */
    public DbcStringTable(Map<Integer, String> strings) {
        if (strings == null) {
            this.strings = Collections.emptyMap();
        } else {
            this.strings = strings;
        }
    }

    /**
     * Check if an entry is available for the given position.
     *
     * @param position The position.
     *
     * @return {@code true} if an entry is available.
     */
    public boolean isEntryAvailableForPosition(int position) {
        return strings.containsKey(position);
    }

    /**
     * Get the entry for the given position.
     *
     * @param position The position.
     *
     * @return Optional containing the entry if available.
     */
    public Optional<String> getEntry(int position) {
        return Optional.ofNullable(strings.get(position));
    }

    /**
     * Get the available positions registered.
     *
     * @return The available positions as unmodifiable set.
     */
    public Set<Integer> getAvailablePositions() {
        return Collections.unmodifiableSet(strings.keySet());
    }

    /**
     * Get the number of entries in the StringTable.
     *
     * @return The number of entries.
     */
    public int getNumberOfEntries() {
        return strings.size();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
