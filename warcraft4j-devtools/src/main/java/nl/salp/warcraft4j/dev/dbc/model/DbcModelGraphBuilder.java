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

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcReference;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DbcModelGraphBuilder {
    private final Set<Class<? extends DbcEntry>> entryTypes;
    private final Map<DbcType, Class<? extends DbcEntry>> typeMappings;

    public DbcModelGraphBuilder() {
        this.entryTypes = new HashSet<>();
        this.typeMappings = new HashMap<>();
    }

    public void add(Class<? extends DbcEntry> entryType) {
        if (entryType != null) {
            this.entryTypes.add(entryType);
            typeMappings.put(DbcUtils.getEntryType(entryType), entryType);
        }
    }

    public void add(Collection<Class<? extends DbcEntry>> entryTypes) {
        this.entryTypes.addAll(entryTypes);
        typeMappings.putAll(
                entryTypes.stream()
                        .filter(entryType -> entryType != null)
                        .filter(entryType -> DbcUtils.getEntryType(entryType) != null)
                        .collect(Collectors.toMap(
                                DbcUtils::getEntryType,
                                entryType -> entryType
                        ))
        );
    }

    public Graph<Class<? extends DbcEntry>, DbcType> build() {
        Graph<Class<? extends DbcEntry>, DbcType> graph = new Graph<>(DbcUtils::getEntryType);
        graph.addAll(entryTypes);
        entryTypes.forEach(source -> findEdges(source).forEach(target -> graph.relate(source, target)));
        return graph;
    }

    private Set<Class<? extends DbcEntry>> findEdges(Class<? extends DbcEntry> entryType) {
        return DbcUtils.getMappedFields(entryType).stream()
                .filter(field -> field.isAnnotationPresent(DbcReference.class))
                .map(field -> field.getAnnotation(DbcReference.class).type())
                .map(typeMappings::get)
                .collect(Collectors.toSet());
    }
}
