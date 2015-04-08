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
@DbcMapping(file = "CurrencyTypes.db2")
public class CurrencyTypeEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CURRENCY_TYPE;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.CURRENCY_CATEGORY)
    private int currencyCategoryId;
    @DbcField(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE, numberOfEntries = 2)
    private String[] inventoryIcon;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int spellWeight;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int spellCategory;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int maxQuantity;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int maxEarnablePerWeek;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int quality;
    @DbcField(order = 11, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCurrencyCategoryId() {
        return currencyCategoryId;
    }

    public String getName() {
        return name;
    }

    public String[] getInventoryIcon() {
        return inventoryIcon;
    }

    public int getSpellWeight() {
        return spellWeight;
    }

    public int getSpellCategory() {
        return spellCategory;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getMaxEarnablePerWeek() {
        return maxEarnablePerWeek;
    }

    public int getFlags() {
        return flags;
    }

    public int getQuality() {
        return quality;
    }

    public String getDescription() {
        return description;
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
