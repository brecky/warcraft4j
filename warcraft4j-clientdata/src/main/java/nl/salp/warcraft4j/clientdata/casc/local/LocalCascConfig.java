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

import nl.salp.warcraft4j.clientdata.casc.CascConfig;
import nl.salp.warcraft4j.clientdata.casc.CascParsingException;
import nl.salp.warcraft4j.clientdata.casc.Checksum;
import nl.salp.warcraft4j.clientdata.casc.Config;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeUtil;
import nl.salp.warcraft4j.clientdata.io.file.FileDataReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class LocalCascConfig implements CascConfig {
    private static final String FILENAME_BUILDINFO = ".build.info";
    private static final String KEY_BUILDINFO_ACTIVE = "Active";
    private static final String KEY_BUILDINFO_ARMADILLO = "Armadillo";
    private static final String KEY_BUILDINFO_BUILD = "Build Key";
    private static final String KEY_BUILDINFO_CDN = "CDN Key";
    private static final String KEY_BUILDINFO_CDN_HOSTS = "CDN Hosts";
    private static final String KEY_BUILDINFO_CDN_PATH = "CDN Path";
    private static final String KEY_BUILDINFO_IM_SIZE = "IM Size";
    private static final String KEY_BUILDINFO_INSTALL = "Install Key";
    private static final String KEY_BUILDINFO_LAST_ACTIVATION = "Last Activated";
    private static final String KEY_BUILDINFO_REGIONS = "Branch";
    private static final String KEY_BUILDINFO_TAGS = "Tags";
    private static final String KEY_BUILDINFO_VERSION = "Version";

    private static final String KEY_BUILD_DOWNLOAD = "download";
    private static final String KEY_BUILD_ENCODING = "encoding";
    private static final String KEY_BUILD_ENCODING_SIZE = "encoding-size";
    private static final String KEY_BUILD_INSTALL = "install";
    private static final String KEY_BUILD_INSTALLER = "build-playbuild-installer";
    private static final String KEY_BUILD_NAME = "build-name";
    private static final String KEY_BUILD_PATCH = "patch";
    private static final String KEY_BUILD_PATCH_CONFIG = "patch-config";
    private static final String KEY_BUILD_PATCH_SIZE = "patch-size";
    private static final String KEY_BUILD_PRODUCT = "build-product";
    private static final String KEY_BUILD_ROOT = "root";
    private static final String KEY_BUILD_UID = "build-uid";

    private static final String KEY_CDN_BUILDS = "builds";
    private static final String KEY_CDN_ARCHIVES = "archives";
    private static final String KEY_CDN_ARCHIVE_GROUP = "archive-group";
    private static final String KEY_CDN_PATCH_ARCHIVES = "patch-archives";
    private static final String KEY_CDN_PATCH_ARCHIVE_GROUP = "patch-achive-group";

    private final Path installationDirectory;
    private Config buildInfo;
    private Config build;
    private Config cdn;

    public LocalCascConfig(Path installationDirectory) {
        this.installationDirectory = installationDirectory;
    }

    private Supplier<DataReader> getBuildInfoReader() {
        return () -> new FileDataReader(installationDirectory.resolve(FILENAME_BUILDINFO));
    }

    private Supplier<DataReader> getConfigDataReader(String checksum) {
        return () -> new FileDataReader(installationDirectory.resolve(Paths.get("Data", "config", checksum.substring(0, 2), checksum.substring(2, 4), checksum)));
    }

    protected final Config getBuildInfo() {
        if (buildInfo == null) {
            buildInfo = Config.tableConfig(getBuildInfoReader());
        }
        return buildInfo;
    }

    protected final Config getBuild() {
        if (build == null) {
            String checksum = getBuildInfo().getFirstValue(KEY_BUILDINFO_BUILD)
                    .orElseThrow(() -> new CascParsingException("No active configurations found to get the build key from."));
            build = Config.keyValueConfig(getConfigDataReader(checksum));
        }
        return build;
    }

    protected final Config getCdn() {
        if (cdn == null) {
            String checksum = getBuildInfo().getFirstValue(KEY_BUILDINFO_CDN)
                    .orElseThrow(() -> new CascParsingException("No active configurations found to get the CDN key from."));
            build = Config.keyValueConfig(getConfigDataReader(checksum));
        }
        return cdn;
    }

    @Override
    public List<String> getAvailableRegions() {
        return getBuildInfo().getValues(KEY_BUILDINFO_REGIONS)
                .orElse(Collections.emptyList());
    }

    /**
     * Check if the configuration for a region is active.
     *
     * @param region The region to check.
     *
     * @return {@code true} if the region is active.
     */
    public boolean isConfigurationForRegionActive(String region) {
        return isConfigurationActive(getAvailableRegions().indexOf(region));
    }

    /**
     * Check if a configuration is active.
     *
     * @param index The index of the configuration.
     *
     * @return {@code true} if the configuration is active.
     */
    protected boolean isConfigurationActive(int index) {
        boolean active = false;
        List<Boolean> activeFlags = getBuildInfo().getValues(KEY_BUILDINFO_ACTIVE)
                .map(v -> v.stream().map(s -> "1".equals(s)).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        if (activeFlags.size() > index) {
            active = activeFlags.get(index);
        }
        return active;

    }

    @Override
    public Checksum getRootContentChecksum() {
        return getBuild().getLastValue(KEY_BUILD_ROOT, (s) -> new Checksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No root content checksum defined."));
    }

    @Override
    public Checksum getEncodingFileChecksum() {
        return getBuild().getLastValue(KEY_BUILD_ENCODING, (s) -> new Checksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No encoding file checksum defined."));
    }

    @Override
    public long getEncodingFileSize() {
        return getBuild().getLastValue(KEY_BUILD_ENCODING_SIZE, Long::valueOf)
                .orElseThrow(() -> new CascParsingException("No encoding file size defined."));
    }

    @Override
    public String getCdnUrl() {
        String host = getBuildInfo().getFirstValue(KEY_BUILDINFO_CDN_HOSTS)
                .map(s -> s.split(" ")[0])
                .orElseThrow(() -> new CascParsingException("No CDN host(s) defined."));
        String path = getBuildInfo().getFirstValue(KEY_BUILDINFO_CDN_PATH)
                .orElseThrow(() -> new CascParsingException("No CDN path defined."));
        return String.format("http://%s%s", host, path);
    }
}