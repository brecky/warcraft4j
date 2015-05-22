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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
        LOGGER.debug("Got a call for profile/new -> newProfile(%s, %s, %s)");
        return "profile.newProfile";
    }

    @GET
    @Path("/from/{region}/{realm}/{character}")
    public String createProfileFromCharacter(
            @PathParam("region") String region,
            @PathParam("realm") String realm,
            @PathParam("character") String character) {
        LOGGER.debug("Got a call for profile/from/{}/{}/{} -> createProfileFromCharacter({}, {}, {})", region, realm, character, region, realm, character);
        return "profile.createProfileFromCharacter";
    }
}
