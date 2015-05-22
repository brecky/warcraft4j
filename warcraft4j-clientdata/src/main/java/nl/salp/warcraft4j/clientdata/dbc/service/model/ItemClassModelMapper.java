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

import nl.salp.warcraft4j.clientdata.dbc.DbcStore;
import nl.salp.warcraft4j.clientdata.dbc.entry.ItemClassEntry;
import nl.salp.warcraft4j.clientdata.dbc.entry.ItemSubClassEntry;
import nl.salp.warcraft4j.clientdata.dbc.entry.ItemSubClassMaskEntry;
import nl.salp.warcraft4j.model.item.ItemClass;
import nl.salp.warcraft4j.model.item.ItemClassMask;
import nl.salp.warcraft4j.model.item.ItemSubClass;

import java.util.Collection;

/**
 * {@link DbcModelMapper} implementation for mapping {@link ItemClassEntry} instances to {@link ItemClass} instances.
 *
 * @author Barre Dijkstra
 */
public class ItemClassModelMapper extends DbcModelMapper<ItemClass, ItemClassEntry> {

    @Override
    protected ItemClass map(ItemClassEntry entry, DbcStore store) {
        return new ItemClass(
                entry.getId(),
                entry.getPriceModifier(),
                entry.getName(),
                getSubClasses(entry, store),
                getClassMasks(entry, store)
        );
    }

    /**
     * Get and map all sub classes instances for the given item class entry.
     *
     * @param entry The entry to get all sub classes for.
     * @param store The DBC store to resolve the sub class entries on.
     *
     * @return The corresponding item sub class instances.
     */
    private Collection<ItemSubClass> getSubClasses(ItemClassEntry entry, DbcStore store) {
        return new ItemSubClassModelMapper().mapAll(store, new SubClassForClassFilter(entry.getId()));
    }

    /**
     * Get and map all class mask instances for the given item class entry.
     *
     * @param entry The entry to get all class masks for.
     * @param store The DBC store to resolve the sub class entries on.
     *
     * @return The corresponding item class mask instances.
     */
    private Collection<ItemClassMask> getClassMasks(ItemClassEntry entry, DbcStore store) {
        return new ItemClassMaskModelMapper().mapAll(store, new ClassMaskForClassFilter(entry.getId()));
    }

    @Override
    public Class<ItemClassEntry> getEntryClass() {
        return ItemClassEntry.class;
    }

    @Override
    public Class<ItemClass> getModelClass() {
        return ItemClass.class;
    }

    /**
     * Filter that filter all item sub classes on the id of an item class.
     */
    private static class SubClassForClassFilter implements EntryFilter<ItemSubClassEntry> {
        private final int itemClassId;

        public SubClassForClassFilter(int itemClassId) {
            this.itemClassId = itemClassId;
        }

        @Override
        public boolean isMatching(ItemSubClassEntry entry) {
            return entry != null && entry.getItemClassId() == this.itemClassId;
        }
    }

    /**
     * Filter that filter all item class masks on the id of an item class.
     */
    private static class ClassMaskForClassFilter implements EntryFilter<ItemSubClassMaskEntry> {
        private final int itemClassId;

        public ClassMaskForClassFilter(int itemClassId) {
            this.itemClassId = itemClassId;
        }

        @Override
        public boolean isMatching(ItemSubClassMaskEntry entry) {
            return entry != null && entry.getClassId() == this.itemClassId;
        }
    }
}
