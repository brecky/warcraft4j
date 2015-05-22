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

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class StringTableCoverageDbcMappingResult {
    private final Class<? extends DbcEntry> mappingType;
    private final int entries;
    private final int entriesWithStringTableEntry;
    private final int fields;
    private final int stringTableReferenceFields;
    private final int stringTableEntries;
    private final int usedStringTableEntries;

    public StringTableCoverageDbcMappingResult(Class<? extends DbcEntry> mappingType, int entries, int entriesWithStringTableEntry, int fields, int stringTableReferenceFields, int stringTableEntries, int usedStringTableEntries) {
        this.mappingType = mappingType;
        this.entries = entries;
        this.entriesWithStringTableEntry = entriesWithStringTableEntry;
        this.fields = fields;
        this.stringTableReferenceFields = stringTableReferenceFields;
        this.stringTableEntries = stringTableEntries;
        this.usedStringTableEntries = usedStringTableEntries;
    }

    public Class<? extends DbcEntry> getMappingType() {
        return mappingType;
    }

    public int getEntries() {
        return entries;
    }

    public int getFields() {
        return fields;
    }

    public int getStringTableReferenceFields() {
        return stringTableReferenceFields;
    }

    public String getMappedFile() {
        return DbcUtils.getMappedFile(mappingType);
    }

    public int getStringTableEntries() {
        return stringTableEntries;
    }

    public int getUsedStringTableEntries() {
        return usedStringTableEntries;
    }

    public float getFieldsReferencingStringTablePercentage() {
        return (((float) stringTableReferenceFields) / fields) * 100;
    }

    /**
     * Calculate the percentage of how many entries have a string table entry.
     *
     * @return The percentage.
     */
    public float getStringTableEntryPercentage() {
        return (((float) stringTableEntries) / entries) * 100;
    }
}
