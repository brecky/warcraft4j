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
package nl.salp.warcraft4j.casc;

import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CascLocale}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.CascLocale
 */
public class CascLocaleTest {
    @Test
    public void shouldFindAllLocalesByFlag() {
        Stream.of(CascLocale.values())
                .forEach(locale -> assertEquals(locale, CascLocale.getLocale(locale.getFlag()).get()));
    }

    @Test
    public void shouldHaveUniqueFlags() {
        long uniqueFlags = Stream.of(CascLocale.values())
                .mapToLong(CascLocale::getFlag)
                .distinct()
                .count();
        assertEquals(CascLocale.values().length, uniqueFlags);
    }

    @Test
    public void shouldMapToWarcraft4jLocale() {
        Stream.of(CascLocale.values())
                .filter(locale -> locale != CascLocale.ALL)
                .filter(locale -> locale != CascLocale.NONE)
                .filter(locale -> locale != CascLocale.UNKNOWN_1)
                .filter(locale -> locale != CascLocale.UNKNOWN_8)
                .map(CascLocale::toLocale)
                .forEach(locale -> assertTrue(locale.isPresent()));
    }

    @Test
    public void shouldGetLocaleByCaseInsensitiveName() {
        assertFalse(CascLocale.getLocale("fa_il").isPresent());
        assertEquals(CascLocale.EN_CN, CascLocale.getLocale("EN_CN").get());
        assertEquals(CascLocale.EN_CN, CascLocale.getLocale("en_cn").get());
        assertEquals(CascLocale.EN_CN, CascLocale.getLocale("en_CN").get());
    }
}
