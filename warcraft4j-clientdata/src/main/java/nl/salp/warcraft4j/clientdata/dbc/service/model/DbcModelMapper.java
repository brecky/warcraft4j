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
package nl.salp.warcraft4j.clientdata.dbc.service.model;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcStore;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Mapped for mapping DBC entries to the model classes.
 * <p/>
 * All mappings are transitive in regards to relations (e.g. referenced models are also mapped).
 *
 * @author Barre Dijkstra
 */
public abstract class DbcModelMapper<T, E extends DbcEntry> {

    /**
     * Create a new model mapper that initialises (adds) the entry class in the {@link DbcStore}.
     *
     * @param store        The {@link DbcStore} to initialise the entry in.
     * @param dbcDirectory The directory where the DBC files are stored.
     *
     * @throws IOException When the mapped DBC file could not be added.
     */
    public DbcModelMapper(DbcStore store, String dbcDirectory) throws IOException {
        store.add(getEntryClass(), dbcDirectory);
    }

    /**
     * Create a new model mapper that uses a pre-initialised {@link DbcStore} for look ups.
     */
    public DbcModelMapper() {
    }

    /**
     * Map all available DBC entries to model instances.
     *
     * @param store The DbcStore with the DBC entries.
     *
     * @return The model instances.
     */
    public Collection<T> mapAll(DbcStore store) {
        Collection<E> entries = store.getInstances(getEntryClass());
        Collection<T> itemSubClasses = new HashSet<>(entries.size());
        for (E entry : entries) {
            itemSubClasses.add(map(entry, store));
        }
        return itemSubClasses;
    }

    /**
     * Map all available DBC entries that match the filter to the model instance.
     * <p/>
     * TODO replace this with a real filter in the store instead of doing a full lookup first.
     *
     * @param store   The DBC store with the available DBC entries.
     * @param filters The filters that all mapped entries should match (inclusive).
     *
     * @return The mapped instances.
     */
    public Collection<T> mapAll(DbcStore store, EntryFilter<E>... filters) {
        Collection<E> entries = store.getInstances(getEntryClass());
        Collection<T> itemSubClasses = new HashSet<>(entries.size());
        for (E entry : entries) {
            boolean matchesFilter = true;
            for (EntryFilter<E> filter : filters) {
                matchesFilter = matchesFilter && filter.isMatching(entry);
            }
            if (matchesFilter) {
                itemSubClasses.add(map(entry, store));
            }
        }
        return itemSubClasses;
    }

    /**
     * Map a DBC entry to the model instance.
     *
     * @param id    The id of the entry to map.
     * @param store The DBC store with the available DBC entries.
     *
     * @return The mapped instance or {@code null} if no entries were available with the given id.
     */
    public T map(int id, DbcStore store) {
        T itemSubClass = null;
        E entry = store.resolve(getEntryClass(), id);
        if (entry != null) {
            itemSubClass = map(entry, store);
        }
        return itemSubClass;
    }

    /**
     * Map the DBC entry to the model instance.
     *
     * @param entry The DBC entry to map.
     * @param store The DBC store with the available DBC entries.
     *
     * @return The model instance.
     */
    protected abstract T map(E entry, DbcStore store);

    /**
     * Get the class of the DBC mapping entry.
     *
     * @return The DBC entry class.
     */
    public abstract Class<E> getEntryClass();

    /**
     * Get the class of the model.
     *
     * @return The model class.
     */
    public abstract Class<T> getModelClass();

}
