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

import java.util.function.Supplier;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class LazyInstance<T> {
    private T instance;
    private boolean resolved;
    private Supplier<T> supplier;

    public LazyInstance(Supplier<T> supplier) throws IllegalArgumentException {
        if (supplier == null) {
            throw new IllegalArgumentException("Unable to create a lazy instance with a null supplier");
        }
        this.supplier = supplier;
    }

    public LazyInstance(T instance) throws IllegalArgumentException {
        this.instance = instance;
        this.resolved = true;
    }

    public T get() {
        if (!resolved) {
            instance = supplier.get();
            resolved = true;
            supplier = null;
        }
        return instance;
    }

    public boolean isResolved() {
        return resolved;
    }
}
