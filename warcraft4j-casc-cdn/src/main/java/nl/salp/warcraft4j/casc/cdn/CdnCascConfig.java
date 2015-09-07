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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.casc.BaseCascConfig;
import nl.salp.warcraft4j.casc.Config;
import nl.salp.warcraft4j.config.W4jConfig;
import nl.salp.warcraft4j.casc.*;
import nl.salp.warcraft4j.io.reader.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CdnCascConfig extends BaseCascConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CdnCascConfig.class);
    private static final String KEY_VERSIONS_REGION = "Region";
    private static final String KEY_VERSIONS_BUILD_ID = "BuildId";
    private static final String KEY_VERSIONS_VERSION = "VersionsName";
    private static final String KEY_VERSIONS_BUILD_CONFIG = "BuildConfig";
    private static final String KEY_VERSIONS_CDN_CONFIG = "CDNConfig";
    private static final String KEY_CDNS_NAME = "Name";
    private static final String KEY_CDNS_PATH = "Path";
    private static final String KEY_CDNS_HOSTS = "Hosts";
    private static final String URL_MASK = "http://us.patch.battle.net/%s/%s";
    private static final String FILE_CDNS = "cdns";
    private static final String FILE_VERSIONS = "versions";

    private final CdnVersion cdnVersion;
    private Config cdns;
    private Config versions;


    public CdnCascConfig(W4jConfig w4jConfig, DataReaderProvider dataReaderProvider) {
        super(w4jConfig, dataReaderProvider);
        this.cdnVersion = CdnVersion.getFrom(w4jConfig.getBranch());
    }

    private String getDirectUrl(String file) {
        return format(URL_MASK, cdnVersion.getProductCode(), file);
    }

    private Config getCdns() {
        if (cdns == null) {
            String uri = getDirectUrl(FILE_CDNS);
            LOGGER.debug("Initialising CDNs config from file {}", uri);
            cdns = Config.tableConfig(getDataReader(uri));
        }
        return cdns;
    }

    private Optional<String> getCdnValue(String key) {
        return getIndexedValue(getCdns(), key, KEY_CDNS_NAME, getRegionCode());
    }

    private Config getVersions() {
        if (versions == null) {
            versions = Config.tableConfig(getDataReader(getDirectUrl(FILE_VERSIONS)));
        }
        return versions;
    }

    private Optional<String> getVersionValue(String key) {
        return getIndexedValue(getVersions(), key, KEY_VERSIONS_REGION, getRegionCode());
    }

    @Override
    public Optional<String> getBuildConfigKey() {
        return getVersionValue(KEY_VERSIONS_BUILD_CONFIG);
    }

    @Override
    public Optional<String> getCdnConfigKey() {
        return getVersionValue(KEY_VERSIONS_CDN_CONFIG);
    }

    @Override
    protected Supplier<DataReader> getConfigDataReader(String checksum) {
        String uri = format("%s/config/%s/%s/%s", getCdnUrl(), checksum.substring(0, 2), checksum.substring(2, 4), checksum);
        LOGGER.trace("Mapped checksum {} to URL {}", checksum, uri);
        return getDataReader(uri);
    }

    @Override
    public List<String> getAvailableRegions() {
        List<String> cdnNames = getCdns().getValues(KEY_CDNS_NAME)
                .orElseThrow(() -> new CascParsingException("No CDN names found."));
        List<String> versionRegions = getVersions().getValues(KEY_VERSIONS_REGION)
                .orElseThrow(() -> new CascParsingException("No version regions found."));

        List<String> regions = new ArrayList<>(cdnNames);
        regions.retainAll(versionRegions);
        LOGGER.trace("Retrieved available regions {}", regions);
        return regions;
    }

    @Override
    public String getCdnUrl() {
        String host = getCdnValue(KEY_CDNS_HOSTS)
                .orElseThrow(() -> new CascParsingException(format("No CDN url found for region %s.", getRegion())));
        String path = getCdnValue(KEY_CDNS_PATH)
                .orElseThrow(() -> new CascParsingException(format("No CDN path found for region %s.", getRegion())));
        String url = format("http://%s/%s", host, path);
        return url;
    }

    @Override
    public String getVersion() {
        String version = getVersionValue(KEY_VERSIONS_VERSION)
                .orElseThrow(() -> new CascParsingException(format("No version information found for region %s.", getRegion())));
        LOGGER.trace("Retrieved version {}", version);
        return version;
    }
}
