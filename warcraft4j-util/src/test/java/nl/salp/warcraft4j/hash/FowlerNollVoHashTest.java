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
package nl.salp.warcraft4j.hash;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for {@link FowlerNollVoHash}.
 * <p/>
 * <p/>
 * Hash values have been taken from <a href="http://find.fnvhash.com/">http://find.fnvhash.com/</a>.
 *
 * @author Barre Dijkstra
 */
public class FowlerNollVoHashTest {
    private static final byte[] SHORT_INPUT = "test".getBytes();
    private static final byte[] SHORT_HASH_FNV1_32BIT = getBytes("bc2c0be9");
    private static final byte[] SHORT_HASH_FNV1A_32BIT = getBytes("afd071e5");
    private static final byte[] SHORT_HASH_FNV1_64BIT = getBytes("8c093f7e9fccbf69");
    private static final byte[] SHORT_HASH_FNV1A_64BIT = getBytes("f9e6e6ef197c2b25");

    private static final byte[] LONG_INPUT = "FNV Hash Calculator Online Developers may enjoy using this online calculator to test or verify their own results.".getBytes();
    private static final byte[] LONG_HASH_FNV1_32BIT = getBytes("3229ed09");
    private static final byte[] LONG_HASH_FNV1A_32BIT = getBytes("5726f10d");
    private static final byte[] LONG_HASH_FNV1_64BIT = getBytes("ac1f73d93d135929");
    private static final byte[] LONG_HASH_FNV1A_64BIT = getBytes("cbc16afc8a1c6d2d");


    @Test
    public void shouldHashTextIn32bitFnv1() throws Exception {
        FowlerNollVoHash fnvHash = FowlerNollVoHash.hash32fnv1();
        assertArrayEquals("Invalid 32-bit FNV-1 hash generated for shorter input text", SHORT_HASH_FNV1_32BIT, fnvHash.hash(SHORT_INPUT));
        assertArrayEquals("Invalid 32-bit FNV-1 hash generated for longer input text", LONG_HASH_FNV1_32BIT, fnvHash.hash(LONG_INPUT));
    }

    @Test
    public void shouldHashTextIn32bitFnv1a() throws Exception {
        FowlerNollVoHash fnvHash = FowlerNollVoHash.hash32fnv1a();
        assertArrayEquals("Invalid 32-bit FNV-1a hash generated for shorter input text", SHORT_HASH_FNV1A_32BIT, fnvHash.hash(SHORT_INPUT));
        assertArrayEquals("Invalid 32-bit FNV-1a hash generated for longer input text", LONG_HASH_FNV1A_32BIT, fnvHash.hash(LONG_INPUT));
    }

    @Test
    public void shouldHashTextIn64bitFnv1() throws Exception {
        FowlerNollVoHash fnvHash = FowlerNollVoHash.hash64fnv1();
        assertArrayEquals("Invalid 64-bit FNV-1 hash generated for shorter input text", SHORT_HASH_FNV1_64BIT, fnvHash.hash(SHORT_INPUT));
        assertArrayEquals("Invalid 64-bit FNV-1 hash generated for longer input text", LONG_HASH_FNV1_64BIT, fnvHash.hash(LONG_INPUT));
    }

    @Test
    public void shouldHashTextIn64bitFnv1a() throws Exception {
        FowlerNollVoHash fnvHash = FowlerNollVoHash.hash64fnv1a();
        assertArrayEquals("Invalid 64-bit FNV-1a hash generated for shorter input text", SHORT_HASH_FNV1A_64BIT, fnvHash.hash(SHORT_INPUT));
        assertArrayEquals("Invalid 64-bit FNV-1a hash generated for longer input text", LONG_HASH_FNV1A_64BIT, fnvHash.hash(LONG_INPUT));
    }

    /**
     * Helper method for easy declaration of byte arrays from a hex string.
     *
     * @param input The hex string.
     *
     * @return The byte[] from the hex string.
     */
    private static byte[] getBytes(String input) {
        try {
            return (byte[]) new Hex().decode(input);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

}
