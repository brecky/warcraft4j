package nl.salp.warcraft4j.files.clientdatabase.entry;

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
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "AnimationData.dbc")
public class AnimationDataEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.ANIMATION_DATA;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    /*
0: weapon not affected by animation,
4: sheathe weapons automatically,
16: sheathe weapons automatically,
32: unsheathe weapons.
     */
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.ANIMATION_DATA)
    private int fallbackId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int behaviourId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int behaviourTier; // 0 = normal, 3 = flying


    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    public int getFallbackId() {
        return fallbackId;
    }

    public int getBehaviourId() {
        return behaviourId;
    }

    public int getBehaviourTier() {
        return behaviourTier;
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
