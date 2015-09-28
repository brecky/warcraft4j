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
package nl.salp.warcraft4j.model.spell;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.salp.warcraft4j.casc.CascService;
import nl.salp.warcraft4j.dataparser.dbc.DbcEntry;
import nl.salp.warcraft4j.dataparser.dbc.DbcFile;
import nl.salp.warcraft4j.dataparser.dbc.DbcParsingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.model.DbcMapping;
import nl.salp.warcraft4j.model.DbcMappingException;

import java.util.Optional;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@Singleton
public class DbcSpellMapping extends DbcMapping<Spell> {
    private static final String DBC_FILENAME_SPELL = "DBFilesClient\\Spell.dbc";
    private static final String DBC_FILENAME_DESCVAR = "DBFilesClient\\SpellDescriptionVariables.dbc";
    private static final int SPELL_IDX_NAME = 4;
    private static final int SPELL_IDX_SUBTEXT = 8;
    private static final int SPELL_IDX_DESCRIPTION = 12;
    private static final int SPELL_IDX_AURA_DESCRIPTION = 16;
    private static final int SPELL_IDX_RUNE_COST_ID = 20;
    private static final int SPELL_IDX_MISSILE_ID = 24;
    private static final int SPELL_IDX_DESCRIPTION_VARIABLE_ID = 28;
    private static final int SPELL_IDX_SCALING_ID = 32;
    private static final int SPELL_IDX_AURA_OPTION_ID = 36;
    private static final int SPELL_IDX_AURA_RESTRICTION_ID = 40;
    private static final int SPELL_IDX_CASTING_REQUIREMENT_ID = 44;
    private static final int SPELL_IDX_CATEGORY_ID = 48;
    private static final int SPELL_IDX_CLASS_OPTION_ID = 52;
    private static final int SPELL_IDX_COOLDOWN_ID = 56;
    private static final int SPELL_IDX_EQUIPPED_ITEM_ID = 60;
    private static final int SPELL_IDX_INTERRUPT_ID = 64;
    private static final int SPELL_IDX_LEVEL_ID = 68;
    private static final int SPELL_IDX_REAGENT_ID = 72;
    private static final int SPELL_IDX_SHAPESHIFT_ID = 76;
    private static final int SPELL_IDX_TARGET_RESTRICTION_ID = 80;
    private static final int SPELL_IDX_TOTEM_ID = 84;
    private static final int SPELL_IDX_REQUIRED_PROJECT_ID = 88;
    private static final int SPELL_IDX_MISC_ID = 92;
    private static final int DESCVAR_IDX_VARIABLE = 4;

    private transient Optional<DbcFile> descriptionVariables;

    @Inject
    public DbcSpellMapping(CascService cascService) {
        super(cascService);
    }

    @Override
    protected String getFilename() {
        return DBC_FILENAME_SPELL;
    }

    @Override
    protected Spell parse(DbcEntry entry) throws DbcMappingException {
        int id = entry.getId();
        String name = getName(entry);
        String subText = getSubText(entry);
        String description = getDescription(entry);
        String auraDescription = getAuraDescription(entry);
        String descriptionVariable = getDescriptionVariable(entry);
        return new Spell(id, name, subText, description, auraDescription, descriptionVariable);
    }

    private String getName(DbcEntry entry) throws DbcParsingException {
        return entry.getStringTableValue(SPELL_IDX_NAME, getStringTable())
                .orElse(null);
    }

    private String getSubText(DbcEntry entry) throws DbcParsingException {
        return entry.getStringTableValue(SPELL_IDX_SUBTEXT, getStringTable())
                .orElse(null);
    }

    private String getDescription(DbcEntry entry) throws DbcParsingException {
        return entry.getStringTableValue(SPELL_IDX_DESCRIPTION, getStringTable())
                .orElse(null);
    }

    private String getAuraDescription(DbcEntry entry) throws DbcParsingException {
        return entry.getStringTableValue(SPELL_IDX_AURA_DESCRIPTION, getStringTable())
                .orElse(null);
    }

    private String getDescriptionVariable(DbcEntry entry) throws DbcParsingException, DbcMappingException {
        if (descriptionVariables == null) {
            descriptionVariables = getDbcFile(DBC_FILENAME_DESCVAR);
        }
        int id = entry.getValue(SPELL_IDX_DESCRIPTION_VARIABLE_ID, DataTypeFactory.getInteger());
        return descriptionVariables
                .flatMap(file -> file.getEntryWithId(id))
                .flatMap(e -> e.getStringTableValue(DESCVAR_IDX_VARIABLE, descriptionVariables.get().getStringTable()))
                .orElse(String.valueOf(id));
    }
}
