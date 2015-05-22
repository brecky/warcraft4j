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
package nl.salp.warcraft4j.model.lazy;

import javassist.util.proxy.MethodHandler;
import nl.salp.warcraft4j.model.Entity;

import java.lang.reflect.Method;

/**
 * Method handler for lazy {@link nl.salp.warcraft4j.model.Entity} proxies.
 *
 * @param <T> The referable entity to lazy load.
 *
 * @author Barre Dijkstra
 */
public class LazyEntityMethodHandler<T extends Entity> implements MethodHandler {
    /** The entity resolver to use to resolve the entity. */
    private final EntityResolver entityResolver;
    /** The id of the entity to resolve. */
    private final int id;
    /** The type of the entity to resolve. */
    private final Class<T> entityType;
    /** The resolved entity or {@code null} when the entity has not been resolved. */
    private T entity;

    /**
     * Create a new handler instance.
     *
     * @param entityResolver The entity resolver to use to resolve the entity.
     * @param id             The id of the entity to resolve.
     * @param entityType     The type of the entity to resolve.
     */
    public LazyEntityMethodHandler(EntityResolver entityResolver, int id, Class<T> entityType) {
        this.entityResolver = entityResolver;
        this.id = id;
        this.entityType = entityType;
    }

    @Override
    public Object invoke(Object o, Method method, Method proceed, Object[] objects) throws Throwable {
        if ("getId".equals(method.getName())) {
            return id;
        }
        if (entity == null) {
            entity = entityResolver.resolve(entityType, id);
        }
        return method.invoke(entity, objects);
    }
}
