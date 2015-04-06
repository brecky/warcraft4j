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
package nl.salp.warcraft4j.clientdatabase.entry;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.clientdatabase.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "SpellItemEnchantmentCondition.dbc")
public class SpellItemEnchantmentConditionEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.SPELL_ITEM_ENCHANTMENT_CONDITION;

    // Check http://pxr.dk/wowdev/wiki/index.php?title=SpellItemEnchantmentCondition.dbc for analysis on values.

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.BYTE, numberOfEntries = 5)
    private byte[] ltOperandTypes;
    @DbcField(order = 3, dataType = DbcDataType.BYTE, numberOfEntries = 3, padding = true)
    private transient byte[] padding1;
    @DbcField(order = 4, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] ltOperands;
    @DbcField(order = 5, dataType = DbcDataType.BYTE, numberOfEntries = 5)
    private byte[] operators;
    @DbcField(order = 6, dataType = DbcDataType.BYTE, numberOfEntries = 5)
    private byte[] rtOperandTypes;
    @DbcField(order = 7, dataType = DbcDataType.BYTE, numberOfEntries = 2, padding = true)
    private transient byte[] padding2;
    @DbcField(order = 8, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] rtOperands;
    @DbcField(order = 9, dataType = DbcDataType.BYTE, numberOfEntries = 5)
    private byte[] logic;
    @DbcField(order = 10, dataType = DbcDataType.BYTE, numberOfEntries = 3, padding = true)
    private transient byte[] padding3;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public byte[] getLtOperandTypes() {
        return ltOperandTypes;
    }

    public int[] getLtOperands() {
        return ltOperands;
    }

    public byte[] getOperators() {
        return operators;
    }

    public byte[] getRtOperandTypes() {
        return rtOperandTypes;
    }

    public int[] getRtOperands() {
        return rtOperands;
    }

    public byte[] getLogic() {
        return logic;
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