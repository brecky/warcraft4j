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

/**
 * Index entry, linking a file key to a data segment in a CASC datafile.
 * <p>
 * File keys can be retrieved from the {@link EncodingEntry} for a file and a file may consist of multiple data segments.
 *
 * @author Barre Dijkstra
 * @see EncodingEntry
 */
public interface IndexEntry {
    /**
     * Get the file key for the data segment.
     *
     * @return The file key.
     */
    FileKey getFileKey();

    /**
     * Get the size of the data segment.
     *
     * @return The size.
     */
    long getFileSize();

    /**
     * Get the CASC datafile number the data is in.
     *
     * @return The file number.
     *
     * @deprecated Replace with generic reference instead of CDN specific reference.
     */
    @Deprecated
    int getFileNumber();

    /**
     * Get the offset the datasegment in the CASC data file.
     *
     * @return The offset.
     *
     * @deprecated Replace with generic reference instead of CDN specific reference.
     */
    @Deprecated
    int getDataFileOffset();
}