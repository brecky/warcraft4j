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
package nl.salp.warcraft4j.clientdata.casc;

import java.io.InputStream;
import java.util.Map;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
abstract class CascConfigDataParser {
    private Map<Integer, Field> indexedFields;
    private Map<String, Integer> fieldNames;

    private void parse(InputStream stream) {

    }

    protected static final class Field {
        private int index;
        private String name;
        private String type;
        private int length;

        public Field(int index, String name, String type, int length) {
            this.index = index;
            this.name = name;
            this.type = type;
            this.length = length;
        }

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getLength() {
            return length;
        }
    }
}
