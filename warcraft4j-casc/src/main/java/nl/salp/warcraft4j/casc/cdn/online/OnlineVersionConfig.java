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
package nl.salp.warcraft4j.casc.cdn.online;

import nl.salp.warcraft4j.Branch;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public enum OnlineVersionConfig {
    WOW_LIVE("wow"),
    WOW_PTR("wowt"),
    WOW_BETA("wow_beta");

    private final String productCode;

    OnlineVersionConfig(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public static OnlineVersionConfig getFrom(Branch branch) throws IllegalArgumentException {
        switch (branch) {
            case BETA:
                return WOW_BETA;
            case LIVE:
                return WOW_LIVE;
            case TESTING:
                return WOW_PTR;
            default:
                throw new IllegalArgumentException(format("Unable to find a CDN version for branch %s", branch));
        }
    }
}
