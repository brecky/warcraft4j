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

package nl.salp.warcraft4j.battlenet.api;

import com.google.inject.Inject;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.battlenet.BattlenetApiConfig;
import nl.salp.warcraft4j.battlenet.BattlenetRegion;
import nl.salp.warcraft4j.battlenet.BattlenetLocale;

import java.io.IOException;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Battle.NET API service.
 *
 * @author Barre Dijkstra
 */
public abstract class BattlenetApi {
    /** The configuration. */
    @Inject
    private BattlenetApiConfig config;
    /** The {@link JsonApiResultParser} instance to use for parsing the results. */
    @Inject
    private JsonApiResultParser resultParser;
    /** The region to get the data from. */
    private BattlenetRegion region;
    /** The locale to get the data in. */
    private BattlenetLocale locale;

    /**
     * Create a new BattlenetHttpService with the default region and locale.
     */
    public BattlenetApi() {
    }

    /**
     * Create a new BattlenetHttpService.
     *
     * @param region   The region to retrieve the data from.
     * @param language The language to get the data in.
     */
    public BattlenetApi(Region region, Locale language) {
        this.region = BattlenetRegion.getRegionForKey(region);
        this.locale = BattlenetLocale.getLocale(language);
    }

    /**
     * Get the default Battle.NET region to use.
     *
     * @return The region.
     */
    protected final synchronized BattlenetRegion getRegion() {
        BattlenetRegion r;
        if (region == null) {
            r = config.getDefaultRegion();
            this.region = r;
        } else {
            r = region;
        }
        return r;
    }

    /**
     * Get the default Battle.NET locale to use.
     *
     * @return The locale.
     */
    protected final synchronized BattlenetLocale getLocale() {
        BattlenetLocale l;
        if (locale == null) {
            l = config.getDefaultLocale();
            this.locale = l;
        } else {
            l = locale;
        }
        return l;
    }

    /**
     * Get the {@link BattlenetApiConfig} instance to use.
     *
     * @return The instance to use.
     */
    protected final BattlenetApiConfig getConfig() {
        return this.config;
    }

    /**
     * Call a Battle.NET API service method using the default configured region and locale.
     *
     * @param method The method to call.
     * @param <T>    The type of the method result.
     *
     * @return The result.
     *
     * @throws IOException                  When there was a problem in the communication with the Battle.NET API.
     * @throws BattlenetApiParsingException When the data returned by the Battle.NET API could not be parsed.
     */
    public <T> T call(BattlenetApiRequest<T> method) throws IOException, BattlenetApiParsingException {
        if (method == null) {
            throw new BattlenetApiParsingException("Can't execute a null Battle.NET API request.");
        }
        String result = execute(getRegion(), getLocale(), method);
        if (isEmpty(result)) {
            throw new BattlenetApiParsingException(format("Execution of method %s returned an empty result.", method.getClass().getName()));
        }
        return resultParser.parse(result, method.getResultType());
    }


    /**
     * Call a Battle.NET API service method.
     *
     * @param region The Battle.NET API region to call.
     * @param locale The i18n language to make the request for.
     * @param method The method to call.
     * @param <T>    The type of the method result.
     *
     * @return The result.
     *
     * @throws IOException                  When there was a problem in the communication with the Battle.NET API.
     * @throws BattlenetApiParsingException When the data returned by the Battle.NET API could not be parsed.
     */
    public <T> T call(BattlenetRegion region, BattlenetLocale locale, BattlenetApiRequest<T> method) throws IOException, BattlenetApiParsingException {
        if (method == null) {
            throw new BattlenetApiParsingException("Can't execute a null Battle.NET API request.");
        }
        String result = execute(region, locale, method);
        if (isEmpty(result)) {
            throw new BattlenetApiParsingException(format("Execution of method %s returned an empty result.", method.getClass().getName()));
        }
        return resultParser.parse(result, method.getResultType());
    }

    /**
     * Execute the method and return the JSON result as String.
     *
     * @param region The region to execute the call for.
     * @param locale The locale to execute the call for.
     * @param method The method to execute.
     * @param <T>    The type of the method result.
     *
     * @return The JSON result as a String.
     *
     * @throws IOException When execution failed.
     */
    protected abstract <T> String execute(BattlenetRegion region, BattlenetLocale locale, BattlenetApiRequest<T> method) throws IOException;
}
