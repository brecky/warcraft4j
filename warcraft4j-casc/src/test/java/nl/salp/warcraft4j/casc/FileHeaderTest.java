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

import nl.salp.warcraft4j.io.reader.ByteArrayDataReader;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.util.DataTypeUtil;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FileHeader}.
 *
 * @author Barre Dijkstra
 * @see FileHeader
 */
public class FileHeaderTest {
    private static final String HEADER = "test";
    private static final byte[] HEADER_DATA = HEADER.getBytes(StandardCharsets.UTF_8);
    private static final String CONTENT = "testData";
    private static final byte[] CONTENT_DATA = CONTENT.getBytes(StandardCharsets.UTF_8);

    @Test
    public void shouldSetHeader() {
        FileHeader header = new FileHeader(HEADER_DATA);

        assertArrayEquals(HEADER_DATA, header.getHeader());
    }

    @Test
    public void shouldUseHeaderHashAsHashCode() {
        FileHeader header = new FileHeader(HEADER_DATA);

        assertEquals(DataTypeUtil.hash(HEADER_DATA), header.hashCode());
    }

    @Test
    public void shouldRepresentHeaderAsString() {
        FileHeader header = new FileHeader(HEADER_DATA);

        assertEquals(DataTypeUtil.byteArrayToHexString(HEADER_DATA), header.toHexString());
        assertEquals(HEADER, header.toString());
    }

    @Test
    public void shouldReaderHeaderAndCreateInstance() throws Exception {
        FileHeader header = FileHeader.parse(() -> new ByteArrayDataReader(CONTENT_DATA));

        assertArrayEquals(HEADER_DATA, header.getHeader());
        assertEquals(DataTypeUtil.hash(HEADER_DATA), header.hashCode());
    }

    @Test(expected = CascParsingException.class)
    public void shouldRethrowIOException() throws Exception {
        DataReader dataReader = mock(DataReader.class);
        when(dataReader.remaining()).thenThrow(IOException.class);

        FileHeader.parse(() -> dataReader);
    }

    @Test
    public void shouldUseAvailableDataAsHeaderLength() {
        byte[] data = ArrayUtils.subarray(CONTENT_DATA, 0, 2);
        FileHeader header = FileHeader.parse(() -> new ByteArrayDataReader(data));

        assertArrayEquals(data, header.getHeader());
        assertEquals(DataTypeUtil.hash(data), header.hashCode());
    }

    @Test public void shouldEqualOnHeaderData() {
        FileHeader header = new FileHeader(HEADER_DATA);
        FileHeader other = new FileHeader(HEADER_DATA);

        assertTrue(header.equals(other));
    }
}
