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
package nl.salp.warcraft4j.dev.dbc.codegen.dbcmapping;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class EntryInformation {
    private final String targetPackage;
    private final String type;
    private final String dbcType;
    private final boolean invalidSize;
    private final int sizeDifference;
    private final int size;

    public EntryInformation(String targetPackage, String type, String dbcType, boolean invalidSize, int sizeDifference, int size) {
        this.targetPackage = targetPackage;
        this.type = type;
        this.dbcType = dbcType;
        this.invalidSize = invalidSize;
        this.sizeDifference = sizeDifference;
        this.size = size;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public String getType() {
        return type;
    }

    public String getDbcType() {
        return dbcType;
    }

    public boolean isInvalidSize() {
        return invalidSize;
    }

    public int getSizeDifference() {
        return sizeDifference;
    }

    public int getSize() {
        return size;
    }
}
