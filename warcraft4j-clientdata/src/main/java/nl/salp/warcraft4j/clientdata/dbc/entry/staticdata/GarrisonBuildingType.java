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
public enum GarrisonBuildingType {
    UNKNOWN(0),
    MINES(1),
    HERB_GARDEN(2),
    BARN(3),
    LUMBER_MILL(4),
    INN(5),
    TRADING_POST(6),
    MENAGERIE(7),
    BARRACKS(8),

    WAR_MILL(10),
    STABLES(11),

    SPIRIT_LODGE(13),
    SALVAGE_YARD(14),
    STOREHOUSE(15),
    ALCHEMY(16),
    BLACKSMITHING(17),
    ENCHANTING(18),
    ENGINEERING(19),
    INSCRIPTION(20),
    JEWELCRAFTING(21),
    LEATHERWORKING(22),
    TAILORING(23),
    FISHING(24),
    GLADIATOR_SANCTUM(25),
    WORKSHOP(26);

    GarrisonBuildingType(int id) {

    }
}
