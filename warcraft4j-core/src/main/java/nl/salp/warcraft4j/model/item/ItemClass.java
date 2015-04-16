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

package nl.salp.warcraft4j.model.item;

import nl.salp.warcraft4j.model.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Item group.
 *
 * @author Barre Dijkstra
 */
public class ItemClass extends Entity {
    /** The price modifier for the item group. */
    private float priceModifier;
    /** The name of the item group. */
    private String name;
    /** The item classes. */
    private Map<Integer, ItemSubClass> itemSubClasses;
    /** The item class masks for the item class. */
    private Map<Integer, ItemClassMask> itemClassMasks;

    /**
     * Create a new item class.
     *
     * @param id             The id of the item class.
     * @param priceModifier  The price modifier.
     * @param name           The name of the item class.
     * @param itemSubClasses The item subclasses.
     * @param itemClassMasks The item class masks.
     */
    public ItemClass(int id, float priceModifier, String name, Collection<ItemSubClass> itemSubClasses, Collection<ItemClassMask> itemClassMasks) {
        super(id);
        this.priceModifier = priceModifier;
        this.name = name;
        if (itemSubClasses == null) {
            this.itemSubClasses = Collections.emptyMap();
        } else {
            this.itemSubClasses = new HashMap<>(itemSubClasses.size());
            for (ItemSubClass subClass : itemSubClasses) {
                this.itemSubClasses.put(subClass.getId(), subClass);
            }
        }
        if (itemClassMasks == null) {
            this.itemClassMasks = Collections.emptyMap();
        } else {
            this.itemClassMasks = new HashMap<>(itemClassMasks.size());
            for (ItemClassMask mask : itemClassMasks) {
                this.itemClassMasks.put(mask.getId(), mask);
            }
        }
    }

    public float getPriceModifier() {
        return priceModifier;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, ItemSubClass> getItemSubClasses() {
        return Collections.unmodifiableMap(itemSubClasses);
    }

    public Map<Integer, ItemClassMask> getItemClassMasks() {
        return Collections.unmodifiableMap(itemClassMasks);
    }
}
