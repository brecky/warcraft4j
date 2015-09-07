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
package nl.salp.warcraft4j.dataparser.dbc.mapping;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DbcValueMapper<T> {
    private Class<T> target;
    private ValueTarget[] valueTargets;

    private Constructor<T> getConstructor() {
        Constructor<T> constructor = null;
        try {
            constructor = target.getConstructor(getTargetTypes());
        } catch (SecurityException | NoSuchMethodException e) {
            // Ignore.
        }
        return constructor;
    }

    private boolean isConstructorAvailable() {
        return getConstructor() != null;
    }

    private boolean isAccessibleConstructorAvailable() {
        Constructor<T> c = getConstructor();
        return c != null && c.isAccessible();
    }

    private Class<?>[] getTargetTypes() {
        return Stream.of(valueTargets).map(ValueTarget::getType).toArray(Class<?>[]::new);
    }

    public static class ValueTarget {
        private final String name;
        private final Class<?> type;

        public ValueTarget(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }
    }
}
