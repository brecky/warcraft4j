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

import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.WowVersion;
import nl.salp.warcraft4j.io.DataReader;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service for accessing CASC information and data.
 *
 * @author Barre Dijkstra
 */
public interface CascService {
    /**
     * Get the World of Warcraft version on which the service is working.
     *
     * @return The World of Warcraft version.
     */
    WowVersion getVersion();

    /**
     * Get the preferred locale for the CASC context if available.
     *
     * @return Optional containing the locale if it's available.
     */
    Optional<Locale> getLocale();

    /**
     * Check if a {@link CascFile} is available for a content checksum.
     *
     * @param contentChecksum The content checksum.
     *
     * @return {@code true} if a CASC file is available.
     */
    boolean isCascFileAvailable(ContentChecksum contentChecksum);

    /**
     * Check if a {@link CascFile} is available for a filename hash.
     *
     * @param filenameHash The filename hash.
     *
     * @return {@code true} if a CASC file is available.
     */
    boolean isCascFileAvailable(long filenameHash);

    /**
     * Check if a {@link CascFile} is available for a filename.
     *
     * @param filename The filename.
     *
     * @return {@code true} if a CASC file is available.
     */
    boolean isCascFileAvailable(String filename);

    /**
     * Get the {@link CascFile} related to the checksum of the file content.
     *
     * @param contentChecksum The content checksum.
     *
     * @return Optional containing the {@link CascFile} if there is one for the content checksum.
     */
    Optional<CascFile> getCascFile(ContentChecksum contentChecksum);

    /**
     * Get the {@link CascFile} related to the hash of the filename.
     *
     * @param filenameHash The hash of the filename.
     *
     * @return Optional containing the {@link CascFile} if there is one for the filename hash.
     */
    Optional<CascFile> getCascFile(long filenameHash);

    /**
     * Get the {@link CascFile} related to the filename.
     *
     * @param filename The name of the file.
     *
     * @return Optional containing the {@link CascFile} if there is one for the filename.
     */
    Optional<CascFile> getCascFile(String filename);

    /**
     * Get all {@link CascFile} instances available.
     *
     * @return The CascFile instances.
     */
    Set<CascFile> getAllCascFiles();

    /**
     * Check if a {@link RootEntry} is available for a casc file.
     *
     * @param cascFile The casc file.
     *
     * @return {@code true} if a root entry is available.
     */
    boolean isRootEntryAvailable(CascFile cascFile);

    /**
     * Get the root entries for a casc file.
     *
     * @param cascFile The CASC file.
     *
     * @return The root entries for the file.
     */
    List<RootEntry> getRootEntries(CascFile cascFile);

    /**
     * Get all available root entries.
     * <p>
     * Please note that not all root entries refer to a valid file.
     *
     * @return The available root entries.
     */
    Set<RootEntry> getAllRootEntries();

    /**
     * Check if a {@link EncodingEntry} is available for a root entry.
     *
     * @param rootEntry The root entry.
     *
     * @return {@code true} if a encoding entry is available.
     */
    boolean isEncodingEntryAvailable(RootEntry rootEntry);

    /**
     * Get the encoding entry for a root entry.
     *
     * @param rootEntry The root entry.
     *
     * @return Optional with the encoding entry if it is available.
     */
    Optional<EncodingEntry> getEncodingEntry(RootEntry rootEntry);

    /**
     * Get all available encoding entries.
     *
     * @return The available encoding entries.
     */
    Set<EncodingEntry> getAllEncodingEntries();

    /**
     * Check if <em>atleast</em> one of the referred {@link IndexEntry} is available for an encoding entry.
     *
     * @param encodingEntry The encoding entry.
     *
     * @return {@code true} if atleast one index entry is available.
     */
    boolean isAtleastOneIndexEntryAvailable(EncodingEntry encodingEntry);

    /**
     * Check if all of the referred {@link IndexEntry} is available for an encoding entry.
     *
     * @param encodingEntry The encoding entry.
     *
     * @return {@code true} if atleast one index entry is available.
     */
    boolean isIndexEntryAvailable(EncodingEntry encodingEntry);

    /**
     * Get the index entries for an encoding entry.
     *
     * @param encodingEntry The encoding entry.
     *
     * @return The index entries.
     */
    List<IndexEntry> getIndexEntries(EncodingEntry encodingEntry);

    /**
     * Get all available index entries.
     *
     * @return The available index entries.
     */
    Set<IndexEntry> getAllIndexEntries();

    /**
     * Check if the {@link CascService} implementation has the data for a casc file available.
     *
     * @param cascFile The casc file to check.
     *
     * @return {@code true} if the service has the data for the file available.
     */
    boolean isDataAvailable(CascFile cascFile);

    /**
     * Check if the {@link CascService} implementation has the data referenced by an index entry available.
     *
     * @param indexEntry The index entry check.
     *
     * @return {@code true} if the service has the data for the index entry available.
     */
    boolean isDataAvailable(IndexEntry indexEntry);

    /**
     * Get a data reader for a file.
     *
     * @param cascFile The casc file.
     *
     * @return The data reader for the file.
     *
     * @throws CascEntryNotFoundException When no file was available for the CASC service.
     * @see #isDataAvailable(CascFile)
     */
    DataReader getDataReader(CascFile cascFile) throws CascEntryNotFoundException;

    /**
     * Get a data reader for the file referenced by an index entry.
     * <p>
     * Some files consist of multiple blocks with each their own index entry, so it is possible that the file is only a partial file.
     *
     * @param indexEntry The index entry for the file.
     *
     * @return The data reader for the file.
     *
     * @throws CascEntryNotFoundException When no file was available for the CASC service.
     * @see #isDataAvailable(IndexEntry)
     */
    DataReader getDataReader(IndexEntry indexEntry) throws CascEntryNotFoundException;
}