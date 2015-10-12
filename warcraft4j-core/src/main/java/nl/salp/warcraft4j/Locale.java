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

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * World of Warcraft supported locales for i18n versions of data.
 *
 * @author Barre Dijkstra
 */
public enum Locale {
    /** American English. */
    EN_US,
    /** European English. */
    EN_GB,
    /** Chinese English. */
    EN_CN,
    /** Taiwan English. */
    EN_TW,
    /** Southeast Asia English. */
    EN_SG,
    /** European Portuguese. */
    PT_PT,
    /** Brazilian Portuguese. */
    PT_BR,
    /** European Spanish. */
    ES_ES,
    /** Mexican Spanish. */
    ES_MX,
    /** Chinese Mandarin. */
    ZH_CN,
    /** Taiwanese Mandarin. */
    ZH_TW,
    /** Korean. */
    KO_KR,
    /** French. */
    FR_FR,
    /** German. */
    DE_DE,
    /** Russian. */
    RU_RU,
    /** Italian. */
    IT_IT;

    /** The language code of the locale. */
    private final String languageCode;
    /** The region code of the locale. */
    private final String regionCode;

    /**
     * Create a new instance.
     */
    Locale() {
        this.languageCode = name().substring(0, 2).toLowerCase();
        this.regionCode = name().substring(3);
    }

    /**
     * Get the language code of the locale.
     *
     * @return The language code.
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Get the region code of the locale.
     *
     * @return The region code.
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * Check if the language of a locale overlaps with the language of the current locale.
     *
     * @param locale The locale to check.
     *
     * @return {@code true} if the language of both locales overlap.
     */
    public boolean isLanguageOverlapping(Locale locale) {
        return locale != null && languageCode.equals(locale.languageCode);
    }

    /**
     * Check if the region of a locale overlaps with the region of the current locale.
     *
     * @param locale The locale to check.
     *
     * @return {@code true} if the regions of both locales overlap.
     */
    public boolean isRegionOverlapping(Locale locale) {
        return locale != null && regionCode.equals(locale.regionCode);
    }

    /**
     * Get all locales of which the language overlaps with the current locale.
     *
     * @return The locales with overlapping language.
     */
    public Set<Locale> getLanguageOverlaps() {
        return Stream.of(Locale.values())
                .filter(l -> l != this)
                .filter(this::isLanguageOverlapping)
                .collect(Collectors.toSet());
    }

    /**
     * Get all locales of which the region overlaps with the current locale.
     *
     * @return The locales with overlapping region.
     */
    public Set<Locale> getRegionOverlaps() {
        return Stream.of(Locale.values())
                .filter(l -> l != this)
                .filter(this::isRegionOverlapping)
                .collect(Collectors.toSet());
    }

    /**
     * Get all locales for a language.
     *
     * @param languageCode The 2-letter language code for the language.
     *
     * @return The locales that match the language.
     */
    public static Set<Locale> getLocalesForLanguage(String languageCode) {
        return Optional.ofNullable(languageCode)
                .filter(StringUtils::isNotEmpty)
                .map(String::toLowerCase)
                .map(lang -> Stream.of(Locale.values())
                        .filter(locale -> locale.languageCode.equals(lang))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    /**
     * Get all locales for a region.
     *
     * @param regionCode The 2-letter region code for the region.
     *
     * @return The locales that match the region.
     */
    public static Set<Locale> getLocalesForRegion(String regionCode) {
        return Optional.ofNullable(regionCode)
                .filter(StringUtils::isNotEmpty)
                .map(String::toUpperCase)
                .map(region -> Stream.of(Locale.values())
                        .filter(locale -> locale.regionCode.equals(region))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    /**
     * Get a locale by its name.
     *
     * @param name The name of the locale.
     *
     * @return Optional of the locale.
     */
    public static Optional<Locale> getLocale(String name) {
        return Optional.ofNullable(name)
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .map(String::toUpperCase)
                .flatMap(n -> Stream.of(Locale.values())
                        .filter(l -> l.name().equals(n))
                        .findFirst());
    }
}
