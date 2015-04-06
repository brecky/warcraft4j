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
@DbcFile(file = "MountType.dbc")
public class MountTypeEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.MOUNT_TYPE;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32, numberOfEntries = 24, knownMeaning = false)
    private int[] capabilities;

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