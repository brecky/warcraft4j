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

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Lookup2HashTest {
    private static final String SHORT_INPUT = "test";
    /** Longer test input (the header of the Bob Jenkins paper) */
    private static final String LONG_INPUT =
            "Abstract\n\nI offer you a new hash function for hash table lookup that is faster and more thorough than the one you are using now. I also give you a way to verify that it is more thorough.";


    /**
     * Test data to validate the hash.
     */
    private static class TestData {
        String str;
        byte[] bytes;
        long initial;
        long expected;

        public TestData(byte[] bytes, long initial, long expected) {
            this.str = "byte[] { ";
            for (byte one : bytes) {
                this.str += one + ", ";
            }
            this.str += "}";
            this.bytes = bytes;
            this.initial = initial;
            this.expected = expected;
        }

        public TestData(String str, long initial, long expected) {
            this.str = str;
            this.bytes = new byte[str.length()];
            for (int charCount = 0; charCount < str.length(); charCount++) {
                this.bytes[charCount] = (byte) str.codePointAt(charCount);
            }
            this.initial = initial;
            this.expected = expected;
        }
    }

    private static TestData[] tests = new TestData[] {
            new TestData("hello", 0L, 3070638494L),
            new TestData("wow", 0L, 627410295L),
            new TestData(new byte[] {0}, 0L, 1843378377L),
            new TestData(new byte[] {(byte) 255, (byte) 128, 64, 1}, 0L, 3359486273L),
            new TestData("this is 11c", 0L, 3384459500L),
            new TestData("this is 12ch", 0L, 313177311L),
            new TestData("this is >12ch", 0L, 2321813933L),
            new TestData("this is much large than 12 characters", 0L, 2771373033L),

            new TestData("hello", 3070638494L, 1535955511L),
            new TestData("wow", 627410295L, 320141986L),
            new TestData(new byte[] {0}, 1843378377L, 341630388L),
            new TestData(new byte[] {(byte) 255, (byte) 128, 64, 1}, 3359486273L, 2916354366L),
            new TestData("this is 11c", 3384459500L, 1497460513L),
            new TestData("this is 12ch", 313177311L, 1671722359L),
            new TestData("this is >12ch", 2321813933L, 4197822112L),
            new TestData("this is much large than 12 characters", 2771373033L, 1338302094L),
    };

    @Test
    public void testHashes() {
        Lookup2Hash lookup2Hash = new Lookup2Hash();
        for (TestData test : tests) {
            long got = lookup2Hash.jenkinsHash(test.bytes, test.initial);
            if (got != test.expected) {
                fail("Buffer '" + test.str + "' (len " + test.bytes.length
                        + ") with initial " + test.initial + " expected " + test.expected + " but got " + got);
            }
        }
    }
}
