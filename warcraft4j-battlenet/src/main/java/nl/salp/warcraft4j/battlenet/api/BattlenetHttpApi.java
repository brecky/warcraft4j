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

import com.google.inject.Singleton;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.battlenet.BattlenetRegion;
import nl.salp.warcraft4j.battlenet.BattlenetLocale;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static java.lang.String.format;

/**
 * {@link BattlenetApi} basic HTTP implementation
 *
 * @author Barre Dijkstra
 */
@Singleton
public class BattlenetHttpApi extends BattlenetApi {
    /** The logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(BattlenetHttpApi.class);
    /** The scheme. */
    private static final String BATTLENETAPI_SCHEME = "https";
    /** The base URI. */
    private static final String BATTLENETAPI_SERVER_MASK = "%s.api.battle.net";
    /** The parameter name for the locale parameter. */
    private static final String PARAMETER_LOCALE = "locale";
    /** The parameter name for the API key parameter. */
    private static final String PARAMETER_API_KEY = "apikey";

    /**
     * Create a new BattlenetHttpService with the default region and locale.
     */
    public BattlenetHttpApi() {
        super();
    }

    /**
     * Create a new BattlenetHttpService.
     *
     * @param region   The region to retrieve the data from.
     * @param language The language to get the data in.
     */
    public BattlenetHttpApi(Region region, Locale language) {
        super(region, language);
    }

    @Override
    protected <T> String execute(BattlenetRegion region, BattlenetLocale locale, BattlenetApiRequest<T> method) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI methodUri = createUri(region, locale, method);
            LOGGER.debug("Calling Battle.NET method {} with URI {}", method.getClass().getName(), methodUri.toASCIIString());
            HttpUriRequest request = new HttpGet(methodUri);
            return execute(request, httpClient);
        }
    }

    /**
     * Execute the request and return the result body.
     *
     * @param request The request to execute.
     * @param client  The client to use for executing the request.
     *
     * @return The result body.
     *
     * @throws IOException When the call couldn't be executed or the server returned an error.
     */
    private String execute(HttpUriRequest request, CloseableHttpClient client) throws IOException {
        try (CloseableHttpResponse response = client.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() > 300) {
                LOGGER.error("Request to '{}' returned code {} with message '{}'", request.getURI().toASCIIString(), statusLine.getStatusCode(), statusLine.getReasonPhrase());
                throw new IOException(format("Error %d: %s", statusLine.getStatusCode(), statusLine.getReasonPhrase()));
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            LOGGER.debug("Request to '{}' successfully completed with code {}", request.getURI().toASCIIString(), statusLine.getStatusCode());
            return result;
        }
    }

    /**
     * Create the request URI to call on the default region and receiving responses in the default locale.
     *
     * @param method The method to create the URI for.
     * @param <T>    The return type of the method.
     *
     * @return The URI.
     *
     * @throws IOException When the URI could not be created.
     */
    protected <T> URI createUri(BattlenetApiRequest<T> method) throws IOException {
        return createUri(getRegion(), getLocale(), method);
    }

    /**
     * Create the request URI to call.
     *
     * @param region The Battle.NET API region to call.
     * @param locale The locale to receive the responses in.
     * @param method The method to create the URI for.
     * @param <T>    The return type of the method.
     *
     * @return The URI to call for execution.
     *
     * @throws IOException When the URI could not be created.
     */
    protected <T> URI createUri(BattlenetRegion region, BattlenetLocale locale, BattlenetApiRequest<T> method) throws IOException {
        URIBuilder builder = new URIBuilder()
                .setScheme(BATTLENETAPI_SCHEME)
                .setPath(method.getRequestUri())
                .addParameter(PARAMETER_API_KEY, getConfig().getBnetApiKey());
        if (region == null) {
            builder.setHost(format(BATTLENETAPI_SERVER_MASK, getRegion().getApiUri()));
        } else {
            builder.setHost(format(BATTLENETAPI_SERVER_MASK, region.getApiUri()));
        }
        if (locale == null) {
            builder.addParameter(PARAMETER_LOCALE, getLocale().getLocale());
        } else {
            builder.addParameter(PARAMETER_LOCALE, locale.getLocale());
        }

        for (Map.Entry<String, String> param : method.getRequestParameters().entrySet()) {
            builder.addParameter(param.getKey(), param.getValue());
        }

        try {
            return builder.build();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
