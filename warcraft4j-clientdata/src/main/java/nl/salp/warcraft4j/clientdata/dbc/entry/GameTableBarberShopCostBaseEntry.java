package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "gtBarberShopCostBase.dbc")
public class GameTableBarberShopCostBaseEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.GAME_TABLE_BARBER_SHOP_COST_BASE;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int level;
    @DbcField(order = 2, dataType = DbcDataType.FLOAT)
    private float baseCost;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return level;
    }

    public int getLevel() {
        return level;
    }

    public float getBaseCost() {
        return baseCost;
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
