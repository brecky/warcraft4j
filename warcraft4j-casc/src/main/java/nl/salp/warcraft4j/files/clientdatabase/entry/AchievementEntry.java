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

package nl.salp.warcraft4j.files.clientdatabase.entry;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcFile;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * {@link ClientDatabaseEntry} implementation for a parsed {@code Achievement.dbc} file.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "Achievement.dbc")
public class AchievementEntry implements ClientDatabaseEntry {
    /** The entry type. */
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.ACHIEVEMENT;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.INT32)
    private int faction;
    @DbcField(order = 3, dataType = DbcDataType.INT32)
    private int mapId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.ACHIEVEMENT)
    private int parentAchievementId;
    @DbcField(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String title;
    @DbcField(order = 6, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.ACHIEVEMENT_CATEGORY)
    private int categoryId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int achievementPoints;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int uiOrder;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.SPELL_ICON)
    private int iconId;
    @DbcField(order = 12, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String reward;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int minimumCriteria;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int sharesCriteria;
    @DbcField(order = 15, dataType = DbcDataType.UINT32)
    private int criteriaTreeId;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    /**
     * Get the faction for which the achievement is applicable.
     * <p/>
     * {@code -1} for both factions, {@code 0} for horde and {@code 1} for alliance
     *
     * @return The faction.
     */
    public int getFaction() {
        return faction;
    }

    /**
     * Get the map id related to the achievement.
     *
     * @return The map id or {@code -1} for no map link.
     */
    public int getMapId() {
        return mapId;
    }

    /**
     * Get the id of the parent achievement.
     * <p/>
     * When there is a parent achievement:
     * <ul>
     * <li>The achievement can not start before the parent is completed</li>
     * <li>All criteria from the parent should be used when the achievement doesn't have its own</li>
     * <li>The progress of the parent achievement should be used as the starting point of the achievement</li>
     * </ul>
     *
     * @return The id of the parent achievement or {@code -1} for no parent achievement.
     */
    public int getParentAchievementId() {
        return parentAchievementId;
    }

    /**
     * Get the parent achievement.
     * <p/>
     * When there is a parent achievement:
     * <ul>
     * <li>The achievement can not start before the parent is completed</li>
     * <li>All criteria from the parent should be used when the achievement doesn't have its own</li>
     * <li>The progress of the parent achievement should be used as the starting point of the achievement</li>
     * </ul>
     *
     * @param clientDatabase The client database instance to resolve the parent achievement on.
     *
     * @return The parent achievement or {@code null} if the achievement could not be resolved.
     *
     * @see #getParentAchievementId()
     */
    public AchievementEntry getParentAchievement(ClientDatabase clientDatabase) {
        AchievementEntry entry = null;
        if (parentAchievementId > 0 && clientDatabase != null) {
            entry = clientDatabase.resolve(AchievementEntry.class, parentAchievementId);
        }
        return entry;
    }

    /**
     * Get the title of the achievement.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the description of the achievement.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the id of the achievement category the achievement belongs to.
     *
     * @return The id.
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Get the achievement category the achievement belongs to.
     *
     * @param clientDatabase The client database instance to resolve the achievement category on.
     *
     * @return The achievement category or {@code null} if it could not be resolved.
     *
     * @see #getCategoryId()
     */
    public AchievementCategoryEntry getCategory(ClientDatabase clientDatabase) {
        AchievementCategoryEntry category = null;
        if (categoryId > 0 && clientDatabase != null) {
            category = clientDatabase.resolve(AchievementCategoryEntry.class, categoryId);
        }
        return category;
    }

    /**
     * Get the amount of achievement points the achievement is worth.
     *
     * @return The achievement points.
     */
    public int getAchievementPoints() {
        return achievementPoints;
    }

    /**
     * Get the order index of the achievement on the UI.
     *
     * @return The order index.
     */
    public int getUiOrder() {
        return uiOrder;
    }

    /**
     * Get the flags for the achievement.
     * <p/>
     * TODO Document the possible flags and their meaning.
     *
     * @return The flags.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Get the id of the icon to display for the achievement.
     *
     * @return The id.
     */
    public int getIconId() {
        return iconId;
    }

    /**
     * Get the icon to display for the achievement.
     *
     * @param clientDatabase The client database to resolve the icon on.
     *
     * @return The icon or {@code null} if the icon could not be resolved.
     *
     * @see #getIconId()
     */
    public SpellIconEntry getIcon(ClientDatabase clientDatabase) {
        SpellIconEntry icon = null;
        if (iconId > 0 && clientDatabase != null) {
            icon = clientDatabase.resolve(SpellIconEntry.class, iconId);
        }
        return icon;

    }

    /**
     * Get the reward text.
     *
     * @return The reward text or {@code null} if no reward is available.
     */
    public String getReward() {
        return reward;
    }

    /**
     * Get the minimum criteria.
     *
     * @return The minimum criteria.
     */
    public int getMinimumCriteria() {
        return minimumCriteria;
    }

    /**
     * Get the "sharesCriteria" ???
     * <p/>
     * TODO Check if this isn't a boolean that indicates if the achievement is account-wide.
     *
     * @return ???
     */
    public int getSharesCriteria() {
        return sharesCriteria;
    }

    /**
     * Get the id of the criteria tree.
     *
     * @return The id of the criteria tree.
     */
    public int getCriteriaTreeId() {
        return criteriaTreeId;
    }

    /**
     *
     * @param clientDatabase
     * @return
     */
    public CriteriaTreeEntry getCriteriaTree(ClientDatabase clientDatabase) {
        CriteriaTreeEntry tree = null;
        if (criteriaTreeId > 0 && clientDatabase != null) {
            tree = clientDatabase.resolve(CriteriaTreeEntry.class, criteriaTreeId);
        }
        return tree;
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
