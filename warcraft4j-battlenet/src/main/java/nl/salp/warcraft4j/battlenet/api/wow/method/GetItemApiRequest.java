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
import nl.salp.warcraft4j.battlenet.api.wow.dto.ItemDTO;
import nl.salp.warcraft4j.battlenet.api.JsonApiRequest;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class GetItemApiRequest extends JsonApiRequest<ItemDTO> {
    /** The DTO class. */
    private static final Class<ItemDTO> DTO_CLASS = ItemDTO.class;
    /** The API URI mask. */
    private static final String API_URI_MASK = "/%s/item/%d";
    /** The API method mask. */
    private static final String API_METHOD_MASK = "/%s/item";
    /** The API to use. */
    private static final BattlenetApiGroup API = BattlenetApiGroup.WORLD_OF_WARCRAFT;
    /** Flag indicating if the method requires authentication. */
    private static final boolean API_REQUIRES_AUTH = false;
    /** The ID of the item to retrieve the information for. */
    private final long itemId;

    /**
     * Create a new BattlenetItemServiceMethod.
     *
     * @param itemId The ID of the item to retrieve the data for.
     */
    public GetItemApiRequest(long itemId) {
        super(DTO_CLASS);
        this.itemId = itemId;
    }

    @Override
    public boolean isAuthenticationRequired() {
        return API_REQUIRES_AUTH;
    }

    @Override
    public String getRequestUri() {
        return format(API_URI_MASK, API.getApiUri(), itemId);
    }

    @Override
    public String getRequestMethodBaseUri() {
        return format(API_METHOD_MASK, API.getApiUri());
    }

    @Override
    public Map<String, String> getRequestParameters() {
        return Collections.emptyMap();
    }
}
