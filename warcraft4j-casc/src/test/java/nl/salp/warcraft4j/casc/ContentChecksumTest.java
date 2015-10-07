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

import java.util.Arrays;

/**
 * Unit tests for {@link ContentChecksum}.
 *
 * @author Barre Dijkstra
 * @see ContentChecksum
 */
public class ContentChecksumTest {
    @Test
    public void shouldCreateInstanceForChecksum() {
        byte[] checksum = new byte[ContentChecksum.CHECKSUM_LENGTH];
        Arrays.fill(checksum, (byte) 0);
        new ContentChecksum(checksum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTooSmallChecksum() {
        byte[] checksum = new byte[ContentChecksum.CHECKSUM_LENGTH - 1];
        Arrays.fill(checksum, (byte) 0);
        new ContentChecksum(checksum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTooBigChecksum() {
        byte[] checksum = new byte[ContentChecksum.CHECKSUM_LENGTH + 1];
        Arrays.fill(checksum, (byte) 0);
        new ContentChecksum(checksum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullChecksum() {
        new ContentChecksum(null);
    }
}
