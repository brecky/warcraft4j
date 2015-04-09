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
package nl.salp.warcraft4j.clientdata.dbc.analysis;

import java.io.IOException;
import java.util.Collection;

/**
 * Service for providing client database related analysis functions.
 *
 * @author Barre Dijkstra
 */
public interface DbcAnalysisService {
    /**
     * Get all entry mappings available on the classpath.
     *
     * @param classLoader The class loader to use (scan will include the parent class loaders).
     *
     * @return The mappings on the classpath.
     */
    ClasspathEntryMappingResults getMappingsOnClasspath(ClassLoader classLoader);

    /**
     * Find all DBC/DB2 files in the provided directory that don't have a entry mapping on the classpath.
     *
     * @param dbcFileDirectory The directory containing the DBC/DB2 files.
     * @param classLoader      The class loader to use (scan will include the parent class loaders).
     *
     * @return The names of the unmapped files.
     */
    Collection<String> getUnmappedFiles(String dbcFileDirectory, ClassLoader classLoader) throws IOException;


}
