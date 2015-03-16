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

import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.wowclient.cdn.Application;
import nl.salp.warcraft4j.wowclient.cdn.ApplicationVersion;
import nl.salp.warcraft4j.wowclient.cdn.CdnException;
import nl.salp.warcraft4j.wowclient.cdn.CdnRegion;

import java.util.StringTokenizer;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * {@link LineParser} implementation for parsing CDN host information.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.wowclient.cdn.ApplicationVersion
 * @see nl.salp.warcraft4j.wowclient.cdn.reader.http.HttpCdnReader
 */

class ApplicationVersionParser implements LineParser<ApplicationVersion> {
    /** The header of the version file. */
    private static final String HEADER = "Region!STRING:0|BuildConfig!HEX:16|CDNConfig!HEX:16|BuildId!DEC:4|VersionsName!String:0";
    /** The URI format mask for the URL to retrieve the CDN information from. */
    private static final String URI_MASK = "http://us.patch.battle.net/%s/versions";

    @Override
    public String getCdnUrl(Application application) throws CdnException {
        if (application == null) {
            throw new CdnException("Can't get the CDN URL for a null application.");
        }
        return format(URI_MASK, application.getKey());
    }

    @Override
    public boolean isValidHeader(String line) {
        return isNotEmpty(line) && HEADER.equals(line.trim());
    }

    @Override
    public ApplicationVersion parse(String line) throws CdnException {
        if (isEmpty(line)) {
            throw new CdnException("Can't parse an empty line into a CDN application version.");
        }
        StringTokenizer tokenizer = new StringTokenizer(line, "|");
        if (tokenizer.countTokens() != 5) {
            throw new CdnException(format("Tried parse an invalid line into a CDN application version: '%s'", line));
        }
        Region region = CdnRegion.getRegion(tokenizer.nextToken()).getRegion();
        String buildConfig = tokenizer.nextToken();
        String cdnConfig = tokenizer.nextToken();
        String buildId = tokenizer.nextToken();
        String versionName = tokenizer.nextToken();
        return new ApplicationVersion(region, buildConfig, cdnConfig, buildId, versionName);
    }
}
