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
 * TODO These locales are CASC specific and contain flags that are only used in the CDN files.
 *
 * @author Barre Dijkstra
 */
public enum CascLocale {
    ALL(-1),
    NONE(0),
    UNKNOWN_1(0x1),
    EN_US(0x2),
    KO_KR(0x4),
    UNKNOWN_8(0x8),
    FR_FR(0x10),
    DE_DE(0x20),
    ZH_CN(0x40),
    ES_ES(0x80),
    ZH_TW(0x100),
    EN_GB(0x200),
    EN_CN(0x400),
    EN_TW(0x800),
    ES_MX(0x1000),
    RU_RU(0x2000),
    PT_BR(0x4000),
    IT_IT(0x8000),
    PT_PT(0x10000);


    private final long flag;

    CascLocale(long flag) {
        this.flag = flag;
    }

    public long getFlag() {
        return flag;
    }

    public Optional<Locale> toLocale() {
        return Locale.getLocale(name());
    }

    public static Optional<CascLocale> getLocale(long flag) {
        return Stream.of(CascLocale.values())
                .filter(l -> (l.flag & flag) == flag)
                .findFirst();
    }


    public static Optional<CascLocale> getLocale(String name) {
        return Stream.of(CascLocale.values())
                .filter(l -> l.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
