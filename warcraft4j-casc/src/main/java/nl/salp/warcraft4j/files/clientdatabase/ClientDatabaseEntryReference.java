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

package nl.salp.warcraft4j.files.clientdatabase;

/**
 * Reference to a {@link ClientDatabaseEntry}.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabaseEntryReference<T extends ClientDatabaseEntry> {
    /** The item type being referenced. */
    private final ClientDatabaseEntryType entryType;
    /** The id of the referenced item. */
    private final int id;

    /**
     * Create a new client database entry reference.
     *
     * @param entryType The referenced entry type.
     * @param id        The id.
     */
    public ClientDatabaseEntryReference(ClientDatabaseEntryType entryType, int id) {
        this.entryType = entryType;
        this.id = id;
    }

    /**
     * Get the item type being referenced.
     *
     * @return The item type.
     */
    public ClientDatabaseEntryType getEntryType() {
        return entryType;
    }

    /**
     * Get the id of the referenced instance.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    public final T resolve(ClientDatabase clientDatabase) {
        return (T) clientDatabase.resolve(entryType, id);
    }
}
