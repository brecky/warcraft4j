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
package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcMapping;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "ItemSubClass.dbc")
public class ItemSubClassEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ITEM_SUB_CLASS;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ITEM_CLASS)
    private int itemClassId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ITEM_SUB_CLASS)
    private int subClassId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int prerequisiteProficiencyId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int postrequisiteProficiencyId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int displayFlags;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int weaponParrySequenceId;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int weaponReadySequenceId;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int weaponAttackSequenceId;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int weaponSwingSize;
    @DbcField(order = 12, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String displayName;
    @DbcField(order = 13, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String verboseName;

    @Override
    public DbcType getEntryType() {
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

    public int getPrerequisiteProficiencyId() {
        return prerequisiteProficiencyId;
    }

    public int getPostrequisiteProficiencyId() {
        return postrequisiteProficiencyId;
    }

    public int getFlags() {
        return flags;
    }

    public int getDisplayFlags() {
        return displayFlags;
    }

    public int getWeaponParrySequenceId() {
        return weaponParrySequenceId;
    }

    public int getWeaponReadySequenceId() {
        return weaponReadySequenceId;
    }

    public int getWeaponAttackSequenceId() {
        return weaponAttackSequenceId;
    }

    public int getWeaponSwingSize() {
        return weaponSwingSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getVerboseName() {
        return verboseName;
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