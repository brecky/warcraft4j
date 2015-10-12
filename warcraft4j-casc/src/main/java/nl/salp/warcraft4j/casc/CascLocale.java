/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License), Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing),
 * software distributed under the License is distributed on an
 * "AS IS" BASIS), WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND), either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.salp.warcraft4j.casc;

import nl.salp.warcraft4j.Locale;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Locales available in the CASC.
 * <p>
 * FIXME These locales are CASC specific and contain flags that are only used in the CDN files.
 *
 * @author Barre Dijkstra
 * @deprecated Refactor to use {@link nl.salp.warcraft4j.Locale} and move the mapping to the warcraft4j-casc-cdn module.
 */
@Deprecated
public enum CascLocale {
    /**
     * Unknown locale.
     * <p>
     * Mask: {@code 00000000 00000000 00000000 00000001}
     * </p>
     */
    UNKNOWN_1(0x1),
    /**
     * English (United States).
     * <p>
     * Mask: {@code 00000000 00000000 00000000 00000010}
     * </p>
     *
     * @see Locale#EN_US
     */
    EN_US(0x2),
    /**
     * Korean (Republic of Korea).
     * <p>
     * Mask: {@code 00000000 00000000 00000000 00000100}
     * </p>
     *
     * @see Locale#KO_KR
     */
    KO_KR(0x4),
    /**
     * Unknown locale.
     * <p>
     * Mask: {@code 00000000 00000000 00000000 00001000}
     * </p>
     */
    UNKNOWN_8(0x8),
    /**
     * French (France).
     * <p>
     * Mask: {@code 00000000 00000000 00000000 00010000}   !
     * </p>
     *
     * @see Locale#FR_FR
     */
    FR_FR(0x10),
    /**
     * German (Germany).
     * <p>
     * Mask: {@code 00000000 00000000 00000000 00100000}   !
     * </p>
     *
     * @see Locale#DE_DE
     */
    DE_DE(0x20),
    /**
     * Simplified Chinese (China).
     * <p>
     * Mask: {@code 00000000 00000000 00000000 01000000}   !
     * </p>
     *
     * @see Locale#ZH_CN
     */
    ZH_CN(0x40),
    /**
     * Spanish (Spain).
     * <p>
     * Mask: {@code 00000000 00000000 00000000 10000000}   !
     * </p>
     *
     * @see Locale#ES_ES
     */
    ES_ES(0x80),
    /**
     * Traditional Chinese (Taiwan).
     * <p>
     * Mask: {@code 00000000 00000000 00000001 00000000}   !
     * </p>
     *
     * @see Locale#ZH_TW
     */
    ZH_TW(0x100),
    /**
     * English (Great Britain).
     * <p>
     * Mask: {@code 00000000 00000000 00000010 00000000}   !
     * </p>
     *
     * @see Locale#EN_GB
     */
    EN_GB(0x200),
    /**
     * English (China).
     * <p>
     * Mask: {@code 00000000 00000000 00000100 00000000}   !
     * </p>
     *
     * @see Locale#EN_CN
     */
    EN_CN(0x400),
    /**
     * English (Taiwan).
     * <p>
     * Mask: {@code 00000000 00000000 00001000 00000000}   !
     * </p>
     *
     * @see Locale#EN_TW
     */
    EN_TW(0x800),
    /**
     * Spanish (Mexico).
     * <p>
     * Mask: {@code 00000000 00000000 00010000 00000000}   !
     * </p>
     *
     * @see Locale#ES_MX
     */
    ES_MX(0x1000),
    /**
     * Russian (Russia).
     * <p>
     * Mask: {@code 00000000 00000000 00100000 00000000}   !
     * </p>
     *
     * @see Locale#RU_RU
     */
    RU_RU(0x2000),
    /**
     * Portuguese (Brazil).
     * <p>
     * Mask: {@code 00000000 00000000 01000000 00000000}   !
     * </p>
     *
     * @see Locale#PT_BR
     */
    PT_BR(0x4000),
    /**
     * Italian (Italy).
     * <p>
     * Mask: {@code 00000000 00000000 10000000 00000000}   !
     * </p>
     *
     * @see Locale#IT_IT
     */
    IT_IT(0x8000),
    /**
     * Portuguese (Portugal).
     * <p>
     * Mask: {@code 00000000 00000001 00000000 00000000}   !
     * </p>
     *
     * @see Locale#PT_PT
     */
    PT_PT(0x10000),
    /**
     * No locale.
     * <p>
     * Mask: {@code 00000000 00000000 00000000 00000000}
     * </p>
     */
    NONE(0),
    /**
     * All locales.
     * <p>
     * Mask: {@code 11111111 11111111 11111111 11111111}
     * </p>
     */
    ALL(-1);

    /** The flag for the locale. */
    private final long flag;

    /**
     * Create a new instance.
     *
     * @param flag The flag for the locale.
     */
    CascLocale(long flag) {
        this.flag = flag;
    }

    /**
     * Get the flag for the locale.
     *
     * @return The flag.
     */
    public long getFlag() {
        return flag;
    }

    /**
     * Convert to a {@link Locale}.
     *
     * @return Optional containing the locale if available.
     */
    public Optional<Locale> toLocale() {
        return Locale.getLocale(name());
    }

    /**
     * Get the locale for a flag.
     *
     * @param flag The flag.
     *
     * @return Optional containing the locale if one is available for a flag.
     */
    public static Optional<CascLocale> getLocale(long flag) {
        Optional<CascLocale> locale;
        if (flag == ALL.flag) {
            locale = Optional.of(ALL);
        } else {
            locale = Stream.of(CascLocale.values())
                    .filter(l -> l != ALL && (l.flag & flag) == l.flag)
                    .findFirst();
        }
        return locale;
    }

    /**
     * Get the locale for a name.
     *
     * @param name The name.
     *
     * @return Optional containing the locale if one is available for a name.
     */
    public static Optional<CascLocale> getLocale(String name) {
        return Stream.of(CascLocale.values())
                .filter(l -> l.name().equalsIgnoreCase(name))
                .findFirst();
    }
}