package nl.salp.warcraft4j.files.clientdatabase.entry;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "ItemUpgrade.db2")
public class ItemUpgradeEntry implements ClientDatabaseEntry {
    private ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.ITEM_UPGRADE;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int chainId; // ???
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int ilvlUpgrade;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int previousId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int costCurrencyId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int costCurrencyAmount;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getChainId() {
        return chainId;
    }

    public int getIlvlUpgrade() {
        return ilvlUpgrade;
    }

    public int getPreviousId() {
        return previousId;
    }

    public int getCostCurrencyId() {
        return costCurrencyId;
    }

    public int getCostCurrencyAmount() {
        return costCurrencyAmount;
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
