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
import nl.salp.warcraft4j.battlenet.BattlenetApiConfig;
import nl.salp.warcraft4j.battlenet.api.BattlenetApi;
import nl.salp.warcraft4j.battlenet.api.JacksonJsonApiResultParser;
import nl.salp.warcraft4j.battlenet.api.wow.WowBattlenetApi;
import nl.salp.warcraft4j.battlenet.api.wow.WowBattlenetApiImpl;
import nl.salp.warcraft4j.battlenet.api.BattlenetHttpApi;
import nl.salp.warcraft4j.battlenet.api.JsonApiResultParser;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Guice module for the Battle.NET API dependencies.
 *
 * @author Barre Dijkstra
 */
public class BattlenetApiModule extends AbstractModule {
    /** The location of the Battle.NET API config file. */
    private final String configFile;


    /**
     * Create a new Guice Battle.NET API module instance using the default config file location.
     */
    public BattlenetApiModule() {
        this.configFile = null;
    }

    /**
     * Create a new Guice Battle.NET API module instance.
     *
     * @param configFile The location of the Battle.NET API config file.
     */
    public BattlenetApiModule(String configFile) {
        this.configFile = configFile;
    }

    @Override
    protected void configure() {
        if (isEmpty(configFile)) {
            bind(BattlenetApiConfig.class).toProvider(BattlenetApiConfigFileProvider.class);
        } else {
            bind(BattlenetApiConfig.class).toProvider(new BattlenetApiConfigFileProvider(configFile));
        }
        bind(BattlenetApi.class).to(BattlenetHttpApi.class);
        bind(JsonApiResultParser.class).to(JacksonJsonApiResultParser.class);
        bind(WowBattlenetApi.class).to(WowBattlenetApiImpl.class);
    }
}
