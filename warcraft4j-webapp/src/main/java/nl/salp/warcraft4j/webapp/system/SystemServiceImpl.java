package nl.salp.warcraft4j.webapp.system;

import com.google.inject.Inject;
import nl.salp.warcraft4j.webapp.Warcraft4JConfig;

/**
 * TODO Document class.
 */
public class SystemServiceImpl implements SystemService {
    @Inject
    private Warcraft4JConfig config;

    @Override
    public int getServiceVersion() {
        return config.getServiceVersion();
    }

    @Override
    public String getRestApiBaseUrl() {
        return config.getRestServiceBaseUri();
    }

    @Override
    public String[] getRestServicePackages() {
        return config.getServicePackages();
    }

    @Override
    public String getApplicationVersion() {
        return config.getApplicationVersion();
    }
}
