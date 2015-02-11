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

package nl.salp.warcraft4j.wowclient.databaseclient;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class DbcHeader {
    private final String magicString;
    private final int recordCount;
    private final int fieldCount;
    private final int recordSize;
    private final int stringBlockSize;

    public DbcHeader(String magicString, int recordCount, int fieldCount, int recordSize, int stringBlockSize) {
        this.magicString = magicString;
        this.recordCount = recordCount;
        this.fieldCount = fieldCount;
        this.recordSize = recordSize;
        this.stringBlockSize = stringBlockSize;
    }

    public String getMagicString() {
        return magicString;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public int getRecordSize() {
        return recordSize;
    }

    public int getStringBlockSize() {
        return stringBlockSize;
    }

    @Override
    public String toString() {
        return String.format("DbcHeader [magicString=%s, recordCount=%d, fieldCount=%d, recordSize=%d, stringBlockSize=%d]", magicString, recordCount, fieldCount, recordSize,
                stringBlockSize);
    }
}
