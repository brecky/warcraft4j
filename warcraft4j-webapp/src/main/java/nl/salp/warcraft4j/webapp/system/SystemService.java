package nl.salp.warcraft4j.webapp.system;

/**
 * System service.
 *
 * @author Barre Dijkstra
 */
public interface SystemService {
    int getServiceVersion();

    String getRestApiBaseUrl();

    String[] getRestServicePackages();

    String getApplicationVersion();
}
