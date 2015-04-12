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

import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.clientdata.casc.CascInformation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class BuildInfo implements CascInformation {
    private final Region region;
    private final boolean versionActive;
    private final String buildHash;
    private final String cdnHash;
    private final String installationHash;
    private final int imSize;
    private final String cdnPath;
    private final String[] cdnHosts;
    private final String[] tags;
    /** Huh?!? Gotta love armadillo's though (even though this one is invisible ;-)) */
    private final String armadillo;
    private final String lastActivation;
    private final String versionName;
    /** Not explicitly available in the .build.info file and extracted from the versionName. */
    private final int buildNumber;

    public BuildInfo(Region region, boolean versionActive, String buildHash, String cdnHash, String installationHash, int imSize, String cdnPath, String[] cdnHosts, String[] tags, String armadillo, String lastActivation, String versionName) {
        this.region = region;
        this.versionActive = versionActive;
        this.buildHash = buildHash;
        this.cdnHash = cdnHash;
        this.installationHash = installationHash;
        this.imSize = imSize;
        this.cdnPath = cdnPath;
        this.cdnHosts = cdnHosts;
        this.tags = tags;
        this.armadillo = armadillo;
        this.lastActivation = lastActivation;
        this.versionName = versionName;
        // TODO Is the build number always the build number in the version string? Make this more robust anyway.
        this.buildNumber = Integer.parseInt(versionName.substring(versionName.lastIndexOf('.') + 1));
    }

    @Override
    public Region getRegion() {
        return region;
    }

    public boolean isVersionActive() {
        return versionActive;
    }

    @Override
    public String getBuildHash() {
        return buildHash;
    }

    @Override
    public String getCdnHash() {
        return cdnHash;
    }

    public String getInstallationHash() {
        return installationHash;
    }

    public int getImSize() {
        return imSize;
    }

    public String getCdnPath() {
        return cdnPath;
    }

    @Override
    public String[] getCdnHosts() {
        return cdnHosts;
    }

    public String[] getTags() {
        return tags;
    }

    public String getArmadillo() {
        return armadillo;
    }

    public String getLastActivation() {
        return lastActivation;
    }

    @Override
    public String getVersionName() {
        return versionName;
    }

    @Override
    public int getBuildNumber() {
        return buildNumber;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
