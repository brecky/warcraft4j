package nl.salp.warcraft4j.webapp.rest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.salp.warcraft4j.webapp.system.SystemService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
@Singleton
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class SystemResource {
    @Inject
    private SystemService systemService;

    @GET
    public String getIdentifier() {
        return format("Warcraft4J web application REST API - version %s", systemService.getApplicationVersion());
    }

    @GET
    @Path("/version")
    public String getVersion() {
        return String.valueOf(systemService.getServiceVersion());
    }
}
