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

import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.cdn.CascEncodingEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CascEncodingEntry}.
 *
 * @author Barre Dijkstra
 * @see CascEncodingEntry
 */
public class CascEncodingEntryTest {
    private static final int FILE_SIZE = 4096;
    private static final int FILE_KEY_COUNT = 10;
    private ContentChecksum contentChecksum;
    private List<FileKey> fileKeys;

    @Before
    public void setUp() {
        contentChecksum = mock(ContentChecksum.class);
        fileKeys = IntStream.range(0, FILE_KEY_COUNT)
                .mapToObj(i -> mock(FileKey.class))
                .collect(Collectors.toList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullContentChecksum() {
        new CascEncodingEntry(FILE_SIZE, null, fileKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullFileKeys() {
        new CascEncodingEntry(FILE_SIZE, contentChecksum, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForEmptyFileKeys() {
        new CascEncodingEntry(FILE_SIZE, contentChecksum, Collections.emptyList());
    }

    @Test
    public void shouldSetData() {
        CascEncodingEntry entry = new CascEncodingEntry(FILE_SIZE, contentChecksum, fileKeys);

        assertEquals(FILE_SIZE, entry.getFileSize());
        assertEquals(fileKeys, entry.getFileKeys());
        assertEquals(fileKeys.get(0), entry.getFirstFileKey());
        assertEquals(contentChecksum, entry.getContentChecksum());
    }

    @Test
    public void shouldDetermineSingleBlock() {
        CascEncodingEntry entry = new CascEncodingEntry(FILE_SIZE, contentChecksum, fileKeys.subList(0, 1));

        assertEquals(1, entry.getBlockCount());
        assertFalse(entry.isMultiBlock());
    }

    @Test
    public void shouldDetermineMultiBlock() {
        CascEncodingEntry entry = new CascEncodingEntry(FILE_SIZE, contentChecksum, fileKeys);

        assertEquals(FILE_KEY_COUNT, entry.getBlockCount());
        assertTrue(entry.isMultiBlock());
    }

    @Test
    public void shouldUseContentChecksumHashcode() {
        CascEncodingEntry entry = new CascEncodingEntry(FILE_SIZE, contentChecksum, fileKeys);

        assertEquals(contentChecksum.hashCode(), entry.hashCode());
    }

    @Test
    public void shouldEqualOnContent() {
        CascEncodingEntry entry = new CascEncodingEntry(FILE_SIZE, contentChecksum, fileKeys);
        CascEncodingEntry other = new CascEncodingEntry(FILE_SIZE, contentChecksum, fileKeys);
        CascEncodingEntry different = new CascEncodingEntry(FILE_SIZE + 1, contentChecksum, fileKeys.subList(0, fileKeys.size() - 1));

        assertTrue(entry.equals(other));
        assertFalse(entry.equals(different));
    }
}