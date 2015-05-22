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

package nl.salp.warcraft4j.clientdata.dbc.parser;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;

import java.util.Set;

/**
 * File parser for parsing DBC / DB2 files.
 *
 * @author Barre Dijkstra
 */
public interface DbcFileParser {
    /**
     * Parse the header of a dbc file.
     *
     * @param filename The name of the file.
     *
     * @return The parsed header.
     *
     * @throws DbcParsingException When there was a problem parsing the data.
     */
    DbcHeader parseHeader(String filename) throws DbcParsingException;

    /**
     * Parse the full meta-data of a dbc file.
     *
     * @param filename The name of the file.
     *
     * @return The parsed meta-data.
     *
     * @throws DbcParsingException When there was a problem parsing the data.
     */
    DbcFile parseMetaData(String filename) throws DbcParsingException;

    /**
     * Parse the full meta-data of a dbc file.
     *
     * @param mappingType The mapping entry type.
     * @param <T>         The type of the mapping entry.
     *
     * @return The parsed meta-data.
     *
     * @throws DbcParsingException When there was a problem parsing the data.
     */
    <T extends DbcEntry> DbcFile parseMetaData(Class<T> mappingType) throws DbcParsingException;

    /**
     * Parse the entries of a mapped dbc file.
     *
     * @param mappingType The mapping entry type.
     * @param <T>         The type of the mapping entry.
     *
     * @return The parsed instances.
     *
     * @throws DbcParsingException When there was a problem parsing the data.
     */
    <T extends DbcEntry> Set<T> parse(Class<T> mappingType) throws DbcParsingException;

    /**
     * Check if direct access for reading the file is supported or if the file can only be read as a whole.
     *
     * @return {@code true} if direct access is supported, {@code false} if not.
     */
    boolean isDirectAccessSupported();

    /**
     * Read a single entry from a mapped dbc file.
     * <p/>
     * This method may not be implemented or supported by all parser implementations (check support via {@link #isDirectAccessSupported()}).
     *
     * @param mappingType The mapping entry type.
     * @param index       The index of the instance to read.
     * @param <T>         The type of the mapping entry.
     *
     * @return The read instance.
     *
     * @throws DbcParsingException           When there was a problem parsing the entry.
     * @throws DbcEntryNotFoundException     When there was no entry found with the given index.
     * @throws UnsupportedOperationException When the retrieval of individual entries is not supported by the parser.
     * @see #isDirectAccessSupported()
     */
    <T extends DbcEntry> T parse(Class<T> mappingType, int index) throws DbcParsingException, DbcEntryNotFoundException, UnsupportedOperationException;

    /**
     * Parse the string table from a dbc file.
     * <p/>
     * This method may not be implemented or supported by all parser implementations (check support via {@link #isDirectAccessSupported()}).
     *
     * @param filename The name of the dbc file.
     *
     * @return The parsed string table.
     *
     * @throws DbcParsingException           When there was a problem parsing the string table.
     * @throws UnsupportedOperationException When the direct parsing of a string table is not supported by the parser.
     * @see #isDirectAccessSupported()
     */
    DbcStringTable parseStringTable(String filename) throws DbcParsingException, UnsupportedOperationException;

    /**
     * Parse the string table from a dbc file.
     * <p/>
     * This method may not be implemented or supported by all parser implementations (check support via {@link #isDirectAccessSupported()}).
     *
     * @param mappingType The mapping entry type.
     *
     * @return The parsed string table.
     *
     * @throws DbcParsingException           When there was a problem parsing the string table.
     * @throws UnsupportedOperationException When the direct parsing of a string table is not supported by the parser.
     * @see #isDirectAccessSupported()
     */
    <T extends DbcEntry> DbcStringTable parseStringTable(Class<T> mappingType) throws DbcParsingException, UnsupportedOperationException;
}
