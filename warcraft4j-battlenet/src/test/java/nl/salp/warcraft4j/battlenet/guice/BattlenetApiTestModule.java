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

package nl.salp.warcraft4j.battlenet.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import nl.salp.warcraft4j.battlenet.BattlenetApiConfig;
import nl.salp.warcraft4j.battlenet.api.BattlenetApi;
import nl.salp.warcraft4j.battlenet.api.BattlenetFileApi;
import nl.salp.warcraft4j.battlenet.api.JacksonJsonApiResultParser;
import nl.salp.warcraft4j.battlenet.api.wow.WowBattlenetApi;
import nl.salp.warcraft4j.battlenet.api.wow.WowBattlenetApiImpl;
import nl.salp.warcraft4j.battlenet.api.JsonApiResultParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Guice module with JSON file bindings.
 *
 * @author Barre Dijkstra
 */
public class BattlenetApiTestModule extends AbstractModule {
    /** The location of the Battle.NET API config file. */
    private final String configFile;

    /**
     * Create a new Guice Battle.NET API module instance using the default config file location.
     */
    public BattlenetApiTestModule() {
        this.configFile = null;
    }

    /**
     * Create a new Guice Battle.NET API module instance.
     *
     * @param configFile The location of the Battle.NET API config file.
     */
    public BattlenetApiTestModule(String configFile) {
        this.configFile = configFile;
    }

    @Override
    protected void configure() {
        bind(BattlenetApiConfig.class).toProvider(new BattlenetApiConfigFileProvider(this.configFile));
        bind(JsonApiResultParser.class).toInstance(new JacksonJsonApiResultParser(true));
        bind(WowBattlenetApi.class).to(WowBattlenetApiImpl.class);
        bind(BattlenetApi.class).toProvider(new FileApiProvider());
    }

    /**
     * Provider for {@link BattlenetFileApi} that maps all methods to test JSON files.
     */
    private static class FileApiProvider implements Provider<BattlenetFileApi> {
        @Override
        public BattlenetFileApi get() {
            Map<String, String> jsonFiles = new HashMap<>();
            jsonFiles.put("/wow/character", "/json/character/character_all.json");
            jsonFiles.put("/wow/item", "/json/item/item-finkles_lava_dredger.json");
            jsonFiles.put("/wow/item/set", "/json/item/itemset-deep_earth_vestments.json");
            return new BattlenetFileApi(jsonFiles);
        }
    }
}