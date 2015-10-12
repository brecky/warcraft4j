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

import nl.salp.warcraft4j.Locale;

/**
 * Battle.NET language locale.
 *
 * @author Barre Dijkstra
 * @see Locale
 */
public enum BattlenetLocale {
    /** English language. */
    ENGLISH(Locale.EN_GB, "en_GB"),
    /** German language. */
    GERMAN(Locale.DE_DE, "de_DE"),
    /** Spanish language. */
    SPANISH(Locale.ES_ES, "es_ES"),
    /** French language. */
    FRENCH(Locale.FR_FR, "fr_FR"),
    /** Italian language. */
    ITALIAN(Locale.IT_IT, "it_IT"),
    /** Portuguese language. */
    PORTUGUESE(Locale.PT_PT, "pt_PT"),
    /** Russian language. */
    RUSSIAN(Locale.RU_RU, "ru_RU");

    /** The language. */
    private final Locale language;
    /** The Battle.NET locale for the language. */
    private final String locale;

    /**
     * Create a new BattlenetLocale.
     *
     * @param language The language.
     * @param locale   The Battle.NET locale for the language.
     */
    private BattlenetLocale(Locale language, String locale) {
        this.language = language;
        this.locale = locale;
    }

    /**
     * Get the language.
     *
     * @return The language.
     */
    public Locale getLanguage() {
        return language;
    }

    /**
     * Get the Battle.NET locale for the language.
     *
     * @return The locale.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Get the BattlenetLocale for the given language.
     *
     * @param language The language.
     *
     * @return The locale or {@code null} if no locale was found for the given language.
     */
    public static BattlenetLocale getLocale(Locale language) {
        BattlenetLocale locale = null;
        for (BattlenetLocale l : BattlenetLocale.values()) {
            if (l.getLanguage() == language) {
                locale = l;
                break;
            }
        }
        return locale;
    }


    /**
     * Get the BattlenetLocale for the given locale key.
     *
     * @param localeKey The locale key.
     *
     * @return The locale or {@code null} if no locale was found for the given locale key.
     */
    public static BattlenetLocale getLocale(String localeKey) {
        BattlenetLocale locale = null;
        for (BattlenetLocale l : BattlenetLocale.values()) {
            if (l.getLocale().equalsIgnoreCase(localeKey)) {
                locale = l;
                break;
            }
        }
        return locale;
    }
}
