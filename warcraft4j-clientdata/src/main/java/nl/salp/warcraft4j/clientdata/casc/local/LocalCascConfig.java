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
import nl.salp.warcraft4j.clientdata.casc.CascFileParsingException;
import nl.salp.warcraft4j.clientdata.casc.Checksum;
import nl.salp.warcraft4j.clientdata.casc.Config;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeUtil;
import nl.salp.warcraft4j.clientdata.io.file.FileDataReader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class LocalCascConfig implements CascConfig {
    private static final String BUILD_INFO_FILENAME = ".build.info";
    private final Path installationDirectory;
    private Config buildInfo;
    private Config build;
    private Config cdn;

    public LocalCascConfig(Path installationDirectory) {
        this.installationDirectory = installationDirectory;
    }

    private Supplier<DataReader> getBuildInfoReader() {
        return () -> new FileDataReader(installationDirectory.resolve(BUILD_INFO_FILENAME));
    }

    private Supplier<DataReader> getConfigDataReader(String checksum) {
        return () -> new FileDataReader(installationDirectory.resolve(Paths.get("Data", "config", checksum.substring(0, 2), checksum.substring(2, 4), checksum)));
    }

    protected Config getBuildInfo() {
        if (buildInfo == null) {
            buildInfo = Config.tableConfig(getBuildInfoReader());
        }
        return buildInfo;
    }

    protected Config getBuild() {
        if (build == null) {
            String checksum = getBuildInfo().getFirstValue("Build Key")
                    .orElseThrow(() -> new CascFileParsingException("No active configurations found to get the build key from."));
            build = Config.keyValueConfig(getConfigDataReader(checksum));
        }
        return build;
    }

    protected Config getCdn() {
        if (cdn == null) {
            String checksum = getBuildInfo().getFirstValue("CDN Key")
                    .orElseThrow(() -> new CascFileParsingException("No active configurations found to get the CDN key from."));
            build = Config.keyValueConfig(getConfigDataReader(checksum));
        }
        return cdn;
    }

    @Override
    public List<String> getAvailableRegions() {
        return getBuildInfo().getValues("Branch")
                .orElse(Collections.emptyList());
    }

    @Override
    public Checksum getRootContentChecksum() {
        return getBuild().getLastValue("root", (s) -> new Checksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascFileParsingException("No root content checksum defined."));
    }

    @Override
    public Checksum getEncodingFileChecksum() {
        return getBuild().getLastValue("encoding", (s) -> new Checksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascFileParsingException("No encoding file checksum defined."));
    }

    @Override
    public String getCdnUrl() {
        String host = getBuildInfo().getFirstValue("CDN Hosts")
                .map(s -> s.split(" ")[0])
                .orElseThrow(() -> new CascFileParsingException("No CDN host(s) defined."));
        String path = getBuildInfo().getFirstValue("CDN Path")
                .orElseThrow(() -> new CascFileParsingException("No CDN path defined."));
        return String.format("http://%s%s", host, path);
    }
}
