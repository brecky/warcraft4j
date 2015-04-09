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

/**
 * Game using the CASC filesystem.
 *
 * @author Barre Dijkstra
 */
public enum CascGame {
    HEROES_OF_THE_STORM("Heroes of the Storm", 0x00010000),
    WORLD_OF_WARCRAFT("World of Warcraft", 0x00020000),
    DIABLO_3("Diablo 3", 0x00030000);

    /** The mask for getting the game flag. */
    private static final int GAME_MASK = 0xFFF0000;
    /** The full name of the game. */
    private final String name;
    /** The flag for the game. */
    private final int gameFlag;

    CascGame(String name, int gameFlag) {
        this.name = name;
        this.gameFlag = gameFlag;
    }

    /**
     * Get the full name of the game.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the flag for the game.
     *
     * @return The flag.
     */
    public int getGameFlag() {
        return gameFlag;
    }

    /**
     * Check if the flag is for the given game.
     *
     * @param flag The flag.
     *
     * @return {@code true} if the flag is for the current game.
     */
    public boolean isFlagForGame(int flag) {
        return (flag & GAME_MASK) == gameFlag;
    }

    /**
     * Get the {@link CascGame} for the given flag.
     *
     * @param flag The flag.
     *
     * @return The {@link CascGame} matching the flag or {@code null} when no game was found matching the flag.
     */
    public static CascGame getGame(int flag) {
        CascGame game = null;
        for (CascGame g : CascGame.values()) {
            if (g.isFlagForGame(flag)) {
                game = g;
                break;
            }
        }
        return game;
    }


}
