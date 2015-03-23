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

package nl.salp.warcraft4j.battlenet.service;

import com.google.inject.Inject;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.battlenet.BattlenetRegion;
import nl.salp.warcraft4j.battlenet.api.BattlenetService;
import nl.salp.warcraft4j.battlenet.api.wow.dto.CharacterDTO;
import nl.salp.warcraft4j.battlenet.api.wow.method.CharacterDetailField;
import nl.salp.warcraft4j.battlenet.api.wow.method.GetCharacterMethod;
import nl.salp.warcraft4j.model.character.PlayerCharacter;
import nl.salp.warcraft4j.model.data.Realm;
import nl.salp.warcraft4j.service.NotFoundException;
import nl.salp.warcraft4j.service.PlayerCharacterService;
import nl.salp.warcraft4j.service.ServiceException;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class BattlenetPlayerCharacterService implements PlayerCharacterService {
    /** The Battle.NET service for execution of methods. */
    @Inject
    private BattlenetService battlenetService;

    @Override
    public PlayerCharacter find(Region region, Realm realm, String name) throws NotFoundException, ServiceException {
        BattlenetRegion bnRegion = BattlenetRegion.getRegionForKey(region);

        try {
            CharacterDTO character = battlenetService.call(new GetCharacterMethod(realm.getSlug(), name, CharacterDetailField.values()));
            if (character == null) {
                throw new NotFoundException(format("No character named %s found on realm %s-%s", name, realm.getSlug(), bnRegion.getKey()));
            }
            System.out.println(character);
        } catch (IOException e) {
            throw new ServiceException(format("Error getting the character information for %s-%s %s", realm.getSlug(), bnRegion.getKey(), name), e);
        }
        return null;
    }
}
