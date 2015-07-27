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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class BuildInfoFile {
    private Branch branch;
    private boolean active;
    private String buildFileChecksum;
    private String cdnFileChecksum;
    private String installFileChecksum;
    private long imSize;
    private String cdnPath;
    private List<String> cdnHosts;
    private List<String> tags;
    private String armadillo;
    private DateTime lastActivated;
    private String version;

    public BuildInfoFile(Branch branch, boolean active, String buildFileChecksum, String cdnFileChecksum, String installFileChecksum, long imSize, String cdnPath,
            List<String> cdnHosts,
            List<String> tags, String armadillo, DateTime lastActivated, String version) {
        this.branch = branch;
        this.active = active;
        this.buildFileChecksum = buildFileChecksum;
        this.cdnFileChecksum = cdnFileChecksum;
        this.installFileChecksum = installFileChecksum;
        this.imSize = imSize;
        this.cdnPath = cdnPath;
        this.cdnHosts = Optional.ofNullable(cdnHosts).orElse(Collections.emptyList());
        this.tags = Optional.ofNullable(tags).orElse(Collections.emptyList());
        this.armadillo = armadillo;
        this.lastActivated = lastActivated;
        this.version = version;
    }

    public Branch getBranch() {
        return branch;
    }

    public boolean isActive() {
        return active;
    }

    public String getBuildFileChecksum() {
        return buildFileChecksum;
    }

    public String getCdnFileChecksum() {
        return cdnFileChecksum;
    }

    public String getInstallFileChecksum() {
        return installFileChecksum;
    }

    public long getImSize() {
        return imSize;
    }

    public String getCdnPath() {
        return cdnPath;
    }

    public List<String> getCdnHosts() {
        return cdnHosts;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getArmadillo() {
        return armadillo;
    }

    public DateTime getLastActivated() {
        return lastActivated;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
