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
package nl.salp.warcraft4j.clientdata.casc.file;

import nl.salp.warcraft4j.clientdata.casc.CascConfig;
import nl.salp.warcraft4j.clientdata.casc.CascRegion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 * <p/>
 * TODO Clean up class and make a non-hacky implementation.
 *
 * @author Barre Dijkstra
 */
public class BuildInfoReader {
    private static final String BUILD_INFO_FILE = ".build.info";
    private static final String HEADER = "Branch!STRING:0|Active!DEC:1|Build Key!HEX:16|CDN Key!HEX:16|Install Key!HEX:16|IM Size!DEC:4|CDN Path!STRING:0|CDN Hosts!STRING:0|Tags!STRING:0|Armadillo!STRING:0|Last Activated!STRING:0|Version!STRING:0";
    private final CascConfig config;

    public BuildInfoReader(CascConfig config) throws IllegalArgumentException {
        if (config == null) {
            throw new IllegalArgumentException("Can't create a BuildInfoReader with a null configuration.");
        }
        if (isEmpty(config.getInstallationDirectory())) {
            throw new IllegalArgumentException("No World of Warcraft installation directory configured.");
        }
        this.config = config;
    }

    public BuildInfo read() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(getBuildInfoFile()))) {
            String header = reader.readLine();
            if (header == null || !HEADER.equals(header)) {
                throw new IOException(format("Invalid header found: %s, expected: %s", header, HEADER));
            }
            String body = reader.readLine();
            if (body == null) {
                throw new IOException("No build information entry found.");
            }
            return parse(body);
        }
    }

    private BuildInfo parse(String entry) throws IOException {
        String[] entries = entry.split("[|]");
        if (entries.length != 12) {
            throw new IOException(format(".build.info entry is invalid: found %d entries while 12 were expected. Entry: %s",entries.length, entry));
        }
        CascRegion region = CascRegion.getRegion(entries[0]);
        boolean active = Integer.parseInt(entries[1]) == 1;
        String buildKey = entries[2];
        String cdnKey = entries[3];
        String installationKey = entries[4];
        int size = Integer.parseInt(entries[5]);
        String cdnPath = entries[6];
        String[] cdnHosts = entries[7].split(" ");
        String[] tags = entries[8].split("[?]");
        String armadillo = entries[9];
        String lastActivation = entries[10];
        String version = entries[11];

        return new BuildInfo(region.getRegion(), active, buildKey, cdnKey, installationKey, size, cdnPath, cdnHosts, tags, armadillo, lastActivation, version);
    }

    private File getBuildInfoFile() throws IOException {
        File file = new File(config.getInstallationDirectory(), BUILD_INFO_FILE);
        if (!file.exists()) {
            throw new IOException(format("Can't find the file %s in the WoW installation directory %s", BUILD_INFO_FILE, config.getInstallationDirectory()));
        }
        if (!file.isFile() || !file.canRead()) {
            throw new IOException(format("Can't read the file %s in the WoW installation directory %s", BUILD_INFO_FILE, config.getInstallationDirectory()));
        }
        return file;
    }


}
