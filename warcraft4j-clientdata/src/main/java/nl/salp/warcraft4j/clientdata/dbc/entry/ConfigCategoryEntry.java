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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "Cfg_Categories.db2")
public class ConfigCategoryEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CONFIG_CATEGORY;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int localeMask; // 205: EU, 256: Russia, 0: rest.
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int createCharsetMask; // 0: Development, 1: US and EU, 4: Russia, 10: Korea, 17: Taiwan & China
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int existingCharsetMask; // 0: Development, 1: US and EU, 4: Russia, 10: Korea, 17: Taiwan & China
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int flags; // 1: tournaments enabled on account
    @DbcField(order = 6, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getLocaleMask() {
        return localeMask;
    }

    public int getCreateCharsetMask() {
        return createCharsetMask;
    }

    public int getExistingCharsetMask() {
        return existingCharsetMask;
    }

    public int getFlags() {
        return flags;
    }

    public String getName() {
        return name;
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
