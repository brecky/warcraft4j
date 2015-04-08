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
@DbcMapping(file = "BarberShopStyle.dbc")
public class BarberShopStyleEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.BARBER_SHOP_STYLE;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int type; // 1 value 0 -> hair, value 2 -> facialhair
    @DbcField(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(order = 5, dataType = DbcDataType.FLOAT)
    private float costModifier;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int race;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int sex;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int hairId;

    @Override
    public DbcType getEntryType() {
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
