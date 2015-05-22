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
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcMapping;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 * <p/>
 * Note: the format changed between 6.0.1 and 6.0.3, where 6.0.1 uses stringrefs for the values and 6.0.3 refers to TextureFileData.dbc
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "ItemDisplayInfo.dbc")
public class ItemDisplayInfoEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ITEM_DISPLAY_INFO;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE, numberOfEntries = 2)
    private String[] modelName;
    @DbcField(order = 4, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    @DbcReference(type = DbcType.TEXTURE_FILE_DATA) // Refers to column #2....
    private int[] modelTexture;
    @DbcField(order = 5, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] geosetGroup;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_VISUAL)
    private int spellVisualId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    @DbcReference(type = DbcType.HELMET_GEOSET_VISUAL_DATA)
    private int[] helmetGeosetVisualId;
    @DbcField(order = 9, dataType = DbcDataType.UINT32, numberOfEntries = 9)
    @DbcReference(type = DbcType.TEXTURE_FILE_DATA) // Refers to TextureFileData#textureItemId (TextureFileData.dbc, field #2)
    private int[] textures;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ITEM_VISUAL)
    private int itemVisualId;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.PARTICLE_COLOR)
    private int particleColorId;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public String[] getModelName() {
        return modelName;
    }

    public int[] getModelTexture() {
        return modelTexture;
    }

    public int[] getGeosetGroup() {
        return geosetGroup;
    }

    public int getFlags() {
        return flags;
    }

    public int getSpellVisualId() {
        return spellVisualId;
    }

    public int[] getHelmetGeosetVisualId() {
        return helmetGeosetVisualId;
    }

    public int[] getTextures() {
        return textures;
    }

    public int getItemVisualId() {
        return itemVisualId;
    }

    public int getParticleColorId() {
        return particleColorId;
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
