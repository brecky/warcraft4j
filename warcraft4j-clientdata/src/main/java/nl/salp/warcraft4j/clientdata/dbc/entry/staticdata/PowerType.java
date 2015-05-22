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
package nl.salp.warcraft4j.clientdata.dbc.entry.staticdata;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public enum PowerType {
    MANA("mana", 0),
    RAGE("rage", 1),
    FOCUS("focus", 2),
    RUNIC_POWER("runic_power", 6),
    ENERGY("energy", 3);

    private final String name;
    private final int id;

    PowerType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static PowerType getPowerType(int id) {
        PowerType powerType = null;
        for (PowerType pt : PowerType.values()) {
            if (pt.id == id) {
                powerType = pt;
                break;
            }
        }
        return powerType;
    }
}
