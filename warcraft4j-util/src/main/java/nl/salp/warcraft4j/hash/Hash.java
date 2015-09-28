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

/**
 * Implementation for creating a unique hash of a set of data.
 *
 * @author Barre Dijkstra
 */
public interface Hash {
    /**
     * Create a hash from the provided data.
     *
     * @param data The data to hash.
     *
     * @return The hash of the data.
     */
    byte[] hash(byte[] data);

    /**
     * Create a hash from the provided String.
     *
     * @param data The String to hash.
     *
     * @return The hash of the String.
     */
    byte[] hash(String data);

    /**
     * Get the hash of the provided data as a hex String.
     *
     * @param data The data to hash.
     *
     * @return The hex String representation of the data hash.
     */
    String hashHexString(byte[] data);

    /**
     * Get the hash of the provided String as a hex String.
     *
     * @param data The String to hash.
     *
     * @return The hex String representation of the String hash.
     */
    String hashHexString(String data);
}
