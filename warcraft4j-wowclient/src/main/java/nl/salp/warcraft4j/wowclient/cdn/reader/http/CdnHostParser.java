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
import nl.salp.warcraft4j.wowclient.cdn.CdnException;
import nl.salp.warcraft4j.wowclient.cdn.CdnHost;
import nl.salp.warcraft4j.wowclient.cdn.CdnRegion;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * {@link LineParser} implementation for parsing CDN host information.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.wowclient.cdn.CdnHost
 * @see nl.salp.warcraft4j.wowclient.cdn.reader.http.HttpCdnReader
 */
class CdnHostParser implements LineParser<CdnHost> {
    /** The header of the CDN information file. */
    private static final String HEADER = "Name!STRING:0|Path!STRING:0|Hosts!STRING:0";
    /** The URI format mask for the URL to retrieve the CDN information from. */
    private static final String URI_MASK = "http://us.patch.battle.net/%s/cdns";

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
    public CdnHost parse(String line) throws CdnException {
        if (isEmpty(line)) {
            throw new CdnException("Can't parse an empty line into a CDN information instance.");
        }

        String[] data = line.split("\\|");
        if (data.length != 3) {
            throw new CdnException(format("Tried parse an invalid line into a CDN information instance: '%s'", line));
        }
        Region region = CdnRegion.getRegion(data[0]).getRegion();
        String path = data[1];
        String[] hosts;
        if (data[2].contains(" ")) {
            hosts = data[2].split(" ");
        } else {
            hosts = new String[]{data[2]};
        }
        return new CdnHost(region, path, hosts);
    }

}
