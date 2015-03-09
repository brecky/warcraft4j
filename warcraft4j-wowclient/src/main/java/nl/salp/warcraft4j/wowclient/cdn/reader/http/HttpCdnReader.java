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
import nl.salp.warcraft4j.wowclient.cdn.CdnHost;
import nl.salp.warcraft4j.wowclient.cdn.reader.CdnReader;
import org.apache.http.client.fluent.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * {@link CdnReader} implementation that reads over HTTP.
 *
 * @author Barre Dijkstra
 */
public class HttpCdnReader implements CdnReader {

    @Override
    public List<CdnHost> getCdnHosts(Application application) throws CdnException {
        try {
            return read(new CdnHostParser(), application);
        } catch (IOException e) {
            throw new CdnException(format("Error reading host information from CDN for application %s", application), e);
        }
    }

    @Override
    public List<ApplicationVersion> getVersions(Application application) throws CdnException {
        try {
            return read(new ApplicationVersionParser(), application);
        } catch (IOException e) {
            throw new CdnException(format("Error reading version information from CDN for application %s", application), e);
        }
    }

    @Override
    public ApplicationVersion getVersion(Application application, Region region) throws CdnException {
        ApplicationVersion version = null;
        List<ApplicationVersion> versions = getVersions(application);
        for (ApplicationVersion v : versions) {
            if (v.getRegion() == region) {
                version = v;
                break;
            }
        }
        return version;
    }

    /**
     * Read and parse the result from a CDN HTTP call.
     *
     * @param parser      The parser to parse the result data with.
     * @param application The application to get the CDN URL for.
     * @param <T>         The type of data read.
     *
     * @return The read entries.
     *
     * @throws IOException  When the HTTP call failed.
     * @throws CdnException When the data couldn't be parsed.
     */
    private <T> List<T> read(LineParser<T> parser, Application application) throws IOException, CdnException {
        List<T> entries = new ArrayList<>();
        String url = parser.getCdnUrl(application);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Request.Get(url).execute().returnContent().asStream()))) {
            String header = reader.readLine();
            if (parser.isValidHeader(header)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    entries.add(parser.parse(line));
                }
            } else {
                throw new CdnException(format("CDN call to '%s' received an invalid header in the response: '%s'", header));
            }
        }
        return entries;
    }
}
