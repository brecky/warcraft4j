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
package nl.salp.warcraft4j.neo4j;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Optional;
import java.util.function.Function;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public abstract class Neo4jEntry {
    private final Node node;
    private final Label label;

    protected Neo4jEntry(Node node, Label label) throws IllegalArgumentException {
        this.node = node;
        this.label = label;
        if (node == null) {
            throw new IllegalArgumentException("Can't create a W4jNeo4jEntry for a null node.");
        }
        if (!node.hasLabel(label)) {
            throw new IllegalArgumentException("Can't create a W4jNeo4jEntry for a node without the node label.");
        }
    }

    protected final Optional<Node> node() {
        return Optional.of(node);
    }

    public final Label getLabel() {
        return label;
    }

    public final Node getNode() {
        return node;
    }

    protected final Object remove(String property) {
        return getNode().removeProperty(property);
    }

    protected final Optional<Object> get(String property) {
        return node()
                .filter(n -> n.hasProperty(property))
                .map(n -> n.getProperty(property));
    }

    protected final <T> Optional<T> get(String property, Function<Object, T> mapper) {
        return node()
                .filter(n -> n.hasProperty(property))
                .map(n -> n.getProperty(property))
                .map(mapper);
    }

    protected final void set(String property, Object object) {
        if (object == null) {
            remove(property);
        } else {
            getNode().setProperty(property, object);
        }
    }

    protected final <T> void set(String property, T instance, Function<T, Object> mapper) {
        Optional<T> value = Optional.ofNullable(instance);
        value.map(mapper)
                .ifPresent(val -> set(property, val));
        if (!value.isPresent()) {
            remove(property);
        }
    }

    protected final Optional<Long> getLong(String property) {
        return get(property, Long.class::cast);
    }

    protected final void setLong(String property, long value) {
        set(property, value);
    }

    protected final Optional<Boolean> getBoolean(String property) {
        return get(property, Boolean.class::cast);
    }

    protected final void setBoolean(String property, boolean value) {
        set(property, value);
    }

    protected final Optional<Integer> getInt(String property) {
        return get(property, Integer.class::cast);
    }

    protected final void setInt(String property, int value) {
        set(property, value);
    }

    protected final Optional<String> getString(String property) {
        return get(property, String.class::cast);
    }

    protected final void setString(String property, String value) {
        set(property, value);
    }

    protected final Optional<byte[]> getByteArray(String property) {
        return get(property, byte[].class::cast);
    }

    protected final void setByteArray(String property, byte[] data) {
        set(property, data);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o != null && Neo4jEntry.class.isAssignableFrom(o.getClass()) && node.equals(((Neo4jEntry) o).node);
    }
}