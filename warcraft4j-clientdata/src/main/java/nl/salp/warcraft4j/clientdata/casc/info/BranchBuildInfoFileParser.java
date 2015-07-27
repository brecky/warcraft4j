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
package nl.salp.warcraft4j.clientdata.casc.info;

import nl.salp.warcraft4j.clientdata.casc.Branch;

import java.util.Map;
import java.util.Optional;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class BranchBuildInfoFileParser extends BuildInfoFileParser<Optional<BuildInfoFile>> {
    private static final String FIELD_BRANCH = "Branch";
    private static final String FIELD_ACTIVE = "Active";
    private static final String FIELD_BUILD_KEY = "Build Key";
    private static final String FIELD_CDN_KEY = "CDN Key";
    private static final String FIELD_INSTALL_KEY = "Install Key";
    private static final String FIELD_IM_SIZE = "IM Size";
    private static final String FIELD_CDN_PATH = "CDN Path";
    private static final String FIELD_CDN_HOSTS = "CDN Hosts";
    private static final String FIELD_TAGS = "Tags";
    private static final String FIELD_ARMADILLO = "Armadillo";
    private static final String FIELD_LAST_ACTIVATED = "Last Activated";
    private static final String FIELD_VERSION = "Version";

    public static final String FILENAME = ".build.info";

    private final Branch branch;

    public BranchBuildInfoFileParser(Branch branch) {
        this.branch = branch;
    }

    @Override
    protected Optional<BuildInfoFile> parse(Map<String, Field> fields) {
        return parseEntries(fields).stream()
                .filter(this::matchesBranch)
                .findFirst();
    }

    protected boolean matchesBranch(BuildInfoFile buildInfoFile) {
        return buildInfoFile != null && buildInfoFile.getBranch() == branch;
    }
}
