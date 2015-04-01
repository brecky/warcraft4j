package nl.salp.warcraft4j.files.clientdatabase.entry;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "BattleMasterList.dbc")
public class BattleMasterListEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.BATTLE_MASTER_LIST;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.INT32, numberOfEntries = 16)
    private int[] mapIds; // 16
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int instanceType; // 3: BG, 4: Arena
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int groupsAllowed;
    @DbcField(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int maxGroupSize;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int holidayWorldStateId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int minLevel; // (sync with PvPDifficulty.dbc content)
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int maxLevel; // (sync with PvPDifficulty.dbc content)
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int ratedPlayers;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int minPlayers;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int maxPlayers;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int flags; // 2: Rated Battlegrounds
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int iconId;
    @DbcField(order = 15, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String gameType;
    @DbcField(order = 16, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown1;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
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
