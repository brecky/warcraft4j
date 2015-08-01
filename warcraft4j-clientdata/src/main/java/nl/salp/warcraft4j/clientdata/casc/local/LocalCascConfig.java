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
package nl.salp.warcraft4j.clientdata.casc.local;

import nl.salp.warcraft4j.clientdata.ClientDataConfiguration;
import nl.salp.warcraft4j.clientdata.casc.*;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.file.FileDataReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class LocalCascConfig extends BaseCascConfig<Path> implements CascConfig {
    private static final String FILENAME_BUILDINFO = ".build.info";
    private static final String KEY_BUILDINFO_ACTIVE = "Active";
    private static final String KEY_BUILDINFO_ARMADILLO = "Armadillo";
    private static final String KEY_BUILDINFO_BUILD = "Build Key";
    private static final String KEY_BUILDINFO_BRANCH = "Branch";
    private static final String KEY_BUILDINFO_CDN = "CDN Key";
    private static final String KEY_BUILDINFO_CDN_HOSTS = "CDN Hosts";
    private static final String KEY_BUILDINFO_CDN_PATH = "CDN Path";
    private static final String KEY_BUILDINFO_IM_SIZE = "IM Size";
    private static final String KEY_BUILDINFO_INSTALL = "Install Key";
    private static final String KEY_BUILDINFO_LAST_ACTIVATION = "Last Activated";
    private static final String KEY_BUILDINFO_REGIONS = "Branch";
    private static final String KEY_BUILDINFO_TAGS = "Tags";
    private static final String KEY_BUILDINFO_VERSION = "Version";

    private Config buildInfo;

    public LocalCascConfig(ClientDataConfiguration clientDataConfiguration, DataReaderProvider<Path> dataReaderProvider) {
        super(clientDataConfiguration, dataReaderProvider);
    }

    private Config getBuildInfo() {
        if (buildInfo == null) {
            buildInfo = Config.tableConfig(getDataReader(getClientDataConfiguration().getWowInstallationDirectory().resolve(FILENAME_BUILDINFO)));
        }
        return buildInfo;
    }

    private Optional<String> getBuildInfoValue(String key) {
        return getIndexedValue(getBuildInfo(), key, KEY_BUILDINFO_BRANCH, getRegionCode());
    }

    @Override
    protected Optional<String> getBuildConfigKey() {
        return getBuildInfoValue(KEY_BUILDINFO_BUILD);
    }

    @Override
    protected Optional<String> getCdnConfigKey() {
        return getBuildInfoValue(KEY_BUILDINFO_CDN);
    }

    @Override
    protected Supplier<DataReader> getConfigDataReader(String checksum) {
        Path path = getClientDataConfiguration().getWowInstallationDirectory().resolve(Paths.get("Data", "config", checksum.substring(0, 2), checksum.substring(2, 4), checksum));
        return () -> new FileDataReader(path);
    }

    @Override
    public List<String> getAvailableRegions() {
        return getBuildInfo().getValues(KEY_BUILDINFO_REGIONS)
                .orElse(Collections.emptyList());
    }

    @Override
    public String getCdnUrl() {
        String host = getBuildInfo().getFirstValue(KEY_BUILDINFO_CDN_HOSTS)
                .map(s -> s.split(" ")[0])
                .orElseThrow(() -> new CascParsingException("No CDN host(s) available."));
        String path = getBuildInfo().getFirstValue(KEY_BUILDINFO_CDN_PATH)
                .orElseThrow(() -> new CascParsingException("No CDN path available."));
        return String.format("http://%s%s", host, path);
    }

    @Override
    public String getVersion() {
        return getBuildInfo().getFirstValue(KEY_BUILDINFO_VERSION)
                .orElseThrow(() -> new CascParsingException("No version available."));
    }
}