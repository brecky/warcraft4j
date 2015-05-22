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
package nl.salp.warcraft4j.dev.dbc.analysis;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * {@link DbcMappingAnalysis} implementation for finding mapped fields that having an unknown value.
 *
 * @author Barre Dijkstra
 */
class UnknownDbcMappingAnalysis implements DbcMappingAnalysis<Collection<UnknownDbcMappingResult>> {
    /** The mapping entry types to analyse. */
    private final Collection<? extends Class<? extends DbcEntry>> mappingTypes;

    /**
     * Create a new analysis instance for all mapping entry types on the classpath.
     */
    public UnknownDbcMappingAnalysis() {
        this.mappingTypes = DbcUtils.getAllClientDatabaseEntryMappings();
    }

    /**
     * Create a new analysis instance for a single mapping entry type.
     *
     * @param entry The mapping type.
     */
    public UnknownDbcMappingAnalysis(Class<? extends DbcEntry> entry) {
        if (entry == null) {
            this.mappingTypes = Collections.emptySet();
        } else {
            this.mappingTypes = Collections.singleton(entry);
        }
    }

    /**
     * Create a new instance for a provided set of mapping entry types.
     *
     * @param mappingTypes The mapping types.
     */
    public UnknownDbcMappingAnalysis(Collection<Class<? extends DbcEntry>> mappingTypes) {
        if (mappingTypes == null) {
            this.mappingTypes = Collections.emptySet();
        } else {
            this.mappingTypes = mappingTypes;
        }
    }

    @Override
    public Collection<UnknownDbcMappingResult> analyse() throws IOException {
        Set<UnknownDbcMappingResult> unknownMappings = new HashSet<>();
        for (Class<? extends DbcEntry> entry : mappingTypes) {
            Collection<Field> fields = DbcUtils.getMappedFields(entry);
            Set<Field> unknownFields = new TreeSet<>(DbcUtils.getFieldOrderComparator());
            for (Field field : fields) {
                DbcField f = field.getAnnotation(DbcField.class);
                if (f != null && !f.knownMeaning()) {
                    unknownFields.add(field);
                }
            }
            if (!unknownFields.isEmpty()) {
                unknownMappings.add(new UnknownDbcMappingResult(entry, fields.size(), unknownFields));
            }
        }
        return unknownMappings;
    }
}
