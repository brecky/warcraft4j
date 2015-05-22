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

package nl.salp.warcraft4j.clientdata.dbc.service;

import nl.salp.warcraft4j.clientdata.dbc.DbcStore;
import nl.salp.warcraft4j.clientdata.dbc.service.model.DbcModelMapper;
import nl.salp.warcraft4j.clientdata.dbc.service.model.ItemClassMaskModelMapper;
import nl.salp.warcraft4j.clientdata.dbc.service.model.ItemClassModelMapper;
import nl.salp.warcraft4j.clientdata.dbc.service.model.ItemSubClassModelMapper;
import nl.salp.warcraft4j.model.data.ItemClass;
import nl.salp.warcraft4j.model.item.ItemClassMask;
import nl.salp.warcraft4j.model.item.ItemSubClass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class DbcModelServiceImpl implements DbcModelService {
    private static final Map<Class<?>, DbcModelMapper<?, ?>> MAPPERS = new HashMap<Class<?>, DbcModelMapper<?, ?>>() {{
        put(ItemClass.class, new ItemClassModelMapper());
        put(ItemSubClass.class, new ItemSubClassModelMapper());
        put(ItemClassMask.class, new ItemClassMaskModelMapper());
    }};

    private final DbcStore store;

    public DbcModelServiceImpl(DbcStore store) {
        this.store = store;
    }

    @Override
    public <T> Collection<T> getAllInstances(Class<T> instanceClass) throws DbcMappingException, DbcRetrievalException {
        return getMapper(instanceClass).mapAll(store);
    }

    @Override
    public <T> T getInstance(Class<T> instanceClass, int id) throws DbcMappingException, DbcRetrievalException {
        return getMapper(instanceClass).map(id, store);
    }

    private static <T> DbcModelMapper<T, ?> getMapper(Class<T> modelClass) {
        DbcModelMapper<T, ?> modelMapper = null;
        DbcModelMapper<?, ?> mapper = MAPPERS.get(modelClass);
        if (mapper != null && modelClass.isAssignableFrom(mapper.getModelClass())) {
            modelMapper = (DbcModelMapper<T, ?>) mapper;
        }
        return modelMapper;
    }
}
