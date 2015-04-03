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

package nl.salp.warcraft4j.clientdatabase.entry;

import nl.salp.warcraft4j.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.clientdatabase.parser.DbcFile;
import nl.salp.warcraft4j.clientdatabase.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "CriteriaTree.dbc")
public class CriteriaTreeEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.CRITERIA_TREE;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.CRITERIA)
    private int criteriaId;
    @DbcField(order = 3, dataType = DbcDataType.ULONG)
    private long amount;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int operator;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.CRITERIA_TREE)
    private int parentId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 7, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int uiOrder;
    @DbcField(order = 9, dataType = DbcDataType.BYTE, numberOfEntries = 4, padding = true)
    private transient byte[] padding1;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCriteriaId() {
        return criteriaId;
    }

    /**
     * Get the criteria for this tree.
     *
     * @param clientDatabase The client database to resolve the criteria on.
     *
     * @return The criteria or {@code null} if no criteria was linked.
     *
     * @see #getCriteriaId()
     */
    public CriteriaEntry getCriteria(ClientDatabase clientDatabase) {
        CriteriaEntry criteria = null;
        if (criteriaId > 0 && clientDatabase != null) {
            criteria = clientDatabase.resolve(CriteriaEntry.class, criteriaId);
        }
        return criteria;
    }

    public long getAmount() {
        return amount;
    }

    public int getOperator() {
        return operator;
    }

    public int getParentId() {
        return parentId;
    }

    /**
     * Get the parent criteria tree.
     *
     * @param clientDatabase The client database to resolve the parent on.
     *
     * @return The parent or {@code null} if no parent could be resolved.
     *
     * @see #getParentId()
     */
    public CriteriaTreeEntry getParent(ClientDatabase clientDatabase) {
        CriteriaTreeEntry entry = null;
        if (parentId > 0 && clientDatabase != null) {
            entry = clientDatabase.resolve(CriteriaTreeEntry.class, parentId);
        }
        return entry;
    }

    public int getFlags() {
        return flags;
    }

    public String getDescription() {
        return description;
    }

    public int getUiOrder() {
        return uiOrder;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
