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

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcMapping;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * {@link DbcEntry} implementation for a parsed {@code Achievement.dbc} file.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "Achievement.dbc")
public class AchievementEntry implements DbcEntry {

    private static final int ACHIEVEMENT_FLAG_COUNTER = 0x00000001;    // Just count statistic (never stop and complete)
    private static final int ACHIEVEMENT_FLAG_HIDDEN = 0x00000002;    // Not sent to client - internal use only
    private static final int ACHIEVEMENT_FLAG_PLAY_NO_VISUAL = 0x00000004;    // Client does not play achievement earned visual
    private static final int ACHIEVEMENT_FLAG_SUMM = 0x00000008;    // Use summ criteria value from all requirements (and calculate max value)
    private static final int ACHIEVEMENT_FLAG_MAX_USED = 0x00000010;    // Show max criteria (and calculate max value ??)
    private static final int ACHIEVEMENT_FLAG_REQ_COUNT = 0x00000020;    // Use not zero req count (and calculate max value)
    private static final int ACHIEVEMENT_FLAG_AVERAGE = 0x00000040;    // Show as average value (value / time_in_days) depend from other flag (by def use last criteria value)
    private static final int ACHIEVEMENT_FLAG_BAR = 0x00000080;    // Show as progress bar (value / max vale) depend from other flag (by def use last criteria value)
    private static final int ACHIEVEMENT_FLAG_REALM_FIRST_REACH = 0x00000100;    //
    private static final int ACHIEVEMENT_FLAG_REALM_FIRST_KILL = 0x00000200;    //
    private static final int ACHIEVEMENT_FLAG_UNK3 = 0x00000400;    // ACHIEVEMENT_FLAG_HIDE_NAME_IN_TIE
    private static final int ACHIEVEMENT_FLAG_UNK4 = 0x00000800;    // first guild on realm done something
    private static final int ACHIEVEMENT_FLAG_SHOW_IN_GUILD_NEWS = 0x00001000;    // Shows in guild news
    private static final int ACHIEVEMENT_FLAG_SHOW_IN_GUILD_HEADER = 0x00002000;    // Shows in guild news header
    private static final int ACHIEVEMENT_FLAG_GUILD = 0x00004000;    //
    private static final int ACHIEVEMENT_FLAG_SHOW_GUILD_MEMBERS = 0x00008000;    //
    private static final int ACHIEVEMENT_FLAG_SHOW_CRITERIA_MEMBERS = 0x00010000;    //
    private static final int ACHIEVEMENT_FLAG_ACCOUNT = 0x00020000;

    private static final int ACHIEVEMENT_FACTION_HORDE = 0;
    private static final int ACHIEVEMENT_FACTION_ALLIANCE = 1;
    private static final int ACHIEVEMENT_FACTION_ANY = -1;


    /** The entry type. */
    private static final DbcType ENTRY_TYPE = DbcType.ACHIEVEMENT;
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.INT32)
    private int faction;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.INT32)
    private int mapId;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ACHIEVEMENT)
    private int parentAchievementId;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String title;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ACHIEVEMENT_CATEGORY)
    private int categoryId;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    private int achievementPoints;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32)
    private int uiOrder;
    @DbcFieldMapping(order = 10, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcFieldMapping(order = 11, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_ICON)
    private int iconId;
    @DbcFieldMapping(order = 12, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String reward;
    @DbcFieldMapping(order = 13, dataType = DbcDataType.UINT32)
    private int minimumCriteria;
    @DbcFieldMapping(order = 14, dataType = DbcDataType.UINT32)
    private int sharesCriteria;
    @DbcFieldMapping(order = 15, dataType = DbcDataType.UINT32)
    private int criteriaTreeId;

    @Override
    public DbcType getEntryType() {
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
