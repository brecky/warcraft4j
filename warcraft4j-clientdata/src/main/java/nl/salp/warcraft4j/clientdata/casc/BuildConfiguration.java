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
package nl.salp.warcraft4j.clientdata.casc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class BuildConfiguration {
    private String rootKey;
    private String downloadKey;
    private String installationKey;
    private String[] encoding;
    private long[] encodingSize;
    private String buildName;
    private String buildPlayBuildInstaller;
    private String buildProduct;
    private String buildUid;
    private String patchKey;
    private long patchSize;
    private String patchConfigKey;

    public BuildConfiguration(String rootKey, String downloadKey, String installationKey, String[] encoding, long[] encodingSize, String buildName, String buildPlayBuildInstaller, String buildProduct, String buildUid, String patchKey, long patchSize, String patchConfigKey) {
        this.rootKey = rootKey;
        this.downloadKey = downloadKey;
        this.installationKey = installationKey;
        this.encoding = encoding;
        this.encodingSize = encodingSize;
        this.buildName = buildName;
        this.buildPlayBuildInstaller = buildPlayBuildInstaller;
        this.buildProduct = buildProduct;
        this.buildUid = buildUid;
        this.patchKey = patchKey;
        this.patchSize = patchSize;
        this.patchConfigKey = patchConfigKey;
    }

    public String getRootKey() {
        return rootKey;
    }

    public String getDownloadKey() {
        return downloadKey;
    }

    public String getInstallationKey() {
        return installationKey;
    }

    public String[] getEncoding() {
        return encoding;
    }

    public long[] getEncodingSize() {
        return encodingSize;
    }

    public String getBuildName() {
        return buildName;
    }

    public String getBuildPlayBuildInstaller() {
        return buildPlayBuildInstaller;
    }

    public String getBuildProduct() {
        return buildProduct;
    }

    public String getBuildUid() {
        return buildUid;
    }

    public String getPatchKey() {
        return patchKey;
    }

    public long getPatchSize() {
        return patchSize;
    }

    public String getPatchConfigKey() {
        return patchConfigKey;
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
