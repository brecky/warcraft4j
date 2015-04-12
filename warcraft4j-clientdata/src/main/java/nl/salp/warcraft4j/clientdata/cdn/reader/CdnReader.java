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
package nl.salp.warcraft4j.clientdata.cdn.reader;

import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.clientdata.cdn.*;

import java.util.List;

/**
 * Reader for reading CDN information.
 *
 * @author Barre Dijkstra
 */
public interface CdnReader {
    /**
     * Get the CDN host information for the given application.
     *
     * @param application The application.
     *
     * @return The CDN host information.
     *
     * @throws CdnException When reading of the information failed.
     */
    List<CdnHost> getCdnHosts(Application application) throws CdnException;

    /**
     * Get the application CDN host information for the region.
     *
     * @param application The application.
     * @param region      The region.
     *
     * @return The CDN host information.
     *
     * @throws CdnException When reading of the information failed.
     */
    CdnHost getCdnHost(Application application, Region region) throws CdnException;

    /**
     * Get the application versions per region for the given application.
     *
     * @param application The application to get the versions for.
     *
     * @return The versions.
     *
     * @throws CdnException When the version information couldn't be read.
     */
    List<ApplicationVersion> getVersions(Application application) throws CdnException;

    /**
     * Get the application version for the given region.
     *
     * @param application The application.
     * @param region      The region.
     *
     * @return The application version.
     *
     * @throws CdnException When the application version couldn't be read.
     */
    ApplicationVersion getVersion(Application application, Region region) throws CdnException;

    /**
     * Get a file from the CDN.
     *
     * @param application The application.
     * @param region      The region.
     * @param fileType    The file type.
     * @param fileName    The file name.
     *
     * @return The file data.
     *
     * @throws CdnException When the file could not be read.
     */
    byte[] getFile(Application application, Region region, CdnFileType fileType, String fileName) throws CdnException;

    byte[] getConfigFile(Application application, Region region, CdnFileType fileType, String fileName) throws CdnException;
}
