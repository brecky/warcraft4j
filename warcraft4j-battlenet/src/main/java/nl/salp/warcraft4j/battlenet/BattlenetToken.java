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

package nl.salp.warcraft4j.battlenet;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Battle.NET oAuth token.
 *
 * @author Barre Dijkstra
 */
public class BattlenetToken {
    /** Default token validity in seconds (30 days, as per Battle.NET API documentation). */
    public static final long DEFAULT_TOKEN_VALIDITY_SECONDS = 2592000;
    /**
     * The (unique) application name, as registered with the Battle.NET API.
     *
     * @see BattlenetApiConfig#getApplicationName()
     */
    private final String applicationName;
    /** The oAuth authentication token. */
    private final String token;
    /** The type of oAuth token. */
    private final String tokenType;
    /** The scope the token was issued for or {@code null} if no scope was provided. */
    private final String scope;
    /** The time ({@code EPOCH}) the token was issued. */
    private final long authenticationTime;
    /** The number of seconds the token is valid. */
    private final long validitySeconds;
    /** The expiration time ({@code EPOCH}) of the token. */
    private final long expirationTime;

    /**
     * Create a new Battle.NET authentication token with the default validity duration ({@link BattlenetToken#DEFAULT_TOKEN_VALIDITY_SECONDS}).
     *
     * @param applicationName    The (unique) application name, as registered with the Battle.NET API.
     * @param token              The oAuth authentication token.
     * @param tokenType          The type of oAuth token.
     * @param scope              The scope the token was issued for (or {@code null} if no scope was provided.
     * @param authenticationTime The time ({@code EPOCH}) the token was issued.
     */
    public BattlenetToken(String applicationName, String token, String tokenType, String scope, long authenticationTime) {
        this(applicationName, token, tokenType, scope, authenticationTime, DEFAULT_TOKEN_VALIDITY_SECONDS);
    }

    /**
     * Create a new Battle.NET authentication token.
     *
     * @param applicationName    The (unique) application name, as registered with the Battle.NET API.
     * @param token              The oAuth authentication token.
     * @param tokenType          The type of oAuth token.
     * @param scope              The scope the token was issued for (or {@code null} if no scope was provided).
     * @param authenticationTime The time ({@code EPOCH}) the token was issued.
     * @param validitySeconds    The number of seconds the token is valid.
     */
    public BattlenetToken(String applicationName, String token, String tokenType, String scope, long authenticationTime, long validitySeconds) {
        this.applicationName = applicationName;
        this.token = token;
        this.tokenType = tokenType;
        this.scope = scope;
        this.authenticationTime = authenticationTime;
        this.validitySeconds = validitySeconds;
        this.expirationTime = authenticationTime + (validitySeconds * 1000);
    }

    /**
     * Get the (unique) application name, as registered with the Battle.NET API.
     *
     * @return The application name.
     *
     * @see BattlenetApiConfig#getApplicationName()
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * The oAuth authentication token.
     *
     * @return The token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Get the type of oAuth token.
     *
     * @return The token type.
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Get the scope the token was issued for.
     *
     * @return The scope or {@code null} if no scope was provided.
     */
    public String getScope() {
        return scope;
    }

    /**
     * Get the time ({@code EPOCH}) the token was issued.
     *
     * @return The time of authentication.
     */
    public long getAuthenticationTime() {
        return authenticationTime;
    }

    /**
     * Get the number of seconds after which the token expires.
     *
     * @return The validity in seconds.
     */
    public long getValiditySeconds() {
        return validitySeconds;
    }

    /**
     * Check if the token expired.
     *
     * @return {@code true} if the token expired, {@code false} if not.
     */
    public boolean isExpired() {
        return System.currentTimeMillis() < expirationTime;
    }

    /**
     * Check if the token is valid.
     *
     * @return {@code true} if the token does not contain invalid information and is not expired.
     */
    public boolean isValid() {
        return isNotEmpty(applicationName) && isNotEmpty(token) && !isExpired();
    }
}
