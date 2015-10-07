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
package nl.salp.warcraft4j;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * World of Warcraft development branch.
 *
 * @author Barre Dijkstra
 */
public enum Branch {
    /** Live development branch, default installation branch. */
    LIVE,
    /** Testing development branch, often referenced as PTR or Public Test Realm. */
    TESTING,
    /** Beta development branch, mostly used for new expansions. */
    BETA;

    /**
     * Get a branch by its name.
     *
     * @param name The name of the branch.
     *
     * @return Optional of the branch.
     */
    public static Optional<Branch> getBranch(String name) {
        return Optional.ofNullable(name)
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .map(String::toUpperCase)
                .flatMap(n -> Stream.of(Branch.values())
                        .filter(l -> l.name().equals(n))
                        .findFirst());
    }

}
