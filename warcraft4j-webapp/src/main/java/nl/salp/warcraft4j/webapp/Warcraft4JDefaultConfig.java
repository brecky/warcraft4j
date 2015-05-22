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
