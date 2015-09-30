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
import com.google.inject.Provider;
import nl.salp.warcraft4j.casc.CascContext;
import nl.salp.warcraft4j.casc.CascService;
import nl.salp.warcraft4j.casc.CdnCascService;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.local.LocalCascContext;
import nl.salp.warcraft4j.config.Warcraft4jConfig;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CdnCascModule extends AbstractModule {
    @Override
    protected void configure() {
        requireBinding(Warcraft4jConfig.class);
        final Provider<Warcraft4jConfig> configProvider = getProvider(Warcraft4jConfig.class);
        bind(CascContext.class).toProvider(new Provider<CascContext>() {
            @Override
            public CascContext get() {
                Warcraft4jConfig config = configProvider.get();
                if (config.isOnline()) {
                    return new CdnCascContext(config);
                } else {
                    return new LocalCascContext(config);
                }
            }
        });
        bind(CascService.class).to(CdnCascService.class);
    }
}