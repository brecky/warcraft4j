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
package nl.salp.warcraft4j.clientdata.casc;

import nl.salp.warcraft4j.Region;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public enum CascRegion {
    /** United States. */
    US(Region.UNITED_STATES, "us"),
    /** Europe */
    EU(Region.EUROPE, "eu"),
    /** Russia. */
    RU(Region.RUSSIA, "ru", "xx"),
    /** China. */
    CN(Region.CHINA, "cn"),
    /** Korea. */
    KR(Region.KOREA, "kr"),
    /** Taiwan. */
    TW(Region.TAIWAN, "tw"),
    /** South East Asia. */
    SEA(Region.SOUTH_EAST_ASIA, "us");

    /** The {@link Region} for the CASC region. */
    private final Region region;
    /** The region codes for the region. */
    private final String[] regionCodes;

    /**
     * Create a new CASC region.
     *
     * @param region      The region.
     * @param regionCodes The region codes for the region.
     */
    private CascRegion(Region region, String... regionCodes) {
        this.region = region;
        this.regionCodes = regionCodes;
    }

    /**
     * Get the {@link Region} for the CASC region.
     *
     * @return The region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Get the CASC region codes for the region.
     *
     * @return The region codes.
     */
    public String[] getRegionCodes() {
        return regionCodes;
    }

    /**
     * Get the {@link CascRegion} for the given region code.
     *
     * @param code The region code.
     *
     * @return The CASC region or {@code null} if no matching region was found.
     */
    public static CascRegion getRegion(String code) {
        CascRegion region = null;
        for (CascRegion r : CascRegion.values()) {
            for (String c : r.regionCodes) {
                if (c.equals(code)) {
                    region = r;
                    break;
                }
            }
        }
        return region;
    }


    /**
     * Get the {@link CascRegion} for the given {@link Region}.
     *
     * @param region The region.
     *
     * @return The CASC region or {@code null} if no matching region was found.
     */
    public static CascRegion getRegion(Region region) {
        CascRegion cascRegion = null;
        for (CascRegion r : CascRegion.values()) {
            if (r.region == region) {
                cascRegion = r;
                break;
            }
        }
        return cascRegion;
    }
}
