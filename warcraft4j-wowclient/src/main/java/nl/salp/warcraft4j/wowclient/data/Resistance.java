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

package nl.salp.warcraft4j.wowclient.data;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
@Dbc("Resistances.dbc")
public class Resistance {
    @DbcField(name = "id", column = 1, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(name = "flags", column = 2, dataType = DbcDataType.INT32)
    private int flags;
    @DbcField(name = "fizzleSoundId", column = 3, dataType = DbcDataType.INT32)
    private int fizzleSoundId;
    @DbcField(name = "sRefName", column = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;

    public Resistance() {
    }

    public Resistance(int id, int flags, int fizzleSoundId, String name) {
        this.id = id;
        this.flags = flags;
        this.fizzleSoundId = fizzleSoundId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getFlags() {
        return flags;
    }

    public int getFizzleSoundId() {
        return fizzleSoundId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
