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

import java.util.List;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public interface CascConfig {
    /**
     * Get the available regions.
     *
     * @return The regions.
     */
    List<String> getAvailableRegions();

    /**
     * Get the content checksum of the root file.
     *
     * @return The content checksum.
     */
    Checksum getRootContentChecksum();

    /**
     * Get the file key of the encoding file.
     *
     * @return The file key.
     */
    Checksum getEncodingFileChecksum();

    /**
     * Get the size of the encoding file.
     *
     * @return The size in bytes.
     */
    long getEncodingFileSize();

    /**
     * Get the CDN URL.
     *
     * @return The CDN URL.
     */
    String getCdnUrl();

    /**
     * Get the build version.
     *
     * @return The version.
     */
    String getVersion();
}
