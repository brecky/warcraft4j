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

package nl.salp.warcraft4j.battlenet;

import com.google.inject.Singleton;
import nl.salp.warcraft4j.config.Warcraft4jConfigException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Configuration for the Battle.NET API.
 *
 * @author Barre Dijkstra
 */
@Singleton
public class BattlenetApiConfig {
    /** Default maximum number of requests per second allowed by the BattleNET API. */
    public static final int MAX_REQUESTS_PER_SECOND_DEFAULT = 100;
    /** Default maximum number of requests per hour allowed by the BattleNET API. */
    public static final int MAX_REQUESTS_PER_HOUR_DEFAULT = 36000;
    /** The default region for the Battle.NET API. */
    public static final String REGION_DEFAULT = "eu";
    /** The default language for the Battle.NET API. */
    private static final String LOCALE_DEFAULT = "en_GB";
    /** The default configuration file name. */
    public static final String CONFIG_FILENAME_DEFAULT = "/w4j_bnet.config";
    /** Configuration file key for the application name. */
    private static final String CONFIG_KEY_APPLICATION_NAME = "w4j.bnet.api.application";
    /** Configuration file key for the API key. */
    private static final String CONFIG_KEY_API_KEY = "w4j.bnet.api.auth.key";
    /** Configuration file key for the API secret key. */
    private static final String CONFIG_KEY_API_SECRET = "w4j.bnet.api.auth.secret";
    /** Configuration file key for the maximum number of API requests per second. */
    private static final String CONFIG_KEY_MAX_REQUESTS_SECOND = "w4j.bnet.api.maxrequests.second";
    /** Configuration file key for the maximum number of API requests per hour. */
    private static final String CONFIG_KEY_MAX_REQUESTS_HOUR = "w4j.bnet.api.maxrequests.hour";
    /** Configuration file key for the default locale for the API. */
    private static final String CONFIG_DEFAULT_LOCALE = "w4j.bnet.api.default.locale";
    /** Configuration file key for the default region for the API. */
    private static final String CONFIG_DEFAULT_REGION = "w4j.bnet.api.default.region";

    /** The name of the application, as registered with the Battle.NET API. */
    private final String applicationName;
    /** The Battle.NET API key. */
    private final String bnetApiKey;
    /** The Battle.NET API secret key. */
    private final String bnetApiSecretKey;
    /** The maximum number of requests per second allowed by the Battle.NET API for the application. */
    private final int maxRequestsSecond;
    /** The maximum number of requests per hour allowed by the Battle.NET API for the application. */
    private final int maxRequestsHour;
    /** The default Battle.NET API region. */
    private BattlenetRegion defaultRegion;
    /** The default Battle.NET API locale. */
    private BattlenetLocale defaultLocale;

    /**
     * Create a new Battle.NET API configuration with the default values.
     *
     * @param applicationName  The name of the application, as registered with the Battle.NET API.
     * @param bnetApiKey       The Battle.NET API key.
     * @param bnetApiSecretKey The Battle.NET API secret key.
     *
     * @throws Warcraft4jConfigException When invalid configuration data was provided.
     */
    public BattlenetApiConfig(String applicationName, String bnetApiKey, String bnetApiSecretKey) throws Warcraft4jConfigException {
        this(applicationName, bnetApiKey, bnetApiSecretKey, MAX_REQUESTS_PER_SECOND_DEFAULT, MAX_REQUESTS_PER_HOUR_DEFAULT, REGION_DEFAULT, LOCALE_DEFAULT);
    }

