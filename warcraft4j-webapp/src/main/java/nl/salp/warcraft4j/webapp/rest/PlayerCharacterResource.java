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
