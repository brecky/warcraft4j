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
package nl.salp.warcraft4j.data.dbc.entry;

import nl.salp.warcraft4j.data.dbc.DbcEntry;
import nl.salp.warcraft4j.data.dbc.DbcType;
import nl.salp.warcraft4j.data.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.data.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.data.dbc.mapping.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 * <p/>
 * <pre>Field Descriptions
 * <p/>
 * The meaning of some of the fields change depending on the GeneralType value.
 * /	0 - Base Skin	1 - Face	2 - Facial Hair	3 - Hair	4 - Underwear
 * Type	-	FaceType	FacialHairType	HairStyle	-
 * Color	SkinColor	SkinColor	HairColor	HairColor	SkinColor
 * Texture1	SkinTexture	FaceLowerTexture	FacialLowerTexture	HairTexture	PelvisTexture
 * Texture2	ExtraSkinTexture	FaceUpperTexture	FacialUpperTexture	ScalpLowerTexture	TorsoTexture
 * Texture3	-	-	-	ScalpUpperTexture	-
 * Flags
 * <p/>
 * Flag	Description
 * 0x1	Playable (probably)
 * 0x2
 * 0x4	Death Knight texture
 * 0x8	NPC skin (e.g. the necromancer human skins with facial marking etc)
 * 0x10</pre>
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "CharSections.dbc")
public class CharacterSectionEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CHARACTER_SECTION;

    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    private int raceId;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int genderId;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int baseSection;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE, numberOfEntries = 3)
    private String[] textures;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int variationIndex;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    private int colorIndex;


    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getRaceId() {
        return raceId;
    }

    public int getGenderId() {
        return genderId;
    }

    public int getBaseSection() {
        return baseSection;
    }

    public String[] getTextures() {
        return textures;
    }

    public int getFlags() {
        return flags;
    }

    public int getVariationIndex() {
        return variationIndex;
    }

    public int getColorIndex() {
        return colorIndex;
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