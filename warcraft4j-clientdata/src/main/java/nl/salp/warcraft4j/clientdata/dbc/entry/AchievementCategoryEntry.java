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

package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcStore;
import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * {@link DbcEntry} implementation for a parsed {@code Achievement_Category.dbc} file.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "Achievement_Category.dbc")
public class AchievementCategoryEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ACHIEVEMENT_CATEGORY;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ACHIEVEMENT_CATEGORY)
    private int parentAchievementCategoryId; // -1 for main category
    @DbcField(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int uiOrder;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    /**
     * Get the id of the parent achievement category.
     *
     * @return The id or {@code -1} if there is no parent (implying a top-level category).
     */
    public int getParentAchievementCategoryId() {
        return parentAchievementCategoryId;
    }

    /**
     * Get the parent achievement category, resolving it on the client database.
     *
     * @param dbcStore The client database.
     *
     * @return The parent achievement or {@code null} if it could not be resolved.
     *
     * @see #getParentAchievementCategoryId()
     */
    public AchievementCategoryEntry getParent(DbcStore dbcStore) {
        AchievementCategoryEntry parent = null;
        if (this.parentAchievementCategoryId > 0 && dbcStore != null) {
            parent = dbcStore.resolve(AchievementCategoryEntry.class, this.parentAchievementCategoryId);
        }
        return parent;
    }

    /**
     * Get the name of the achievement category.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the order index of the achievement category in the UI.
     *
     * @return The order index.
     */
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
