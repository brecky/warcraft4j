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
@DbcFile(file = "BroadcastText.db2")
public class BroadcastTextEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.BROADCAST_TEXT;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int languageId;
    @DbcField(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String maleText;
    @DbcField(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String femaleText;
    @DbcField(order = 5, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] emoteId; // 3
    @DbcField(order = 6, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] emoteDelay; // 3
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int soundId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknownEmoteId;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int type;


    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getLanguageId() {
        return languageId;
    }

    public String getMaleText() {
        return maleText;
    }

    public String getFemaleText() {
        return femaleText;
    }

    public int[] getEmoteId() {
        return emoteId;
    }

    public int[] getEmoteDelay() {
        return emoteDelay;
    }

    public int getSoundId() {
        return soundId;
    }

    public int getUnknownEmoteId() {
        return unknownEmoteId;
    }

    public int getType() {
        return type;
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
