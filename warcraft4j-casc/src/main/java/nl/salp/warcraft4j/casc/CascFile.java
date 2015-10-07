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
package nl.salp.warcraft4j.casc;

import java.util.Optional;

/**
 * General information of a file stored in a CASC.
 *
 * @author Barre Dijkstra
 */
public interface CascFile {
    /**
     * Get the hash of the filename.
     * <p>
     * The filename hash is used as primary resolution entry point for a CASC file through a corresponding {@link RootEntry}.
     *
     * @return The hash.
     *
     * @see RootEntry
     */
    long getFilenameHash();

    /**
     * Get the relative filename of the file in the CASC.
     *
     * @return Optional containing the filename if it's known.
     */
    Optional<String> getFilename();


    /**
     * Set the filename for the CASC file.
     *
     * @param filename The filename of the file in the CASC.
     */
    void setFilename(String filename);

    /**
     * Get the header of the file.
     *
     * @return Optional containing the header of the file if available.
     */
    Optional<FileHeader> getHeader();

    /**
     * Set the header for the file.
     *
     * @param header The header.
     */
    void setHeader(FileHeader header);
}
