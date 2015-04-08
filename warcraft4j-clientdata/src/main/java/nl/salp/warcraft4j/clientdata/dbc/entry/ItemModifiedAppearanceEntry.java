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
@DbcMapping(file = "ItemModifiedAppearance.db2")
public class ItemModifiedAppearanceEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ITEM_MODIFIED_APPEARANCE;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int itemId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int appearanceModId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int appearanceId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int iconId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int index;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getAppearanceModId() {
        return appearanceModId;
    }

    public int getAppearanceId() {
        return appearanceId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getIndex() {
        return index;
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
