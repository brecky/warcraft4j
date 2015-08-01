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
package nl.salp.warcraft4j.clientdata.casc;

import nl.salp.warcraft4j.clientdata.Branch;
import nl.salp.warcraft4j.clientdata.Locale;
import nl.salp.warcraft4j.clientdata.Region;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public abstract class UpdatableCascContext implements CascContext {
    private final CascConfig config;
    private final Branch branch;
    private final Region region;
    private final Locale locale;
    private final String version;

    protected UpdatableCascContext(CascConfig config, Branch branch, Region region, Locale locale) {
        this.config = config;
        this.branch = branch;
        this.region = region;
        this.locale = locale;
        this.version = config.getVersion();
    }

    protected abstract DataReaderProvider getDataReaderProvider();

    protected final CascConfig getConfig() {
        return config;
    }

    @Override
    public final Branch getBranch() {
        return branch;
    }

    @Override
    public final Locale getLocale() {
        return locale;
    }

    @Override
    public final Region getRegion() {
        return region;
    }

    @Override
    public String getVersion() {
        return version;
    }
}
