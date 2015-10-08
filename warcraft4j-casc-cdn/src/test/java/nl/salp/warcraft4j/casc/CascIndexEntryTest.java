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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CascIndexEntry}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.CascIndexEntry
 */
public class CascIndexEntryTest {
    private static final int FILE_NUMBER = 12;
    private static final int DATAFILE_OFFSET = 54321;
    private static final long FILE_SIZE = 1234567;
    private FileKey fileKey;

    @Before
    public void setUp() {
        fileKey = mock(FileKey.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullFileKey() {
        new CascIndexEntry(null, 1, 2, 3);
    }

    @Test
    public void shouldSetData() {
        CascIndexEntry indexEntry = new CascIndexEntry(fileKey, FILE_NUMBER, DATAFILE_OFFSET, FILE_SIZE);

        assertEquals(fileKey, indexEntry.getFileKey());
        assertEquals(FILE_NUMBER, indexEntry.getFileNumber());
        assertEquals(DATAFILE_OFFSET, indexEntry.getDataFileOffset());
        assertEquals(FILE_SIZE, indexEntry.getFileSize());
    }

    @Test
    public void shouldUseFileKeyHashCode() {
        CascIndexEntry indexEntry = new CascIndexEntry(fileKey, FILE_NUMBER, DATAFILE_OFFSET, FILE_SIZE);

        assertEquals(fileKey.hashCode(), indexEntry.hashCode());
    }
}
