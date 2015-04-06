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
@DbcFile(file = "CharShipmentContainer.db2")
public class CharacterShipmentContainerEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.CHARACTER_SHIPMENT_CONTAINER;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown2;
    @DbcField(order = 3, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown3;
    @DbcField(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 5, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown5;
    @DbcField(order = 6, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown6;
    @DbcField(order = 7, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown7;
    @DbcField(order = 8, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown8;
    @DbcField(order = 9, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown9;
    @DbcField(order = 10, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown10;
    @DbcField(order = 11, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(order = 12, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown12;
    @DbcField(order = 13, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown13;

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