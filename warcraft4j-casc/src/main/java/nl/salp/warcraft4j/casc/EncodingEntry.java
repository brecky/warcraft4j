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

import java.util.List;

/**
 * Encoding entry, linking a content checksum to one or more index entries.
 *
 * @author Barre Dijkstra
 */
public interface EncodingEntry {
    /**
     * Get the file size.
     *
     * @return The file size.
     */
    long getFileSize();

    /**
     * Get the content checksum of the file.
     *
     * @return The content checksum.
     */
    ContentChecksum getContentChecksum();

    /**
     * Get the first file key for the entry.
     *
     * @return The file key.
     */
    FileKey getFirstFileKey();

    /**
     * Get all file keys for the entry.
     *
     * @return The file keys (in parsing order).
     */
    List<FileKey> getFileKeys();

    /**
     * Check if the file referenced by the encoding entry consists of multiple data blocks (and thus index entries).
     *
     * @return {@code true} if the entry consists of multiple blocks.
     */
    boolean isMultiBlock();

    /**
     * Get the number of data blocks the referenced file consists of.
     *
     * @return The number of blocks.
     */
    int getBlockCount();
}
