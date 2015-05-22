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
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * {@link DbcMappingAnalysis} implementation for analysing string table entry coverage in mapped entries.
 *
 * @author Barre Dijkstra
 */
class StringTableCoverageDbcMappingAnalysis implements DbcMappingAnalysis<StringTableCoverageDbcMappingResult> {
    /** The mapping entry types to analyse. */
    private final Collection<? extends Class<? extends DbcEntry>> mappingTypes;

    /**
     * Create a new analysis instance for all mapping entry types on the classpath.
     */
    public StringTableCoverageDbcMappingAnalysis() {
        this.mappingTypes = DbcUtils.getAllClientDatabaseEntryMappings();
    }

    /**
     * Create a new analysis instance for a single mapping entry type.
     *
     * @param entry The mapping type.
     */
    public StringTableCoverageDbcMappingAnalysis(Class<? extends DbcEntry> entry) {
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
    public StringTableCoverageDbcMappingAnalysis(Collection<Class<? extends DbcEntry>> mappingTypes) {
        if (mappingTypes == null) {
            this.mappingTypes = Collections.emptySet();
        } else {
            this.mappingTypes = mappingTypes;
        }
    }

    @Override
    public StringTableCoverageDbcMappingResult analyse() {
        for (Class<? extends DbcEntry> mappingType : this.mappingTypes) {
            // FIXME Implement
        }
        return null;
    }
}
