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
package nl.salp.warcraft4j.dataparser.dbc;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public interface DbcFieldMapping<T> extends Comparable<DbcFieldMapping<T>> {
    boolean DEFAULT_PADDING = false;
    int DEFAULT_ENTRY_LENGTH = 0;
    int DEFAULT_NUMBER_ENTRIES = 1;
    String DEFAULT_FIELD_NAME_MASK = "unknownField%d";

    int getIndex();

    String getFieldName();

    Class<T> getJavaDataType();

    DbcDataType getDbcDataType();

    boolean isPadding();

    int getEntryLength();

    int getNumberOfEntries();

    int getFieldSize();

    @Override
    default int compareTo(DbcFieldMapping<T> other) {
        if (other == null) {
            return -1;
        } else {
            return Integer.valueOf(getIndex()).compareTo(other.getIndex());
        }
    }
}
