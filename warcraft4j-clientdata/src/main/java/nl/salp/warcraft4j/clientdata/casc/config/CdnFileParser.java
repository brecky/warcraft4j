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

import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CdnFileParser extends ConfigFileParser<CdnFile> {
    private final static String KEY_ARCHIVES = "archives";
    private final static String KEY_ARCHIVE_GROUP = "archive-group";
    private final static String KEY_PATCH_ARCHIVES = "patch-archives";
    private final static String KEY_PATCH_ARCHIVE_GROUP = "patch-archive-group";
    private final static String KEY_BUILDS = "builds";

    @Override
    protected CdnFile parse(Map<String, String> fields) {
        List<byte[]> archives = toList(fields.get(KEY_ARCHIVES), DataTypeUtil::hexStringToByteArray);
        byte[] archiveGroup = DataTypeUtil.hexStringToByteArray(fields.get(KEY_ARCHIVE_GROUP));
        List<byte[]> patchArchives = Optional.ofNullable(fields.get(KEY_PATCH_ARCHIVES)).filter(StringUtils::isNotEmpty).map(p -> toList(p, DataTypeUtil::hexStringToByteArray))
                .orElse(Collections.emptyList());
        byte[] patchArchiveGroup = Optional.ofNullable(fields.get(KEY_PATCH_ARCHIVE_GROUP)).map(DataTypeUtil::hexStringToByteArray).orElse(null);
        List<byte[]> builds = toList(fields.get(KEY_BUILDS), DataTypeUtil::hexStringToByteArray);

        return new CdnFile(archives, archiveGroup, patchArchives, patchArchiveGroup, builds);
    }
}
