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

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class JenkinsHashTest {
    @Test
    public void testJenkinsHashLittle2WithEmptyValue() {
        testJenkinsHashLittle2(0xdeadbeef_deadbeefL, "", 0, 0);
    }

    @Test
    public void testJenkinsHashLittle2WithEmptyValueAndOffsetB() {
        testJenkinsHashLittle2(0xbd5b7dde_deadbeefL, "", 0, 0xdeadbeef);
    }

    @Test
    public void testJenkinsHashLittle2WithEmptyValueAndBothOffsets() {
        testJenkinsHashLittle2(0x9c093ccd_bd5b7ddeL, "", 0xdeadbeef, 0xdeadbeef);
    }

    @Test
    public void testJenkinsHashLittle2WithValue() {
        testJenkinsHashLittle2(0x17770551_ce7226e6L, "Four score and seven years ago", 0, 0);
    }

    @Test
    public void testJenkinsHashLittle2WithValueAndOffsetB() {
        testJenkinsHashLittle2(0xe3607cae_bd371de4L, "Four score and seven years ago", 0, 1);
    }

    @Test
    public void testJenkinsHashLittle2WithValueAndOffsetC() {
        testJenkinsHashLittle2(0xcd628161_6cbea4b3L, "Four score and seven years ago", 1, 0);
    }

    @Test
    public void testJenkinsHashLittleValue() {
        testJenkinsHashLittle(0x17770551, "Four score and seven years ago", 0);
    }

    @Test
    public void testJenkinsHashHashLittleValueWithOffset() {
        testJenkinsHashLittle(0xcd628161, "Four score and seven years ago", 1);
    }

    private static void testJenkinsHashLittle(int expected, String value, int pc) {
        byte[] data = value.getBytes(StandardCharsets.US_ASCII);
        int hash = JenkinsHash.hashLittle(data, data.length, pc);

        assertEquals(String.format("('%s', %d, %d) -> %8X != %8X", value, data.length, pc, hash, expected), expected, hash);
    }

    private static void testJenkinsHashLittle2(long expected, String value, int pc, int pb) {
        byte[] data = value.getBytes(StandardCharsets.US_ASCII);
        long hash = JenkinsHash.hashLittle2(data, data.length, pc, pb);
        int hashA = JenkinsHash.hashLittle2a(data, data.length, pc, pb);
        int hashB = JenkinsHash.hashLittle2b(data, data.length, pc, pb);

        assertEquals(String.format("('%s', %d, %d, %d) -> %16X[a=%8X, b=%8X] != %16X", value, data.length, pc, pb, hash, hashA, hashB, expected), expected, hash);
    }
}
