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
package nl.salp.warcraft4j.clientdata.casc.config;

import nl.salp.warcraft4j.clientdata.casc.CascParsingException;
import nl.salp.warcraft4j.clientdata.casc.Checksum;
import nl.salp.warcraft4j.clientdata.casc.DataReaderProvider;
import nl.salp.warcraft4j.clientdata.casc.Region;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeUtil;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
abstract class BaseCascConfig<T> implements CascConfig {
    public static Region REGION_DEFAULT = Region.EUROPE;
    protected static final String KEY_BUILD_DOWNLOAD = "download";
    protected static final String KEY_BUILD_ENCODING = "encoding";
    protected static final String KEY_BUILD_ENCODING_SIZE = "encoding-size";
    protected static final String KEY_BUILD_INSTALL = "install";
    protected static final String KEY_BUILD_INSTALLER = "buildConfig-playbuild-installer";
    protected static final String KEY_BUILD_NAME = "buildConfig-name";
    protected static final String KEY_BUILD_PATCH = "patch";
    protected static final String KEY_BUILD_PATCH_CONFIG = "patch-config";
    protected static final String KEY_BUILD_PATCH_SIZE = "patch-size";
    protected static final String KEY_BUILD_PRODUCT = "buildConfig-product";
    protected static final String KEY_BUILD_ROOT = "root";
    protected static final String KEY_BUILD_UID = "buildConfig-uid";

    protected static final String KEY_CDN_BUILDS = "builds";
    protected static final String KEY_CDN_ARCHIVES = "archives";
    protected static final String KEY_CDN_ARCHIVE_GROUP = "archive-group";
    protected static final String KEY_CDN_PATCH_ARCHIVES = "patch-archives";
    protected static final String KEY_CDN_PATCH_ARCHIVE_GROUP = "patch-achive-group";

    private final DataReaderProvider<T> dataReaderProvider;
    private final Region region;
    private Config buildConfig;
    private Config cdnConfig;


    protected BaseCascConfig(DataReaderProvider<T> dataReaderProvider) {
        this(REGION_DEFAULT, dataReaderProvider);
    }

    protected BaseCascConfig(Region region, DataReaderProvider<T> dataReaderProvider) {
        this.region = region;
        this.dataReaderProvider = dataReaderProvider;
    }

    protected abstract Optional<String> getBuildConfigKey();

    protected abstract Optional<String> getCdnConfigKey();

    protected final Supplier<DataReader> getDataReader(T uri) {
        return dataReaderProvider.getDataReader(uri);
    }

    protected abstract Supplier<DataReader> getConfigDataReader(String checksum);

    protected final Config getBuildConfig() {
        if (buildConfig == null) {
            String buildConfigKey = getBuildConfigKey()
                    .orElseThrow(() -> new CascParsingException("No build configuration file checksum available."));
            buildConfig = Config.keyValueConfig(getConfigDataReader(buildConfigKey));
        }
        return buildConfig;
    }

    protected final Config getCdnConfig() {
        if (cdnConfig == null) {
            String cdnConfigKey = getCdnConfigKey()
                    .orElseThrow(() -> new CascParsingException("No CDN configuration file checksum available."));
            buildConfig = Config.keyValueConfig(getConfigDataReader(cdnConfigKey));
        }
        return cdnConfig;
    }

    @Override
    public Checksum getRootContentChecksum() {
        return getBuildConfig().getLastValue(KEY_BUILD_ROOT, (s) -> new Checksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No root content checksum available."));
    }

    @Override
    public Checksum getEncodingFileChecksum() {
        return getBuildConfig().getLastValue(KEY_BUILD_ENCODING, (s) -> new Checksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No encoding file checksum available."));
    }

    @Override
    public long getEncodingFileSize() {
        return getBuildConfig().getLastValue(KEY_BUILD_ENCODING_SIZE, Long::valueOf)
                .orElseThrow(() -> new CascParsingException("No encoding file size available."));
    }

    @Override
    public abstract String getCdnUrl();

    public final Region getRegion() {
        return region;
    }

    protected final String getRegionCode() {
        return region.getRegionCode();
    }

    protected final Optional<String> getIndexedValue(Config config, String key, String indexKey, String indexValue) {
        return getIndexedValue(config, key, 0, indexKey, indexValue);
    }

    protected final Optional<String> getIndexedValue(Config config, String key, int idx, String indexKey, String indexValue) {
        return config.getValue(key, indexKey, indexValue)
                .flatMap(s -> {
                    if (s.contains(" ")) {
                        String[] values = s.split(" ");
                        if (idx > values.length) {
                            return Optional.empty();
                        } else {
                            return Optional.of(s.split(" ")[idx].trim());
                        }
                    } else {
                        return Optional.ofNullable(s);
                    }
                });
    }
}
