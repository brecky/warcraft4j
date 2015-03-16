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

package nl.salp.warcraft4j.util;

import java.nio.ByteBuffer;

/**
 * Jenkins hash implementation.
 *
 * @author Barre Dijkstra
 */
public class JenkinsHash implements Hash {

    /**
     * Hash the data using the 'one-at-a-time' Jenkins hash.
     *
     * @param data The data.
     *
     * @return The hash.
     */
    @Override
    public byte[] hash(byte[] data) {
        int hash = 0;
        for (byte b : data) {
            hash += b;
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return ByteBuffer.allocate(4).putInt(hash).array();
    }
}
