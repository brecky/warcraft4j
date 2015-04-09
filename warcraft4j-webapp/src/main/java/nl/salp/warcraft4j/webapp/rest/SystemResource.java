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
