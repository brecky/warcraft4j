package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import nl.salp.warcraft4j.clientdata.dbc.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "ItemAppearance.db2")
public class ItemAppearanceEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ITEM_APPEARANCE;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    // TODO Determine link
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int displayId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_ICON)
    private int iconId;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getDisplayId() {
        return displayId;
    }

    public int getIconId() {
        return iconId;
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
