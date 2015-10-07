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

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Region}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.Region
 */
public class RegionTest {
    @Test
    public void shouldGetSupportedLocales() {
        assertEquals(Locale.getLocalesForRegion("CN"), Region.CHINA.getSupportedLocales());
    }

    @Test
    public void shouldGetLocaleSupport() {
        Locale.getLocalesForRegion("CN")
                .forEach(locale ->
                                assertTrue("Supported locale reported as not supported", Region.CHINA.isSupported(locale))
                );
        assertFalse("Unsupported locale reported as supported.", Region.CHINA.isSupported(Locale.KO_KR));
        assertFalse("Null locale reported as supported.", Region.CHINA.isSupported(null));
    }

    @Test
    public void shouldNotFindRegionWithEmptyName() {
        assertFalse("Found a region with a null name.", Region.getRegion(null).isPresent());
        assertFalse("Found a region with an empty name.", Region.getRegion("").isPresent());
    }

    @Test
    public void shouldFindRegionWithUntrimmedName() {
        assertEquals(Region.CHINA, Region.getRegion(" CHINA ").get());
    }

    @Test
    public void shouldFindRegionWithAnyCaseName() {
        assertEquals(Region.CHINA, Region.getRegion("china").get());
        assertEquals(Region.CHINA, Region.getRegion("CHINA").get());
        assertEquals(Region.CHINA, Region.getRegion("cHiNa").get());
    }
}