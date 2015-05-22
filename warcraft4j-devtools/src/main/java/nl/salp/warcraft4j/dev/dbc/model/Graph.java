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
package nl.salp.warcraft4j.dev.dbc.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Graph<V, K> {
    private Map<K, V> vertexes;
    private Map<K, Set<K>> edges;
    private Function<V, K> keyRetriever;

    public Graph(Function<V, K> keyRetriever) {
        this.vertexes = new HashMap<>();
        this.edges = new HashMap<>();
        this.keyRetriever = keyRetriever;
    }

    public void addAll(Collection<V> vertexes) {
        vertexes.forEach(this::addVertex);
    }

    public void relate(V source, V target) {
        addVertex(source);
        addVertex(target);
        addEdge(getKey(source), getKey(target));
    }

    public void relateAll(Map<V, V> relations) {
        relations.entrySet().forEach(entry -> relate(entry.getKey(), entry.getValue()));
    }

    public Collection<V> getVertexes() {
        return vertexes.values();
    }

    public Set<V> getLeafs() {
        return vertexes.keySet().stream()
                .filter(this::hasIncomingEdge)
                .filter(key -> !hasOutgoingEdge(key))
                .map(this::getVertex)
                .collect(Collectors.toSet());
    }

    public Set<V> getTrunks() {
        return vertexes.keySet().stream()
                .filter(this::hasOutgoingEdge)
                .filter(key -> !hasIncomingEdge(key))
                .map(this::getVertex)
                .collect(Collectors.toSet());
    }

    public Set<V> getOrphans() {
        return vertexes.keySet().stream()
                .filter(key -> !hasOutgoingEdge(key))
                .filter(key -> !hasIncomingEdge(key))
                .map(this::getVertex)
                .collect(Collectors.toSet());
    }

    public Set<V> getEdges(V source) {
        return getEdgeKeys(source).stream()
                .map(this::getVertex)
                .collect(Collectors.toSet());
    }

    private boolean hasOutgoingEdge(K key) {
        return edges.containsKey(key);
    }

    private boolean hasIncomingEdge(K key) {
        return edges.values().parallelStream()
                .anyMatch(edges -> edges.contains(key));
    }

    private void addEdge(K source, K target) {
        if (source != null && target != null) {
            Set<K> edges = this.edges.getOrDefault(source, new HashSet<>());
            edges.add(target);
            this.edges.put(source, edges);
        }
    }

    private void addVertex(V vertex) {
        if (vertex != null && !vertexes.containsKey(vertex)) {
            vertexes.put(getKey(vertex), vertex);
        }
    }

    private Set<K> getEdgeKeys(V vertex) {
        return edges.getOrDefault(getKey(vertex), Collections.emptySet());
    }

    private Set<K> getVertexKeys() {
        return vertexes.keySet();
    }

    private K getKey(V vertex) {
        return keyRetriever.apply(vertex);
    }

    private V getVertex(K key) {
        return vertexes.get(key);
    }
}
