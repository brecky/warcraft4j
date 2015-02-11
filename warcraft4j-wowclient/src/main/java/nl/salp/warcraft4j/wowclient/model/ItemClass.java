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

package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.databaseclient.Dbc;
import nl.salp.warcraft4j.wowclient.databaseclient.datatype.DbcDataTypes;
import nl.salp.warcraft4j.wowclient.databaseclient.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
@Dbc("ItemClass.dbc")
public class ItemClass {
    @DbcField(name = "classId", column = 1, dataType = DbcDataTypes.INT32)
    private int id;
    @DbcField(name = "flags", column = 2, dataType = DbcDataTypes.INT32)
    private int flags;
    @DbcField(name = "priceModifier", column = 3, dataType = DbcDataTypes.FLOAT)
    private float priceModifier;
    @DbcField(name = "name", column = 4, length = 4, dataType = DbcDataTypes.STRING_REFERENCE)
    private String name;

    public ItemClass() {
    }

    public ItemClass(int id, int flags, float priceModifier, String name) {
        this.id = id;
        this.flags = flags;
        this.priceModifier = priceModifier;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getFlags() {
        return flags;
    }

    public float getPriceModifier() {
        return priceModifier;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
