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

package nl.salp.warcraft4j.battlenet.api.wow;

import nl.salp.warcraft4j.battlenet.BattlenetRegion;
import nl.salp.warcraft4j.battlenet.api.wow.dto.CharacterDTO;
import nl.salp.warcraft4j.battlenet.api.wow.dto.ItemDTO;
import nl.salp.warcraft4j.battlenet.api.wow.dto.ItemSetDTO;
import nl.salp.warcraft4j.battlenet.api.wow.method.CharacterDetailField;
import nl.salp.warcraft4j.battlenet.BattlenetLocale;
import nl.salp.warcraft4j.battlenet.api.BattlenetApiParsingException;

import java.io.IOException;

/**
 * World of Warcraft Battle.NET API implementation.
 *
 * @author Barre Dijkstra
 */
public interface WowBattlenetApi {
    /**
     * Get the information for a character from the default region in the default locale.
     *
     * @param realm        The realm the character is on.
     * @param character    The name of the character.
     * @param detailFields The detail fields to retrieve.
     *
     * @return The character or {@code null} when no matching character could be found.
     *
     * @throws IOException                  When communication with the API failed.
     * @throws BattlenetApiParsingException When the API response could not be parsed.
     */
    CharacterDTO getCharacter(String realm, String character, CharacterDetailField... detailFields) throws BattlenetApiParsingException, IOException;

    /**
     * Get the information for a character.
     *
     * @param region       The region the character is on.
     * @param locale       The locale to get the information in.
     * @param realm        The realm the character is on.
     * @param character    The name of the character.
     * @param detailFields The detail fields to retrieve.
     *
     * @return The character or {@code null} when no matching character could be found.
     *
     * @throws IOException                  When communication with the API failed.
     * @throws BattlenetApiParsingException When the API response could not be parsed.
     */
    CharacterDTO getCharacter(BattlenetRegion region, BattlenetLocale locale, String realm, String character, CharacterDetailField... detailFields) throws BattlenetApiParsingException, IOException;

    /**
     * Get the information for an item from the default region and in the default locale.
     *
     * @param itemId The id of the item.
     *
     * @return The item or {@code null} when no item was found with the given id.
     *
     * @throws IOException                  When communication with the API failed.
     * @throws BattlenetApiParsingException When the API response could not be parsed.
     */
    ItemDTO getItem(long itemId) throws BattlenetApiParsingException, IOException;

    /**
     * Get the information for an item.
     *
     * @param region The region of the API to use.
     * @param locale The locale to get information in.
     * @param itemId The id of the item.
     *
     * @return The item or {@code null} when no item was found for the given id.
     *
     * @throws IOException                  When communication with the API failed.
     * @throws BattlenetApiParsingException When the API response could not be parsed.
     */
    ItemDTO getItem(BattlenetRegion region, BattlenetLocale locale, long itemId) throws BattlenetApiParsingException, IOException;

    /**
     * Get the information for an item set from the default region and in the default locale.
     *
     * @param itemSetId The id of the item set.
     *
     * @return The item set or {@code null} when no item set was found with the given id.
     *
     * @throws IOException                  When communication with the API failed.
     * @throws BattlenetApiParsingException When the API response could not be parsed.
     */
    ItemSetDTO getItemSet(long itemSetId) throws BattlenetApiParsingException, IOException;

    /**
     * Get the information for an item set.
     *
     * @param region    The region of the API to use.
     * @param locale    The locale to get information in.
     * @param itemSetId The id of the item set.
     *
     * @return The item set or {@code null} when no item set was found with the given id.
     *
     * @throws IOException                  When communication with the API failed.
     * @throws BattlenetApiParsingException When the API response could not be parsed.
     */
    ItemSetDTO getItemSet(BattlenetRegion region, BattlenetLocale locale, long itemSetId) throws BattlenetApiParsingException, IOException;
}
