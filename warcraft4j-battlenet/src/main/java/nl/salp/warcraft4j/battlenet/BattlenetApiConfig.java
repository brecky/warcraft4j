package nl.salp.warcraft4j.battlenet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration for the Battle.NET API.
 *
 * @author Barre Dijkstra
 */
public class BattlenetApiConfig {
    /** Default maximum number of requests per second allowed by the BattleNET API. */
    public static final int MAX_REQUESTS_PER_SECOND_DEFAULT = 100;
    /** Default maximum number of requests per hour allowed by the BattleNET API. */
    public static final int MAX_REQUESTS_PER_HOUR_DEFAULT = 36000;
    /** The default configuration file name. */
    public static final String CONFIG_FILENAME_DEFAULT = "w4j_bnet.config";
    /** Configuration file key for the application name. */
    private static final String CONFIG_KEY_APPLICATION_NAME = "w4j.bnet.application";
    /** Configuration file key for the API key. */
    private static final String CONFIG_KEY_API_KEY = "w4j.bnet.auth.key";
    /** Configuration file key for the API secret key. */
    private static final String CONFIG_KEY_API_SECRET = "w4j.bnet.auth.secret";
    /** Configuration file key for the maximum number of API requests per second. */
    private static final String CONFIG_KEY_MAX_REQUESTS_SECOND = "w4j.bnet.maxrequests.second";
    /** Configuration file key for the maximum number of API requests per hour. */
    private static final String CONFIG_KEY_MAX_REQUESTS_HOUR = "w4j.bnet.maxrequests.hour";

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

    /**
     * Create a new Battle.NET API configuration.
     *
     * @param applicationName  The name of the application, as registered with the Battle.NET API.
     * @param bnetApiKey       The Battle.NET API key.
     * @param bnetApiSecretKey The Battle.NET API secret key.
     *
     * @throws ConfigurationException When invalid configuration data was provided.
     */
    public BattlenetApiConfig(String applicationName, String bnetApiKey, String bnetApiSecretKey) throws ConfigurationException {
        this(applicationName, bnetApiKey, bnetApiSecretKey, MAX_REQUESTS_PER_SECOND_DEFAULT, MAX_REQUESTS_PER_HOUR_DEFAULT);
    }

    /**
     * Create a new Battle.NET API configuration.
     *
     * @param applicationName   The name of the application, as registered with the Battle.NET API.
     * @param bnetApiKey        The Battle.NET API key.
     * @param bnetApiSecretKey  The Battle.NET API secret key.
     * @param maxRequestsSecond The maximum number of requests per second allowed by the Battle.NET API for the application.
     * @param maxRequestsHour   The maximum number of requests per hour allowed by the Battle.NET API for the application.
     *
     * @throws ConfigurationException When invalid configuration data was provided.
     */
    public BattlenetApiConfig(String applicationName, String bnetApiKey, String bnetApiSecretKey, int maxRequestsSecond, int maxRequestsHour) throws ConfigurationException {
        if (applicationName == null || applicationName.isEmpty()) {
            throw new ConfigurationException("Null or empty application names are not allowed.");
        }
        this.applicationName = applicationName;

        if (bnetApiKey == null || bnetApiKey.isEmpty()) {
            throw new ConfigurationException("Null or empty API keys are not allowed.");
        }
        this.bnetApiKey = bnetApiKey;

        if (bnetApiSecretKey == null || bnetApiSecretKey.isEmpty()) {
            throw new ConfigurationException("Null or empty API secret keys are not allowed.");
        }
        this.bnetApiSecretKey = bnetApiSecretKey;

        if (maxRequestsSecond <= 0) {
            throw new ConfigurationException("The maximum allowed number of API requests per second must be greater then 0.");
        }
        this.maxRequestsSecond = maxRequestsSecond;

        if (maxRequestsHour <= 0) {
            throw new ConfigurationException("The maximum allowed number of API requests per hour must be greater then 0.");
        }
        this.maxRequestsHour = maxRequestsHour;
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
     * Create a new BnetApiConfig from the default ({@link BattlenetApiConfig#CONFIG_FILENAME_DEFAULT}) configuration file.
     *
     * @return The created config.
     *
     * @throws IOException            When the file could not be read.
     * @throws ConfigurationException When the configuration data is invalid.
     */
    public static BattlenetApiConfig fromConfigFile() throws IOException, ConfigurationException {
        return fromConfigFile(CONFIG_FILENAME_DEFAULT);
    }

    /**
     * Create a new BnetApiConfig from the provided configuration file.
     *
     * @return The created config.
     *
     * @throws IOException            When the file could not be read.
     * @throws ConfigurationException When the configuration data is invalid.
     */
    public static BattlenetApiConfig fromConfigFile(String configFile) throws IOException, ConfigurationException {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(configFile)) {
            properties.load(is);
        }
        String applicationName = properties.getProperty(CONFIG_KEY_APPLICATION_NAME);
        String apiKey = properties.getProperty(CONFIG_KEY_API_KEY);
        String apiSecretKey = properties.getProperty(CONFIG_KEY_API_SECRET);
        int maxRequestsSecond = Integer.parseInt(properties.getProperty(CONFIG_KEY_MAX_REQUESTS_SECOND, String.valueOf(MAX_REQUESTS_PER_SECOND_DEFAULT)));
        int maxRequestsHour = Integer.parseInt(properties.getProperty(CONFIG_KEY_MAX_REQUESTS_HOUR, String.valueOf(MAX_REQUESTS_PER_HOUR_DEFAULT)));

        return new BattlenetApiConfig(applicationName, apiKey, apiSecretKey, maxRequestsSecond, maxRequestsHour);
    }
}
