package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "ItemToBattlePetSpecies.db2")
public class ItemToBattlePetSpeciesEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ITEM_TO_BATTLEPET_SPECIES;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int itemId;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int petSpecies;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPetSpecies() {
        return petSpecies;
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
