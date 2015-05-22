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

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import nl.salp.warcraft4j.model.Entity;

import java.lang.reflect.Method;

/**
 * Factory for creating lazy loaded entities through proxying them.
 *
 * @author Barre Dijkstra
 */
public class LazyEntityFactory {
    /** The resolver to use for resolving the entities. */
    private final EntityResolver resolver;

    /**
     * Create a new factory instance.
     *
     * @param resolver The resolver to use for resolving the entities.
     */
    public LazyEntityFactory(EntityResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Create a lazy loading proxy for an entity instance.
     *
     * @param entityType The type of the entity.
     * @param id         The id of the entity.
     * @param <T>        The type of the entity.
     *
     * @return The, proxied, lazy loading entity.
     *
     * @throws Exception When creating the proxy failed.
     */
    public <T extends Entity> T create(Class<T> entityType, int id) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(entityType);
        proxyFactory.setFilter(new MethodFilter() {
            @Override
            public boolean isHandled(Method method) {
                return !method.getName().equals("finalize");
            }
        });
        Class lazyType = proxyFactory.createClass();
        T instance = (T) lazyType.newInstance();
        ((Proxy) instance).setHandler(new LazyEntityMethodHandler<>(resolver, id, entityType));
        return instance;
    }

    /**
     * Create a lazy loading proxy for an entity instance.
     *
     * @param resolver   The resolver to use for resolving the entity.
     * @param entityType The type of the entity.
     * @param id         The id of the entity.
     * @param <T>        The type of the entity.
     *
     * @return The, proxied, lazy loading entity.
     *
     * @throws Exception When creating the proxy failed.
     */
    public static <T extends Entity> T create(EntityResolver resolver, Class<T> entityType, int id) throws Exception {
        return new LazyEntityFactory(resolver).create(entityType, id);
    }
}