    /**
     * Create a new Battle.NET API configuration.
     *
     * @param applicationName   The name of the application, as registered with the Battle.NET API.
     * @param bnetApiKey        The Battle.NET API key.
     * @param bnetApiSecretKey  The Battle.NET API secret key.
     * @param maxRequestsSecond The maximum number of requests per second allowed by the Battle.NET API for the application.
     * @param maxRequestsHour   The maximum number of requests per hour allowed by the Battle.NET API for the application.
     * @param defaultRegion     The default Battle.NET region to use.
     * @param defaultLocale     The default Battle.NET locale to use.
     *
     * @throws Warcraft4jConfigException When invalid configuration data was provided.
     */
    public BattlenetApiConfig(String applicationName, String bnetApiKey, String bnetApiSecretKey, int maxRequestsSecond, int maxRequestsHour, String defaultRegion,
            String defaultLocale) throws Warcraft4jConfigException {
        if (isEmpty(applicationName)) {
            throw new Warcraft4jConfigException("Null or empty application names are not allowed.");
        }
        this.applicationName = applicationName;

        if (isEmpty(bnetApiKey)) {
            throw new Warcraft4jConfigException("Null or empty API keys are not allowed.");
        }
        this.bnetApiKey = bnetApiKey;

        if (isEmpty(bnetApiSecretKey)) {
            throw new Warcraft4jConfigException("Null or empty API secret keys are not allowed.");
        }
        this.bnetApiSecretKey = bnetApiSecretKey;

        if (maxRequestsSecond <= 0) {
            throw new Warcraft4jConfigException("The maximum allowed number of API requests per second must be greater then 0.");
        }
        this.maxRequestsSecond = maxRequestsSecond;

        if (maxRequestsHour <= 0) {
            throw new Warcraft4jConfigException("The maximum allowed number of API requests per hour must be greater then 0.");
        }
        this.maxRequestsHour = maxRequestsHour;

        if (isEmpty(defaultRegion)) {
            throw new Warcraft4jConfigException("Null or empty default region setting is not allowed.");
        }
        this.defaultRegion = BattlenetRegion.getRegionForKey(defaultRegion);
        if (this.defaultRegion == null) {
            throw new Warcraft4jConfigException(format("Can't find a region with the key '%s'", defaultRegion));
        }

        if (isEmpty(defaultLocale)) {
            throw new Warcraft4jConfigException("Null or empty default locale setting is not allowed.");
        }
        this.defaultLocale = BattlenetLocale.getLocale(defaultLocale);
        if (this.defaultLocale == null) {
            throw new Warcraft4jConfigException(format("Can't find a locale with the key '%s'", defaultLocale));
        }
    }

    /**
     * Get the name of the application as it is registered with the Battle.NET API.
     *
     * @return The name.
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Get the Battle.NET API key for the application.
     *
     * @return The API key.
     */
    public String getBnetApiKey() {
        return bnetApiKey;
    }

    /**
     * Get the Battle.NET API secret key for the application.
     *
     * @return The API secret key.
     */
    public String getBnetApiSecretKey() {
        return bnetApiSecretKey;
    }

    /**
     * Get the maximum number of requests per second allowed by the Battle.NET API for the application.
     *
     * @return The maximum number of requests per second.
     */
    public int getMaxRequestsSecond() {
        return maxRequestsSecond;
    }

    /**
     * Get the maximum number of requests per hour allowed by the Battle.NET API for the application.
     *
     * @return The maximum number of requests per hour.
     */
    public int getMaxRequestsHour() {
        return maxRequestsHour;
    }

    /**
     * Get the default locale for the Battle.NET API calls.
     *
     * @return The default locale.
     */
    public BattlenetLocale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Get the default region for the Battle.NET API calls.
     *
     * @return The default region.
     */
    public BattlenetRegion getDefaultRegion() {
        return defaultRegion;
    }

    /**
     * Create a new BnetApiConfig from the default ({@link BattlenetApiConfig#CONFIG_FILENAME_DEFAULT}) configuration file.
     *
     * @return The created config.
     *
     * @throws IOException            When the file could not be read.
     * @throws Warcraft4jConfigException When the configuration data is invalid.
     */
    public static BattlenetApiConfig fromConfigFile() throws IOException, Warcraft4jConfigException {
        return fromConfigFile(CONFIG_FILENAME_DEFAULT);
    }

    /**
     * Create a new BnetApiConfig from the provided configuration file.
     *
     * @return The created config.
     *
     * @throws IOException            When the file could not be read.
     * @throws Warcraft4jConfigException When the configuration data is invalid.
     */
    public static BattlenetApiConfig fromConfigFile(String configFile) throws IOException, Warcraft4jConfigException {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(configFile)) {
            properties.load(is);
        } catch (IOException e) {
            try (InputStream is = BattlenetApiConfig.class.getResourceAsStream(configFile)) {
                if (is != null) {
                    properties.load(is);
                }
            }
        }
        String applicationName = properties.getProperty(CONFIG_KEY_APPLICATION_NAME);
        String apiKey = properties.getProperty(CONFIG_KEY_API_KEY);
        String apiSecretKey = properties.getProperty(CONFIG_KEY_API_SECRET);
        int maxRequestsSecond = Integer.parseInt(properties.getProperty(CONFIG_KEY_MAX_REQUESTS_SECOND, String.valueOf(MAX_REQUESTS_PER_SECOND_DEFAULT)));
        int maxRequestsHour = Integer.parseInt(properties.getProperty(CONFIG_KEY_MAX_REQUESTS_HOUR, String.valueOf(MAX_REQUESTS_PER_HOUR_DEFAULT)));
        String defaultRegion = properties.getProperty(CONFIG_DEFAULT_REGION, REGION_DEFAULT);
        String defaultLocale = properties.getProperty(CONFIG_DEFAULT_LOCALE, LOCALE_DEFAULT);

        return new BattlenetApiConfig(applicationName, apiKey, apiSecretKey, maxRequestsSecond, maxRequestsHour, defaultRegion, defaultLocale);
    }
}
