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
package nl.salp.warcraft4j.clientdata.dbc.dao.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.dao.DbcEntryDAO;
import nl.salp.warcraft4j.clientdata.dbc.dao.DbcEntryDaoException;
import nl.salp.warcraft4j.clientdata.dbc.dao.DuplicateDbcEntryException;
import nl.salp.warcraft4j.clientdata.dbc.dao.NoSuchDbcEntryException;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcHeader;
import org.mongojack.Id;

import java.util.Collection;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public abstract class DbcEntryMongodbDAO<T extends DbcEntry> implements DbcEntryDAO<T> {
    /** The build number of the parsed instance (as per the DBC file header). */
    private final int buildNumber;
    /** The mapping type. */
    private final Class<T> mappingType;

    /**
     * Create a new instance.
     *
     * @param mappingType The mapping type.
     * @param header      The parsed DBC file header.
     */
    public DbcEntryMongodbDAO(Class<T> mappingType, DbcHeader header) {
        this(mappingType, (header != null ? header.getBuildNumber() : 0));
    }

    /**
     * Create a new instance for an unknown build.
     *
     * @param mappingType The mapping type.
     */
    public DbcEntryMongodbDAO(Class<T> mappingType) {
        this(mappingType, 0);
    }

    /**
     * Create a new instance.
     *
     * @param mappingType The mapping type.
     * @param buildNumber The build number the data was extracted for or {@code 0} for unknown.
     */
    public DbcEntryMongodbDAO(Class<T> mappingType, int buildNumber) {
        if (mappingType == null) {
            throw new IllegalArgumentException("Unable to create a DbcEntry MongoDB DAO with a null mapping type.");
        }
        this.mappingType = mappingType;
        if (buildNumber < 0) {
            this.buildNumber = 0;
        } else {
            this.buildNumber = buildNumber;
        }
    }

    @Override
    public final Collection<T> findAll() throws DbcEntryDaoException {
        ObjectMapper objectMapper = new ObjectMapper();
        // TODO Implement me!
        return null;
    }

    @Override
    public final T find(int id) throws DbcEntryDaoException, NoSuchDbcEntryException {
        // TODO Implement me!
        return null;
    }

    @Override
    public final void create(T instance) throws DbcEntryDaoException, DuplicateDbcEntryException, UnsupportedOperationException {
        // TODO Implement me!
    }

    @Override
    public final void update(T instance) throws DbcEntryDaoException, NoSuchDbcEntryException, UnsupportedOperationException {
        // TODO Implement me!
    }

    @Override
    public final void save(T instance) throws DbcEntryDaoException, UnsupportedOperationException {
        // TODO Implement me!
    }

    @Override
    public final void create(Collection<T> instances) throws DbcEntryDaoException, UnsupportedOperationException {
        // TODO Implement me!
    }

    @Override
    public final void update(Collection<T> instances) throws DbcEntryDaoException, UnsupportedOperationException {
        // TODO Implement me!
    }

    @Override
    public final void save(Collection<T> instances) throws DbcEntryDaoException, UnsupportedOperationException {
        // TODO Implement me!
    }

    @Override
    public final void delete(T instance) throws DbcEntryDaoException, NoSuchDbcEntryException, UnsupportedOperationException {
        // TODO Implement me!
    }

    @Override
    public final boolean isReadOnlyStore() {
        return false;
    }
}
