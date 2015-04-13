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
package nl.salp.warcraft4j.clientdata.dbc.dao;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;

import java.util.Collection;

/**
 * DbcEntry data access object.
 *
 * @author Barre Dijkstra
 */
public interface DbcEntryDAO<T extends DbcEntry> {
    /**
     * Find all persisted instances.
     *
     * @return The instances.
     *
     * @throws DbcEntryDaoException When there was a problem reading the instances.
     */
    Collection<T> findAll() throws DbcEntryDaoException;

    /**
     * Find a persisted instance by its id.
     *
     * @param id The id of the instance.
     *
     * @return The instance.
     *
     * @throws DbcEntryDaoException    When there was a problem reading the instances.
     * @throws NoSuchDbcEntryException When no instance was found with the given id.
     */
    T find(int id) throws DbcEntryDaoException, NoSuchDbcEntryException;

    /**
     * Persist a new instance.
     *
     * @param instance The instance to persist.
     *
     * @throws DbcEntryDaoException          When there was a problem persisting the instances.
     * @throws DuplicateDbcEntryException    When there is already an instance persisted with the same id.
     * @throws UnsupportedOperationException When the persistence store doesn't support saving of data.
     * @see #isReadOnlyStore()
     */
    void create(T instance) throws DbcEntryDaoException, DuplicateDbcEntryException, UnsupportedOperationException;

    /**
     * Update an existing instance.
     *
     * @param instance The instance to persist.
     *
     * @throws DbcEntryDaoException          When there was a problem persisting the instances.
     * @throws NoSuchDbcEntryException       When no instance was found with the given id.
     * @throws UnsupportedOperationException When the persistence store doesn't support saving of data.
     * @see #isReadOnlyStore()
     */
    void update(T instance) throws DbcEntryDaoException, NoSuchDbcEntryException, UnsupportedOperationException;

    /**
     * Persist an instance, persisting a new instance if it's not already persisted or updating the existing instance if it already was persisted.
     *
     * @param instance The instance to persist.
     *
     * @throws DbcEntryDaoException          When there was a problem persisting the instances.
     * @throws UnsupportedOperationException When the persistence store doesn't support saving of data.
     * @see #isReadOnlyStore()
     */
    void save(T instance) throws DbcEntryDaoException, UnsupportedOperationException;


    /**
     * Persist new instances, ignoring those that were already persisted.
     *
     * @param instances The instances to persist.
     *
     * @throws DbcEntryDaoException          When there was a problem persisting the instances.
     * @throws UnsupportedOperationException When the persistence store doesn't support saving of data.
     * @see #isReadOnlyStore()
     */
    void create(Collection<T> instances) throws DbcEntryDaoException, UnsupportedOperationException;

    /**
     * Update existing instances, ignoring those that are not persisted yet.
     *
     * @param instances The instances to persist.
     *
     * @throws DbcEntryDaoException          When there was a problem persisting the instances.
     * @throws UnsupportedOperationException When the persistence store doesn't support saving of data.
     * @see #isReadOnlyStore()
     */
    void update(Collection<T> instances) throws DbcEntryDaoException, UnsupportedOperationException;

    /**
     * Persist instances, either creating new instances or updating the existing ones.
     *
     * @param instances The instances to persist.
     *
     * @throws DbcEntryDaoException          When there was a problem persisting the instances.
     * @throws UnsupportedOperationException When the persistence store doesn't support saving of data.
     * @see #isReadOnlyStore()
     */
    void save(Collection<T> instances) throws DbcEntryDaoException, UnsupportedOperationException;

    /**
     * Delete a persisted an instance.
     *
     * @param instance The instance to delete.
     *
     * @throws DbcEntryDaoException          When there was a problem deleting the instances.
     * @throws NoSuchDbcEntryException       When the instance to delete instance was not found.
     * @throws UnsupportedOperationException When the persistence store doesn't support deletion of data.
     * @see #isReadOnlyStore()
     */
    void delete(T instance) throws DbcEntryDaoException, NoSuchDbcEntryException, UnsupportedOperationException;

    /**
     * Check if the persistence store is read-only (e.g. does not allow persistence or deletion of data).
     *
     * @return {@code true} if the persistence store is read-only.
     */
    boolean isReadOnlyStore();
}
