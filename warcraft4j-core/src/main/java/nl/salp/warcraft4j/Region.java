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

package nl.salp.warcraft4j;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableSet;
import static nl.salp.warcraft4j.Locale.*;

/**
 * World of Warcraft regions.
 * <p>
 * Supported locale lists for the regions are based on <a href="https://us.battle.net/support/en/article/check-the-game-region">a battle.net support article</a>.
 *
 * @author Barre Dijkstra
 */
public enum Region {
    /** American region (North-, Central-, South-America and Canada). */
    AMERICAS(EN_US, ES_MX, PT_BR),
    /** European region. */
    EUROPE(EN_GB, DE_DE, ES_ES, FR_FR, IT_IT, PT_PT, RU_RU),
    /** Korean region (probably South only due to export restrictions). */
    KOREA(KO_KR),
    /** Taiwanese region. */
    TAIWAN(EN_TW, ZH_TW),
    /** Chinese region. */
    CHINA(EN_CN, ZH_CN),
    /** SEA and Australasian region (Southeast Asia, Australia and New Zealand). */
    SEA_AUSTRALASIA(EN_SG);

    /** The supported locales for the region. */
    private final Set<Locale> supportedLocales;

    /**
     * Create a new region instance.
     *
     * @param supportedLocales The supported locales for the region.
     */
    Region(Locale... supportedLocales) {
        this.supportedLocales = Stream.of(supportedLocales)
                .distinct()
                .collect(Collectors.toSet());
    }

    /**
     * Get the supported locales for the region.
     *
     * @return The supported locales.
     */
    public Set<Locale> getSupportedLocales() {
        return unmodifiableSet(supportedLocales);
    }

    /**
     * Check if a locale is supported by the region.
     *
     * @param locale The locale.
     *
     * @return {@code true} if the locale is supported.
     */
    public boolean isSupported(Locale locale) {
        return Optional.ofNullable(locale)
                .map(this.supportedLocales::contains)
                .orElse(false);
    }

    /**
     * Get a region by its name.
     *
     * @param name The name of the region.
     *
     * @return Optional of the region.
     */
    public static Optional<Region> getRegion(String name) {
        return Optional.ofNullable(name)
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .map(String::toUpperCase)
                .flatMap(n -> Stream.of(Region.values()).filter(l -> l.name().equals(n)).findFirst());
    }

}
