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

package nl.salp.warcraft4j.battlenet.api;

import java.io.IOException;

/**
 * Battle.NET API service.
 *
 * @author Barre Dijkstra
 */
public interface BattlenetService {

    /**
     * Call a Battle.NET API service method.
     *
     * @param method The method to call.
     * @param <T>    The type of the method result.
     *
     * @return The result.
     *
     * @throws IOException                  When there was a problem in the communication with the Battle.NET API.
     * @throws BattlenetApiParsingException When the data returned by the Battle.NET API could not be parsed.
     */
    <T> T call(BattlenetServiceMethod<T> method) throws IOException, BattlenetApiParsingException;
}
