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

package nl.salp.warcraft4j.files.clientdatabase.entry;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "Item.db2")
public class ItemEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.ITEM;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int itemClassId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int subClassId;
    @DbcField(order = 4, dataType = DbcDataType.INT32)
    private int soundOverrideSubclassId;
    @DbcField(order = 5, dataType = DbcDataType.INT32)
    private int materialId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int inventoryType;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int sheath;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int fileDataId;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int groupSoundsId;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getItemClassId() {
        return itemClassId;
    }

    public int getSubClassId() {
        return subClassId;
    }

    public int getSoundOverrideSubclassId() {
        return soundOverrideSubclassId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getInventoryType() {
        return inventoryType;
    }

    public int getSheath() {
        return sheath;
    }

    public int getFileDataId() {
        return fileDataId;
    }

    public int getGroupSoundsId() {
        return groupSoundsId;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
