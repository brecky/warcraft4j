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
package nl.salp.warcraft4j.casc.cdn.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import nl.salp.warcraft4j.casc.CascService;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.cdn.CdnCascService;
import nl.salp.warcraft4j.casc.cdn.local.LocalCdnCascContext;
import nl.salp.warcraft4j.casc.cdn.online.OnlineCdnCascContext;
import nl.salp.warcraft4j.config.Warcraft4jConfig;

/**
 * Guice module that binds the the CDN implementation to the CASC module, being either online or local depending on the Warcraft4J configuration.
 * <p>
 * Requires a bound {@link Warcraft4jConfig}.
 *
 * @author Barre Dijkstra
 */
public class CdnCascModule extends AbstractModule {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        requireBinding(Warcraft4jConfig.class);
        final Provider<Warcraft4jConfig> configProvider = getProvider(Warcraft4jConfig.class);
        bind(CdnCascContext.class).toProvider(new Provider<CdnCascContext>() {
            @Override
            public CdnCascContext get() {
                Warcraft4jConfig config = configProvider.get();
                if (config.isOnline()) {
                    return new OnlineCdnCascContext(config);
                } else {
                    return new LocalCdnCascContext(config);
                }
            }
        });
        bind(CascService.class).to(CdnCascService.class);
    }
}