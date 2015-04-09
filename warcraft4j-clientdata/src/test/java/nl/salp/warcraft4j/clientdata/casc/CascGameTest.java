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

package nl.salp.warcraft4j.clientdata.casc;

import org.junit.Test;

import static nl.salp.warcraft4j.clientdata.casc.CascGame.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CascGame}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.clientdata.casc.CascGame
 */
public class CascGameTest {
    private static final int FLAG_HOTS = 0x00010000;
    private static final int FLAG_WOW = 0x00020000;
    private static final int FLAG_D3 = 0x00030000;
    private static final int FLAG_WOW_GARBAGE = 0x00020001;
    private static final int FLAG_INVALID = 0x10110001;
    private static final int FLAG_UNKNOWN = 0x00040000;

    @Test
    public void shouldMatchGameFlag() {
        assertTrue("Heroes of the Storm game flag wasn't matched by the implementation.", HEROES_OF_THE_STORM.isFlagForGame(FLAG_HOTS));
        assertTrue("World of Warcraft game flag wasn't matched by the implementation.", WORLD_OF_WARCRAFT.isFlagForGame(FLAG_WOW));
        assertTrue("Diablo 3 game flag wasn't matched by the implementation.", DIABLO_3.isFlagForGame(FLAG_D3));
    }

    @Test
    public void shouldGetGame() {
        assertSame("Flag for Heroes of the Storm did not return a CascGame instance for the game.", HEROES_OF_THE_STORM, CascGame.getGame(FLAG_HOTS));
        assertSame("Flag for World of Warcraft did not return a CascGame instance for the game.", WORLD_OF_WARCRAFT, CascGame.getGame(FLAG_WOW));
        assertSame("Flag for Diablo 3 did not return a CascGame instance for the game.", DIABLO_3, CascGame.getGame(FLAG_D3));
    }

    @Test
    public void shouldMatchGameFlagWithPostFlagData() {
        assertTrue(WORLD_OF_WARCRAFT.isFlagForGame(FLAG_WOW_GARBAGE));
    }

    @Test
    public void shouldNotGetGameForUnknownFlag() {
        assertNull("Game found for invalid flag.", CascGame.getGame(FLAG_INVALID));
        assertNull("Game found for unknown flag.", CascGame.getGame(FLAG_UNKNOWN));
    }
}
