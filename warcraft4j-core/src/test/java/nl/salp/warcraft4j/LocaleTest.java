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

import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link nl.salp.warcraft4j.Locale}
 *
 * @author Barre Dijkstra
 */
public class LocaleTest {
    @Test
    public void shouldParseLanguageCode() {
        assertEquals("Language code not properly parsed from locale name.", "en", Locale.EN_US.getLanguageCode());
    }

    @Test
    public void shouldParseRegionCode() {
        assertEquals("Region code not properly parsed from locale name.", "US", Locale.EN_US.getRegionCode());
    }

    @Test
    public void shouldDetermineOverlappingLanguage() {
        assertTrue("Locales with overlapping languages not determined as overlapping.", Locale.EN_US.isLanguageOverlapping(Locale.EN_GB));
    }

    @Test
    public void shouldDetermineDifferentLanguage() {
        assertFalse("Locales with different languages determined as overlapping.", Locale.EN_US.isLanguageOverlapping(Locale.RU_RU));
    }

    @Test
    public void shouldDetermineOverlappingRegion() {
        assertTrue("Locales with overlapping regions not determined as overlapping.", Locale.EN_CN.isRegionOverlapping(Locale.ZH_CN));
    }

    @Test
    public void shouldDetermineDifferentRegion() {
        assertFalse("Locales with overlapping regions determined as overlapping.", Locale.ZH_CN.isRegionOverlapping(Locale.ZH_TW));
    }

    @Test
    public void shouldFindOverlappingLanguages() {
        Set<Locale> overlappingLocales = Stream.of(Locale.EN_GB, Locale.EN_CN, Locale.EN_TW, Locale.EN_SG).collect(Collectors.toSet());

        assertEquals("Locales with overlapping languages incomplete.", overlappingLocales, Locale.EN_US.getLanguageOverlaps());
    }

    @Test
    public void shouldFindOverlappingRegions() {
        Set<Locale> overlappingRegions = Stream.of(Locale.ZH_TW).collect(Collectors.toSet());

        assertEquals("Locales with overlapping regions incomplete.", overlappingRegions, Locale.EN_TW.getRegionOverlaps());
    }

    @Test
    public void shouldFindAllLocalesForRegion() {
        Set<Locale> locales = Stream.of(Locale.ZH_TW, Locale.EN_TW).collect(Collectors.toSet());

        assertEquals("Locales with overlapping regions incomplete.", locales, Locale.getLocalesForRegion("Tw"));
    }

    @Test
    public void shouldFindNoLocalesForInvalidRegion() {
        assertEquals("Locales found for non-existing region.", Collections.emptySet(), Locale.getLocalesForRegion("zz"));
    }

    @Test
    public void shouldFindAllLocalesForLanguage() {
        Set<Locale> locales = Stream.of(Locale.EN_US, Locale.EN_GB, Locale.EN_CN, Locale.EN_TW, Locale.EN_SG).collect(Collectors.toSet());

        assertEquals("Locales with overlapping language incomplete.", locales, Locale.getLocalesForLanguage("En"));
    }

    @Test
    public void shouldFindNoLocalesForInvalidLanguage() {
        assertEquals("Locales found for non-existing language.", Collections.emptySet(), Locale.getLocalesForLanguage("zz"));
    }
}
