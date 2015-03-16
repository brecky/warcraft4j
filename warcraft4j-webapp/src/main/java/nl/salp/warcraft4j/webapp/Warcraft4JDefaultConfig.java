package nl.salp.warcraft4j.webapp;

import com.google.inject.Singleton;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
@Singleton
public class Warcraft4JDefaultConfig implements Warcraft4JConfig {
    /** The packages to automagically bind the services from. */
    private static final String[] SERVICE_PACKAGES = {
            "nl.salp.warcraft4j.webapp.rest"
    };
    /** The version of the service. */
    private static final int SERVICE_VERSION = 1;
    /** The version of the application. */
    private static final String APPLICATION_VERSION = "0.0.1-SNAPSHOT";
    /** The base URI for services. */
    private static final String REST_BASE_URI = format("/v%d/", SERVICE_VERSION);

    public String[] getServicePackages() {
        return SERVICE_PACKAGES;
    }

    public int getServiceVersion() {
        return SERVICE_VERSION;
    }

    public String getRestServiceBaseUri() {
        return REST_BASE_URI;
    }

    @Override
    public String getApplicationVersion() {
        return APPLICATION_VERSION;
    }
}
