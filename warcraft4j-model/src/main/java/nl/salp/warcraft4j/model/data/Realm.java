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

package nl.salp.warcraft4j.model.data;

import nl.salp.warcraft4j.Region;

/**
 * Server realm.
 *
 * @author Barre Dijkstra
 */
public class Realm extends StaticDataEntity {
    /** The region of the realm. */
    private final Region region;
    /** The slug (short name) of the realm. */
    private final String slug;
    /** The locale of the realm. */
    private final String locale;
    /** The timezone of the realm. */
    private final String timezone;
    /** The connected realms. */
    private final Realm[] connectedRealms;
    /** The realm type. */
    private final RealmType realmType;

    /**
     * Create a new StaticDataEntity.
     *
     * @param wowId           The ID of the entity as used in World of Warcraft.
     * @param name            The name of the entity.
     * @param region          The region of the realm.
     * @param slug            The slug (short name) of the realm.
     * @param locale          The locale of the realm.
     * @param timezone        The timezone of the realm.
     * @param connectedRealms The connected realms.
     * @param realmType       The realm type.
     */
    public Realm(long wowId, String name, Region region, String slug, String locale, String timezone, Realm[] connectedRealms, RealmType realmType) {
        super(wowId, name);
        this.region = region;
        this.slug = slug;
        this.locale = locale;
        this.timezone = timezone;
        if (connectedRealms == null) {
            this.connectedRealms = new Realm[0];
        } else {
            this.connectedRealms = connectedRealms;
        }
        this.realmType = realmType;
    }

    /**
     * Get the region of the realm.
     *
     * @return The region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Get the slug (short name) of the realm.
     *
     * @return The slug.
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Get the locale of the realm.
     *
     * @return The locale.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Get the timezone of the realm.
     *
     * @return The timezone.
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Get the connected realms.
     *
     * @return The connected realms.
     */
    public Realm[] getConnectedRealms() {
        return connectedRealms;
    }

    /**
     * Get the realm type.
     *
     * @return The realm type.
     */
    public RealmType getRealmType() {
        return realmType;
    }
}
