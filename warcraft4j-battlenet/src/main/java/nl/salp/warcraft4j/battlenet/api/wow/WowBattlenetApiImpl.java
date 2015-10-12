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

import com.google.inject.Inject;
import nl.salp.warcraft4j.battlenet.BattlenetRegion;
import nl.salp.warcraft4j.battlenet.api.wow.dto.CharacterDTO;
import nl.salp.warcraft4j.battlenet.api.wow.dto.ItemDTO;
import nl.salp.warcraft4j.battlenet.api.wow.method.CharacterDetailField;
import nl.salp.warcraft4j.battlenet.api.wow.method.GetCharacterApiRequest;
import nl.salp.warcraft4j.battlenet.api.wow.method.GetItemApiRequest;
import nl.salp.warcraft4j.battlenet.api.wow.method.GetItemSetApiRequest;
import nl.salp.warcraft4j.battlenet.BattlenetLocale;
import nl.salp.warcraft4j.battlenet.api.BattlenetApi;
import nl.salp.warcraft4j.battlenet.api.BattlenetApiParsingException;
import nl.salp.warcraft4j.battlenet.api.wow.dto.ItemSetDTO;

import java.io.IOException;

/**
 * {@link WowBattlenetApi} basic implementation.
 *
 * @author Barre Dijkstra
 */
public class WowBattlenetApiImpl implements WowBattlenetApi {
    /** The {@link BattlenetApi} to use for calls to the Battle.NET API. */
    @Inject
    private BattlenetApi battlenetApi;

    @Override
    public CharacterDTO getCharacter(String realm, String character, CharacterDetailField... detailFields) throws BattlenetApiParsingException, IOException {
        return battlenetApi.call(new GetCharacterApiRequest(realm, character, detailFields));
    }

    @Override
    public CharacterDTO getCharacter(BattlenetRegion region, BattlenetLocale locale, String realm, String character, CharacterDetailField... detailFields) throws BattlenetApiParsingException, IOException {
        return battlenetApi.call(region, locale, new GetCharacterApiRequest(realm, character, detailFields));
    }

    @Override
    public ItemDTO getItem(long itemId) throws BattlenetApiParsingException, IOException {
        return battlenetApi.call(new GetItemApiRequest(itemId));
    }

    @Override
    public ItemDTO getItem(BattlenetRegion region, BattlenetLocale locale, long itemId) throws BattlenetApiParsingException, IOException {
        return battlenetApi.call(region, locale, new GetItemApiRequest(itemId));
    }

    @Override
    public ItemSetDTO getItemSet(long itemSetId) throws BattlenetApiParsingException, IOException {
        return battlenetApi.call(new GetItemSetApiRequest(itemSetId));
    }

    @Override
    public ItemSetDTO getItemSet(BattlenetRegion region, BattlenetLocale locale, long itemSetId) throws BattlenetApiParsingException, IOException {
        return battlenetApi.call(region, locale, new GetItemSetApiRequest(itemSetId));
    }
}
