package nl.salp.warcraft4j.webapp;

/**
 * TODO Document class.
 */
public interface Warcraft4JConfig {
    String[] getServicePackages();

    int getServiceVersion();

    String getRestServiceBaseUri();

    String getApplicationVersion();
}
