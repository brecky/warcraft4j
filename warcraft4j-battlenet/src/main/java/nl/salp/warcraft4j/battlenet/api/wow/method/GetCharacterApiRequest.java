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
package nl.salp.warcraft4j.battlenet.api.wow.method;

import nl.salp.warcraft4j.battlenet.api.BattlenetApiGroup;
import nl.salp.warcraft4j.battlenet.api.JsonApiRequest;
import nl.salp.warcraft4j.battlenet.api.wow.dto.CharacterDTO;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;
import static nl.salp.warcraft4j.battlenet.api.BattlenetApiGroup.WORLD_OF_WARCRAFT;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class GetCharacterApiRequest extends JsonApiRequest<CharacterDTO> {
    /** The DTO class. */
    private static final Class<CharacterDTO> DTO_CLASS = CharacterDTO.class;
    /** The API method. */
    private static final String API_METHOD = "/%s/character/%s/%s";
    /** The API to use. */
    private static final BattlenetApiGroup API = WORLD_OF_WARCRAFT;
    /** Flag indicating if the method requires authentication. */
    private static final boolean API_REQUIRES_AUTH = false;
    /** The name of the parameter of the detail fields to retrieve. */
    private static final String PARAMETER_FIELDS = "fields";
    /** The realm of the character. */
    private final String realm;
    /** The character name. */
    private final String characterName;
    /** The character details to retrieve. */
    private final CharacterDetailField[] characterDetailFields;

    /**
     * Create a new GetCharacterMethod instance.
     *
     * @param realm                 The realm of the character.
     * @param characterName         The character name.
     * @param characterDetailFields The character details to retrieve.
     */
    public GetCharacterApiRequest(String realm, String characterName, CharacterDetailField... characterDetailFields) {
        super(DTO_CLASS);
        this.realm = realm;
        this.characterName = characterName;
        this.characterDetailFields = characterDetailFields;
    }

    @Override
    public boolean isAuthenticationRequired() {
        return API_REQUIRES_AUTH;
    }

    @Override
    public String getRequestUri() {
        return format(API_METHOD, API.getApiUri(), realm, characterName);
    }

    @Override
    public Map<String, String> getRequestParameters() {
        StringBuilder fieldsBuilder = new StringBuilder();
        for (int i = 0; i < characterDetailFields.length; i++) {
            if (i > 0) {
                fieldsBuilder.append(',');
            }
            fieldsBuilder.append(characterDetailFields[i].getFieldName());
        }
        return Collections.singletonMap(PARAMETER_FIELDS, fieldsBuilder.toString());
    }
}
