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

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link RootEntry}
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.RootEntry
 */
public class RootEntryTest {

    @Test
    public void shouldGetCascLocaleFromFlags() {
        final CascLocale locale = CascLocale.EN_TW;

        RootEntry entry = new RootEntry() {
            @Override
            public ContentChecksum getContentChecksum() {
                return null;
            }

            @Override
            public long getFilenameHash() {
                return 0;
            }

            @Override
            public long getFlags() {
                return locale.getFlag();
            }
        };

        assertEquals(locale, entry.getLocale().get());
    }
}
