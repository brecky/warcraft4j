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
package nl.salp.warcraft4j.casc.guice;

import com.google.inject.AbstractModule;
import nl.salp.warcraft4j.casc.CascConfig;
import nl.salp.warcraft4j.casc.CascContext;
import nl.salp.warcraft4j.casc.DataReaderProvider;
import nl.salp.warcraft4j.casc.local.FileDataReaderProvider;
import nl.salp.warcraft4j.casc.local.LocalCascConfig;
import nl.salp.warcraft4j.casc.local.LocalCascContext;
import nl.salp.warcraft4j.config.W4jConfig;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class LocalCascModule extends AbstractModule {

    private final W4jConfig config;

    public LocalCascModule(W4jConfig config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(CascContext.class).to(LocalCascContext.class);
        bind(CascConfig.class).to(LocalCascConfig.class);
        bind(DataReaderProvider.class).to(FileDataReaderProvider.class);
    }
}
