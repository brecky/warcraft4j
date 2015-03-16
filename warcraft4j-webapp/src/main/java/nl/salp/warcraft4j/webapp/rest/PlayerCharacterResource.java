package nl.salp.warcraft4j.webapp.rest;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static java.lang.String.format;

/**
 * REST resource for exposing player character related API methods.
 *
 * @author Barre Dijkstra
 */
@Singleton
@Path("/character")
@Produces(MediaType.TEXT_PLAIN)
public class PlayerCharacterResource {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerCharacterResource.class);

    @GET
    public String getCharacterServiceOverview() {
        return "Warcraft4J CharacterService";
    }

    @GET
    @Path("/{region}/{realm}/{character}")
    public String getPlayerCharacter(
            @PathParam("region") String region,
            @PathParam("realm") String realm,
            @PathParam("character") String character) {
        LOGGER.debug(format("Got a call for character/%s/%s/%s -> getPlayer(%s, %s, %s)", region, realm, character, region, realm, character));
        return "character.get";
    }
}
