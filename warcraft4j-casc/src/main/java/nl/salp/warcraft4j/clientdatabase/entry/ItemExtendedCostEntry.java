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
@DbcFile(file = "ItemExtendedCost.db2")
public class ItemExtendedCostEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.ITEM_EXTENDED_COST;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int requiredArenaSlot;
    @DbcField(order = 3, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredItem;
    @DbcField(order = 4, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredItemCount;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int requiredPersonalArenaRating;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int itemPurchaseGroup;
    @DbcField(order = 7, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredCurrency;
    @DbcField(order = 8, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredCurrencyCount;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int requiredFactionId;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int requiredFactionStanding;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int requirementsFlag;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int requiredAchievementId;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int requiredMoney;


    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getRequiredArenaSlot() {
        return requiredArenaSlot;
    }

    public int[] getRequiredItem() {
        return requiredItem;
    }

    public int[] getRequiredItemCount() {
        return requiredItemCount;
    }

    public int getItemPurchaseGroup() {
        return itemPurchaseGroup;
    }

    public int[] getRequiredCurrency() {
        return requiredCurrency;
    }

    public int[] getRequiredCurrencyCount() {
        return requiredCurrencyCount;
    }

    public int getRequiredFactionId() {
        return requiredFactionId;
    }

    public int getRequiredFactionStanding() {
        return requiredFactionStanding;
    }

    public int getRequirementsFlag() {
        return requirementsFlag;
    }

    public int getRequiredAchievementId() {
        return requiredAchievementId;
    }

    public int getRequiredMoney() {
        return requiredMoney;
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
