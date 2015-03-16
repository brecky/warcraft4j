package nl.salp.warcraft4j.webapp.rest;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static java.lang.String.format;

/**
 * REST resource for exposing player character related API methods.
 *
 * @author Barre Dijkstra
 */
@Singleton
@Path("/profile")
@Produces(MediaType.TEXT_PLAIN)
public class PlayerCharacterProfileResource {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerCharacterProfileResource.class);

    @GET
    public String getCharacterServiceOverview() {
        return "Warcraft4J PlayerCharacterProfileResource";
    }

    @GET
    @Path("/new")
    public String newProfile() {

        LOGGER.debug(format("Got a call for profile/new -> newProfile(%s, %s, %s)"));

        return "profile.newProfile";
    }

    @GET
    @Path("/from/{region}/{realm}/{character}")
    public String createProfileFromCharacter(
            @PathParam("region") String region,
            @PathParam("realm") String realm,
            @PathParam("character") String character) {

        LOGGER.debug(format("Got a call for profile/from/%s/%s/%s -> createProfileFromCharacter(%s, %s, %s)", region, realm, character, region, realm, character));

        return "profile.createProfileFromCharacter";
    }
}
