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

package nl.salp.warcraft4j.clientdata.util.hash;

/**
 * Collection of hash implementations.
 *
 * @author Barre Dijkstra
 */
public enum Hashes implements Hash {
    /** 32-bit FNV-1 hash. */
    FNV1_32BIT(FowlerNollVoHash.hash32fnv1()),
    /** 32-bit FNV-1a hash. */
    FNV1A_32BIT(FowlerNollVoHash.hash32fnv1a()),
    /** 64-bit FNV-1 hash. */
    FNV1_64BIT(FowlerNollVoHash.hash64fnv1()),
    /** 64-bit FNV-1a hash. */
    FNV1A_64BIT(FowlerNollVoHash.hash64fnv1a()),
    /** Jenkins hash. */
    JENKINS(new JenkinsHash()),
    /** MD5 hash. */
    MD5(new Md5Hash());

    /** The hashing implementation, which _MUST_ be stateless. */
    private final Hash implementation;

    /**
     * Create a new Hashes instances.
     *
     * @param implementation The hashing implementation, which _MUST_ be stateless.
     */
    private Hashes(Hash implementation) {
        this.implementation = implementation;
    }

    /**
     * Get the hash implementation.
     *
     * @return The hash implementation.
     */
    public Hash getHash() {
        return implementation;
    }

    /**
     * Hash the data using the hash implementation.
     *
     * @param data The data to hash.
     *
     * @return The hash of the data.
     */
    public byte[] hash(byte[] data) {
        return implementation.hash(data);
    }
}
