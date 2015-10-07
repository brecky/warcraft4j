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
 * Root entry, linking the hash of a filename to the {@link ContentChecksum} of the file.
 *
 * @author Barre Dijkstra
 */
public interface RootEntry {
    /**
     * The {@link ContentChecksum} of the file.
     *
     * @return The content checksum.
     */
    ContentChecksum getContentChecksum();

    /**
     * The hash of the filename.
     * <p>
     * The hash is a 64-bit Jenkins' hashlittle2 ({@link nl.salp.warcraft4j.hash.JenkinsHash#hashLittle2(byte[])}).
     * <p>
     * The hash is based on the all uppercase version of the filename with {@code /} characters being replaced with {@code \\}.
     *
     * @return The filename hash.
     */
    long getFilenameHash();

    /**
     * Get the flags for the entry.
     * <p>
     * Contains, amongst other things the value for the locale
     *
     * @return The flags.
     *
     * @see #getLocale()
     * @deprecated CDN specific implementation, remove and use wrappers such as {@link #getLocale()}.
     */
    @Deprecated
    long getFlags();

    /**
     * Get the {@link CascLocale} the entry is in.
     * FIXME: Refactor to return {@link nl.salp.warcraft4j.Locale} instead.
     *
     * @return Optional containing the locale of the entry if it's available.
     */
    default Optional<CascLocale> getLocale() {
        return CascLocale.getLocale(getFlags());
    }
}