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

import nl.salp.warcraft4j.clientdata.casc.Checksum;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class BuildFileParser extends ConfigFileParser<BuildFile> {
    private static final String KEY_ROOT = "root";
    private static final String KEY_DOWNLOAD = "download";
    private static final String KEY_INSTALL = "install";
    private static final String KEY_ENCODING = "encoding";
    private static final String KEY_ENCODING_SIZE = "encoding-size";
    private static final String KEY_BUILD_NAME = "build-name";
    private static final String KEY_BUILD_PLAYBUILD_INSTALLER = "build-playbuild-installer";
    private static final String KEY_BUILD_PRODUCT = "build-product";
    private static final String KEY_BUILD_UID = "build-uid";
    private static final String KEY_PATCH = "patch";
    private static final String KEY_PATCH_SIZE = "patch-size";
    private static final String KEY_PATCH_CONFIG = "patch-config";

    @Override
    protected BuildFile parse(Map<String, String> fields) {
        Checksum root = new Checksum(DataTypeUtil.hexStringToByteArray(fields.get(KEY_ROOT)));
        Checksum download = new Checksum(DataTypeUtil.hexStringToByteArray(fields.get(KEY_DOWNLOAD)));
        Checksum install = new Checksum(DataTypeUtil.hexStringToByteArray(fields.get(KEY_INSTALL)));
        Checksum encodingFile = new Checksum(getLastEntry(toList(fields.get(KEY_ENCODING), DataTypeUtil::hexStringToByteArray)));
        long encodingFileSize = getLastEntry(toList(fields.getOrDefault(KEY_ENCODING_SIZE, "0"), Long::parseLong));
        String buildName = fields.get(KEY_BUILD_NAME);
        String playBuildInstaller = fields.get(KEY_BUILD_PLAYBUILD_INSTALLER);
        String buildProduct = fields.get(KEY_BUILD_PRODUCT);
        String buildUid = fields.get(KEY_BUILD_UID);
        Checksum patch = Optional.ofNullable(fields.get(KEY_PATCH)).map(DataTypeUtil::hexStringToByteArray).map(Checksum::new).orElse(null);
        long patchSize = Optional.ofNullable(fields.get(KEY_PATCH_SIZE)).map(Long::valueOf).orElse(0L);
        Checksum patchConfig = Optional.ofNullable(fields.get(KEY_PATCH_CONFIG)).map(DataTypeUtil::hexStringToByteArray).map(Checksum::new).orElse(null);

        return new BuildFile(root, download, install, encodingFile, encodingFileSize, buildName, playBuildInstaller, buildProduct, buildUid, patch, patchSize, patchConfig);
    }

    private static <T> T getLastEntry(List<T> list) {
        T value = null;
        if (list != null && !list.isEmpty()) {
            value = list.get(list.size() - 1);
        }
        return value;
    }
}
