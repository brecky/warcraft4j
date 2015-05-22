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

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.util.DbcUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import static nl.salp.warcraft4j.clientdata.dbc.util.DbcUtil.findMappingsOnClasspath;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DbcAnalysisServiceImpl implements DbcAnalysisService {
    @Override
    public ClasspathEntryMappingResults getMappingsOnClasspath(ClassLoader classLoader) {
        return new ClasspathEntryMappingResults(findMappingsOnClasspath(classLoader));
    }

    @Override
    public Collection<String> getUnmappedFiles(String dbcFileDirectory, ClassLoader classLoader) throws IOException {
        Collection<String> missingFiles = new TreeSet<>();
        Collection<String> mappedFiles = getAllMappedFiles(DbcUtil.findMappingsOnClasspath(classLoader));
        for (String file : DbcUtil.getAllClientDatabaseFiles(dbcFileDirectory)) {
            if (!mappedFiles.contains(file)) {
                missingFiles.add(file);
            }
        }
        return missingFiles;
    }


    private Collection<String> getAllMappedFiles(Collection<Class<? extends DbcEntry>> mappingEntries) {
        Collection<String> files = new HashSet<>();
        for (Class<? extends DbcEntry> mapping : mappingEntries) {
            String file = DbcUtil.getMappedFile(mapping);
            if (isNotEmpty(file)) {
                files.add(file);
            }
        }
        return files;
    }
}
