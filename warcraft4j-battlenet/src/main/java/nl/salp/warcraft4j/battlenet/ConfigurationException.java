/*
 *
 *  * Licensed to the Warcraft4J Project under one
 *  * or more contributor license agreements.  See the NOTICE file
 *  * distributed with this work for additional information
 *  * regarding copyright ownership.  The Warcraft4J Project licenses
 *  * this file to you under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package nl.salp.warcraft4j.battlenet;

/**
 * Exception that is used for problems with the Battle.NET API configuration.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.battlenet.BattlenetApiConfig
 */
public class ConfigurationException extends Exception {
    /**
     * Create a new ConfigurationException with an exception message.
     *
     * @param message The exception message.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Create a new ConfigurationException with an exception message and cause exception.
     *
     * @param message The exception message.
     * @param cause   The exception that caused this exception.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new ConfigurationException with a cause exception.
     *
     * @param cause The exception that caused this exception.
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
