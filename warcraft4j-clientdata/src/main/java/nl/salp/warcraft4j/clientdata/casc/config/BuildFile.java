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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class BuildFile {
    private final Checksum root;
    private final Checksum download;
    private final Checksum install;
    private final Checksum encodingFile;
    private final Long encodingFileSize;
    private final String buildName;
    private final String playBuildInstaller;
    private final String buildProduct;
    private final String buildUid;
    private final Checksum patch;
    private final long patchSize;
    private final Checksum patchConfig;

    public BuildFile(Checksum root, Checksum download, Checksum install, Checksum encodingFile, long encodingFileSize, String buildName,
            String playBuildInstaller, String buildProduct, String buildUid, Checksum patch, long patchSize, Checksum patchConfig) {
        this.root = root;
        this.download = download;
        this.install = install;
        this.encodingFile = encodingFile;
        this.encodingFileSize = encodingFileSize;
        this.buildName = buildName;
        this.playBuildInstaller = playBuildInstaller;
        this.buildProduct = buildProduct;
        this.buildUid = buildUid;
        this.patch = patch;
        this.patchSize = patchSize;
        this.patchConfig = patchConfig;
    }

    public Checksum getRootContentChecksum() {
        return root;
    }

    public Checksum getDownloadContentChecksum() {
        return download;
    }

    public Checksum getInstallContentChecksum() {
        return install;
    }

    public Checksum getEncodingFileChecksum() {
        return encodingFile;
    }

    public long getEncodingFileSize() {
        return encodingFileSize;
    }

    public String getBuildName() {
        return buildName;
    }

    public String getPlayBuildInstaller() {
        return playBuildInstaller;
    }

    public String getBuildProduct() {
        return buildProduct;
    }

    public String getBuildUid() {
        return buildUid;
    }

    public boolean isPatched() {
        return patch != null;
    }

    public Checksum getPatchFileChecksum() {
        return patch;
    }

    public long getPatchSize() {
        return patchSize;
    }

    public Checksum getPatchConfigFileChecksum() {
        return patchConfig;
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
