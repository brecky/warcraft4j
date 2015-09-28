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
package nl.salp.warcraft4j.dev.casc.model;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class MultiMap<K, E> {
    private final Map<K, Set<E>> keyMapping;
    private final Map<E, Set<K>> valueMapping;

    public MultiMap() {
        keyMapping = new HashMap<>();
        valueMapping = new HashMap<>();
    }

    public int size() {
        return keyMapping.size();
    }

    public int valueSize() {
        return valueMapping.size();
    }

    public boolean isEmpty() {
        return keyMapping.isEmpty();
    }

    public boolean containsKey(K key) {
        return keyMapping.containsKey(key);
    }

    public boolean containsValue(E value) {
        return valueMapping.containsKey(value);
    }

    public Set<E> getValues(K key) {
        return Optional.ofNullable(unmodifiableSet(keyMapping.get(key))).orElse(emptySet());
    }

    public Set<K> getKeys(E value) {
        return Optional.ofNullable(unmodifiableSet(valueMapping.get(value))).orElse(emptySet());
    }

    public boolean add(K key, E value) {
        boolean added;
        if (key == null || value == null) {
            added = false;
        } else {
            keyMapping.putIfAbsent(key, new HashSet<>());
            valueMapping.putIfAbsent(value, new HashSet<>());
            added = valueMapping.get(value).add(key);
            added = keyMapping.get(key).add(value) || added;
        }
        return added;
    }

    public boolean addAll(K key, Set<E> value) {
        boolean added;
        if (key == null || value == null || value.isEmpty()) {
            added = false;
        } else {
            keyMapping.putIfAbsent(key, new HashSet<>());
            value.forEach(v -> valueMapping.putIfAbsent(v, new HashSet<>()));

            // Forces full execution of stream instead of using .anyMatch which will break on first hit.
            added = value.stream()
                    .map(v -> valueMapping.get(v).add(key))
                    .collect(Collectors.toSet())
                    .contains(true);
            added = keyMapping.get(key).addAll(value) || added;
        }
        return added;
    }

    public void remove(K key) {
        Set<E> values = keyMapping.remove(key);
        if (values != null) {
            values.stream()
                    .forEach(v -> valueMapping.get(v).remove(key));
        }
    }

    public void remove(K key, E value) {
        if (keyMapping.containsKey(key) && keyMapping.get(key).remove(value)) {
            valueMapping.get(value).remove(key);
        }
    }

    public void clear() {
        keyMapping.clear();
        valueMapping.clear();
    }

    public Set<K> keySet() {
        return unmodifiableSet(keyMapping.keySet());
    }

    public Set<E> values() {
        return unmodifiableSet(valueMapping.keySet());
    }
}