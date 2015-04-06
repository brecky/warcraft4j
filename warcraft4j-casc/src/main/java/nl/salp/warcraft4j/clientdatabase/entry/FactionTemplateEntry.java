package nl.salp.warcraft4j.clientdatabase.entry;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.clientdatabase.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "FactionTemplate.dbc")
public class FactionTemplateEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.FACTION_TEMPLATE;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int factionId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int factionGroupId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int friendGroupId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int enemyGroupId;
    @DbcField(order = 7, dataType = DbcDataType.UINT32, numberOfEntries = 4)
    private int[] enemies; // 4
    @DbcField(order = 8, dataType = DbcDataType.UINT32, numberOfEntries = 4)
    private int[] friends; // 4

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getFactionId() {
        return factionId;
    }

    public int getFlags() {
        return flags;
    }

    public int getFactionGroupId() {
        return factionGroupId;
    }

    public int getFriendGroupId() {
        return friendGroupId;
    }

    public int getEnemyGroupId() {
        return enemyGroupId;
    }

    public int[] getEnemies() {
        return enemies;
    }

    public int[] getFriends() {
        return friends;
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