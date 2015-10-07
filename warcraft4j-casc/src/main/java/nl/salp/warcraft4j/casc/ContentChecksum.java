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

import nl.salp.warcraft4j.util.Checksum;

import static java.lang.String.format;

/**
 * {@link Checksum} implementation for the contents of a CASC file.
 *
 * @author Barre Dijkstra
 */
public class ContentChecksum extends Checksum {
    /** The length of the checksum in bytes. */
    public static final int CHECKSUM_LENGTH = 16;

    /**
     * Create a new checksum instance.
     *
     * @param checksum The checksum.
     *
     * @throws IllegalArgumentException When the checksum is invalid.
     */
    public ContentChecksum(byte[] checksum) throws IllegalArgumentException {
        super(checksum);
        if (checksum.length != CHECKSUM_LENGTH) {
            throw new IllegalArgumentException(format("Unable to create a %d-byte content checksum from a %d byte array.", CHECKSUM_LENGTH, checksum.length));
        }
    }
}
