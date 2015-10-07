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
package nl.salp.warcraft4j.util;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Lazy-loaded object wrapper.
 *
 * @param <T> The type of the wrapped object.
 *
 * @author Barre Dijkstra
 */
public class LazyInstance<T> {
    /** The resolved instance. */
    private T instance;
    /** Flags indicating if the instance was resolved. */
    private boolean resolved;
    /** The supplier for resolving the instance. */
    private Supplier<T> supplier;

    /**
     * Create a new lazy instance.
     *
     * @param supplier The supplier for resolving the instance.
     *
     * @throws IllegalArgumentException When the supplier is null.
     */
    public LazyInstance(Supplier<T> supplier) throws IllegalArgumentException {
        this.supplier = Optional.ofNullable(supplier)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a lazy instance with a null supplier"));
    }

    /**
     * Create a new resolved instance.
     *
     * @param instance The instance.
     *
     * @throws IllegalArgumentException When the supplier is null.
     */
    public LazyInstance(T instance) throws IllegalArgumentException {
        this.instance = instance;
        this.resolved = true;
    }

    /**
     * Get the instance, resolving it if needed.
     *
     * @return The instance.
     */
    public T get() {
        if (!resolved) {
            instance = supplier.get();
            resolved = true;
            supplier = null;
        }
        return instance;
    }

    /**
     * Check if the instance is resolved.
     *
     * @return {@code true} if the instance is resolved.
     */
    public boolean isResolved() {
        return resolved;
    }
}
