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
@DbcFile(file = "ItemEffect.db2")
public class ItemEffectEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.ITEM_EFFECT;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int itemId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int orderIndex;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int spellId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int trigger;
    @DbcField(order = 6, dataType = DbcDataType.INT32)
    private int charges;
    @DbcField(order = 7, dataType = DbcDataType.INT32)
    private int cooldown;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int categoryId;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int categoryCooldown;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getTrigger() {
        return trigger;
    }

    public int getCharges() {
        return charges;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getCategoryCooldown() {
        return categoryCooldown;
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
