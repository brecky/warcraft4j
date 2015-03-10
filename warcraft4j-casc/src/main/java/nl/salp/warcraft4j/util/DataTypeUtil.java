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

import java.nio.charset.Charset;

/**
 * Utility methods for data types.
 *
 * @author Barre Dijkstra
 */
public class DataTypeUtil {
    /**
     * Private constructor to prevent instantiation.
     */
    private DataTypeUtil() {
    }

    /**
     * Get the average number of bytes a character can consist of in the provided charset.
     *
     * @param charset The character set.
     *
     * @return The maximum number of bytes per character.
     */
    public static int getAverageBytesPerCharacter(Charset charset) {
        int avg = 0;
        if (charset != null) {
            // TODO Determine whether it should be rounded or cut off.
            avg = Math.round(charset.newEncoder().averageBytesPerChar());
        }
        return avg;
    }

    /**
     * Get the maximum number of bytes a character can consist of in the provided charset.
     *
     * @param charset The character set.
     *
     * @return The maximum number of bytes per character.
     */
    public static int getMaximumBytesPerCharacter(Charset charset) {
        int max = 0;
        if (charset != null) {
            // TODO Determine whether it should be rounded or cut off.
            max = Math.round(charset.newEncoder().maxBytesPerChar());
        }
        return max;
    }
}
