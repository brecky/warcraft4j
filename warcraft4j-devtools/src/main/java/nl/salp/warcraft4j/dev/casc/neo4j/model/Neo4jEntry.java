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
package nl.salp.warcraft4j.dev.casc.neo4j.model;

import nl.salp.warcraft4j.DataTypeUtil;
import nl.salp.warcraft4j.dev.casc.neo4j.ReferenceEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.Collection;
import java.util.HashSet;
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
            throw new IllegalArgumentException("Can't create a Neo4jEntry for a null node.");
        }
        if (!node.hasLabel(label)) {
            throw new IllegalArgumentException("Can't create a Neo4jEntry for a node without the node label.");
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

    protected final Object remove(CascProperty property) {
        return getNode().removeProperty(property.getName());
    }

    protected final Optional<Object> get(CascProperty property) {
        return node()
                .filter(n -> n.hasProperty(property.getName()))
                .map(n -> n.getProperty(property.getName()));
    }

    protected final <T> Optional<T> get(CascProperty property, Function<Object, T> mapper) {
        return node()
                .filter(n -> n.hasProperty(property.getName()))
                .map(n -> n.getProperty(property.getName()))
                .map(mapper);
    }

    protected final void set(CascProperty property, Object object) {
        if (object == null) {
            remove(property);
        } else {
            getNode().setProperty(property.getName(), object);
        }
    }

    protected final <T> void set(CascProperty property, T instance, Function<T, Object> mapper) {
        Optional<T> value = Optional.ofNullable(instance);
        value.map(mapper)
                .ifPresent(val -> set(property, val));
        if (!value.isPresent()) {
            remove(property);
        }
    }

    protected final Optional<Long> getLong(CascProperty property) {
        return get(property, Long.class::cast);
    }

    protected final void setLong(CascProperty property, long value) {
        set(property, value);
    }

    protected final Optional<Boolean> getBoolean(CascProperty property) {
        return get(property, Boolean.class::cast);
    }

    protected final void setBoolean(CascProperty property, boolean value) {
        set(property, value);
    }

    protected final Optional<Integer> getInt(CascProperty property) {
        return get(property, Integer.class::cast);
    }

    protected final void setInt(CascProperty property, int value) {
        set(property, value);
    }

    protected final Optional<String> getString(CascProperty property) {
        return get(property, String.class::cast);
    }

    protected final void setString(CascProperty property, String value) {
        set(property, value);
    }

    protected final Optional<byte[]> getByteArray(CascProperty property) {
        return get(property, String.class::cast)
                .map(DataTypeUtil::hexStringToByteArray);
    }

    protected final void setByteArray(CascProperty property, byte[] data) {
        set(property, data, DataTypeUtil::byteArrayToHexString);
    }

    protected final <T> Collection<T> getReferencedEntries(Function<Node, T> mapper, Direction direction, ReferenceEvaluator evaluator, GraphDatabaseService service) {
        Traverser traverser = service.traversalDescription()
                .breadthFirst()
                .relationships(CascRelationship.REFERENCES, direction)
                .evaluator(evaluator)
                .uniqueness(Uniqueness.NODE_PATH).traverse(getNode());
        Collection<T> entries = new HashSet<>();
        traverser.nodes().forEach(node -> entries.add(mapper.apply(node)));
        return entries;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o != null && Neo4jCascEntry.class.isAssignableFrom(o.getClass()) && node.equals(((Neo4jEntry) o).node);
    }
}