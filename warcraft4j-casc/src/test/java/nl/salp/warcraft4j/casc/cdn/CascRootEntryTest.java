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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.casc.CascLocale;
import nl.salp.warcraft4j.casc.ContentChecksum;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CascRootEntry}.
 *
 * @author Barre Dijkstra
 * @see CascRootEntry
 */
public class CascRootEntryTest {
    private static final long FILENAME_HASH = 73457243L;
    private static final long ENTRY_FLAGS = CascLocale.EN_TW.getFlag();
    private ContentChecksum contentChecksum;

    @Before
    public void setUp() {
        contentChecksum = mock(ContentChecksum.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForEmptyContentChecksum() {
        new CascRootEntry(FILENAME_HASH, null, ENTRY_FLAGS, 1, 2);
    }

    @Test
    public void shouldSetValues() {
        CascRootEntry entry = new CascRootEntry(FILENAME_HASH, contentChecksum, ENTRY_FLAGS, 1, 2);

        assertEquals(FILENAME_HASH, entry.getFilenameHash());
        assertEquals(contentChecksum, entry.getContentChecksum());
        assertEquals(ENTRY_FLAGS, entry.getFlags());
    }

    @Test
    public void shouldResolveLocaleFromFlags() {
        CascRootEntry entry = new CascRootEntry(FILENAME_HASH, contentChecksum, ENTRY_FLAGS, 1, 2);

        assertEquals(CascLocale.EN_TW, entry.getLocale().orElse(null));
    }
}