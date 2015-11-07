/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License") throws CascParsingException; you may not use this file except in compliance
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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.FileKey;

import java.util.List;
import java.util.Optional;

/**
 * CASC configuration.
 *
 * @author Barre Dijkstra
 */
public interface CdnCascConfig {
    /**
     * Get the available regions.
     *
     * @return The regions.
     *
     * @throws CascParsingException When the available regions can't be retrieved.
     */
    List<String> getAvailableRegions() throws CascParsingException;

    /**
     * Get the build configuration key if present.
     *
     * @return Optional of the build configuration key.
     */
    Optional<String> getBuildConfigKey();

    /**
     * Get the build configuration key if present.
     *
     * @return Optional of the CDN configuration key.
     */
    Optional<String> getCdnConfigKey();

    /**
     * Get the content checksum of the root file.
     *
     * @return The content checksum for the root file.
     *
     * @throws CascParsingException When the root content checksum is not available.
     */
    ContentChecksum getRootContentChecksum() throws CascParsingException;

    /**
     * Get the file key of the encoding file.
     *
     * @return The file key for the encoding file.
     *
     * @throws CascParsingException When the encoding file key is not available.
     */
    FileKey getExtractedEncodingFileChecksum() throws CascParsingException;

    /**
     * Get the file key of the encoding file for the active encoding file.
     *
     * @return The file key for the encoding file.
     */
    FileKey getStorageEncodingFileChecksum();

    /**
     * Get the size of the encoding file for the active encoding file.
     *
     * @return The file size of the encoding file.
     */
    long getStorageEncodingFileSize();

    /**
     * Get the size of the encoding file.
     *
     * @return The size in bytes.
     *
     * @throws CascParsingException When the encoding file size is not available.
     */
    long getExtractedEncodingFileSize() throws CascParsingException;

    /**
     * Get the file keys for the archive files.
     *
     * @return The file keys.
     *
     * @throws CascParsingException When the archive file keys are not available.
     */
    List<FileKey> getArchiveChecksums() throws CascParsingException;

    /**
     * Get the file key for the archive group file.
     *
     * @return The file key.
     *
     * @throws CascParsingException When the archive group file key is not available.
     */
    FileKey getArchiveGroupChecksum() throws CascParsingException;

    /**
     * Get the file keys for the patch archive files.
     *
     * @return The file keys.
     *
     * @throws CascParsingException When the patch archive file keys are not available.
     */
    List<FileKey> getPatchArchiveChecksums() throws CascParsingException;

    /**
     * Get the file key for the patch archive group file.
     *
     * @return The file key.
     *
     * @throws CascParsingException When the patch archive group file key is not available.
     */
    FileKey getPatchArchiveGroupChecksum() throws CascParsingException;

    /**
     * Get the CDN URL.
     *
     * @return The CDN URL.
     *
     * @throws CascParsingException When the CDN URL is not available.
     */
    String getCdnUrl() throws CascParsingException;

    /**
     * Get the build version.
     *
     * @return The version.
     *
     * @throws CascParsingException When the version is not available.
     */
    String getVersion() throws CascParsingException;

    /**
     * Get the region for which the CASC config is configured.
     *
     * @return The region.
     */
    Region getRegion();

    /**
     * Get the region code for the configured region.
     *
     * @return The region code.
     */
    String getRegionCode();
}
