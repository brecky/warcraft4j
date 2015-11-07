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
package nl.salp.warcraft4j.dev.casc.dbc;

import nl.salp.warcraft4j.fileformat.dbc.DbcEntry;
import nl.salp.warcraft4j.fileformat.dbc.DbcStringTable;
import nl.salp.warcraft4j.io.datatype.DataType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class Entry {
    private final int id;
    private final DbcEntry entry;
    private final WeakReference<DbcStringTable> stringTable;
    private List<Field> fields;

    public Entry(DbcEntry entry, DbcStringTable stringTable) {
        this.entry = entry;
        this.id = entry.getId();
        this.stringTable = new WeakReference<>(stringTable);
        this.fields = new ArrayList<>(fields);
    }

    public int getId() {
        return id;
    }

    public int getFieldCount() {
        return entry.getFieldCount();
    }

    public int getSize() {
        return entry.getEntrySize();
    }

    public <T> T getValue(int offset, DataType<T> dataType) {
        return entry.getValue(offset, dataType);
    }

    public Optional<String> getStringTableValue(int offset) {
        return entry.getStringTableValue(offset, getStringTable());
    }

    private DbcStringTable getStringTable() {
        return stringTable.get();
    }
}
