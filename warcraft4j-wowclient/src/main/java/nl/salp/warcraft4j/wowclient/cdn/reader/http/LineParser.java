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

package nl.salp.warcraft4j.wowclient.cdn.reader.http;

import nl.salp.warcraft4j.wowclient.cdn.Application;
import nl.salp.warcraft4j.wowclient.cdn.CdnException;

/**
 * Parser for parsing a CDN file text line to a parsed instance.
 *
 * @param <T> The type of the parsed instance.
 *
 * @author Barre Dijkstra
 */
public interface LineParser<T> {
    /**
     * Get the CDN URL for the type and the given application.
     *
     * @param application The application to get the CDN URL for.
     *
     * @return The URL.
     *
     * @throws CdnException When the URL could not be constructed.
     */
    String getCdnUrl(Application application) throws CdnException;

    /**
     * Check if the line is a valid header for the type.
     *
     * @param line The line.
     *
     * @return {@code true} if the line is a valid header for the type.
     */
    boolean isValidHeader(String line);

    /**
     * Parse the line into an instance.
     *
     * @param line The line to parse.
     *
     * @return The instance of the parsed type.
     *
     * @throws CdnException When parsing failed.
     */
    T parse(String line) throws CdnException;
}
