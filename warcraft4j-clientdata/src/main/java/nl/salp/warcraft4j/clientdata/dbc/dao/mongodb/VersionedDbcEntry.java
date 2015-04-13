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
package nl.salp.warcraft4j.clientdata.dbc.dao.mongodb;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import org.mongojack.Id;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class VersionedDbcEntry<T extends DbcEntry> {
    private static final String ID_MASK = "%d_v%d";
    @Id
    private final String _id;
    private final int version;
    private final int entryId;
    private final T entry;

    public VersionedDbcEntry(T entry, int version) {
        this.version = version;
        this.entryId = entry.getId();
        this.entry = entry;
        this._id = String.format(ID_MASK, entryId, version);
    }

    public VersionedDbcEntry(String _id, int version, int entryId, T entry) {
        this._id = _id;
        this.version = version;
        this.entryId = entryId;
        this.entry = entry;
    }

    public String get_id() {
        return _id;
    }

    public int getVersion() {
        return version;
    }

    public int getEntryId() {
        return entryId;
    }

    public T getEntry() {
        return entry;
    }

    public static <T extends DbcEntry> List<VersionedDbcEntry<? extends T>> forEntries(Collection<? extends T> entries, int version) {
        List<VersionedDbcEntry<? extends T>> wrappedEntries = new ArrayList<>();
        for (T entry : entries) {
            wrappedEntries.add(new VersionedDbcEntry<>(entry, version));
        }
        return wrappedEntries;
    }
}
