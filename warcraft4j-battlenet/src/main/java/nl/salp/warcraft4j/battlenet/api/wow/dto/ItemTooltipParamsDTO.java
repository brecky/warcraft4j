package nl.salp.warcraft4j.battlenet.api.wow.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class ItemTooltipParamsDTO {
    @JsonProperty("tinker")
    private int tinker;
    @JsonProperty("transmogItem")
    private long transmogItem;
    @JsonProperty("set")
    private int[] set;
    @JsonProperty("enchant")
    private long enchant;
    @JsonProperty("gem0")
    private long gem0;
    @JsonProperty("gem1")
    private long gem1;
    @JsonProperty("gem2")
    private long gem2;
    @JsonProperty("upgrade")
    private ItemUpgradeDTO upgrade;

    public int getTinker() {
        return tinker;
    }

    public void setTinker(int tinker) {
        this.tinker = tinker;
    }

    public long getTransmogItem() {
        return transmogItem;
    }

    public void setTransmogItem(long transmogItem) {
        this.transmogItem = transmogItem;
    }

    public int[] getSet() {
        return set;
    }

    public void setSet(int[] set) {
        this.set = set;
    }

    public long getEnchant() {
        return enchant;
    }

    public void setEnchant(long enchant) {
        this.enchant = enchant;
    }

    public long getGem0() {
        return gem0;
    }

    public void setGem0(long gem0) {
        this.gem0 = gem0;
    }

    public long getGem1() {
        return gem1;
    }

    public void setGem1(long gem1) {
        this.gem1 = gem1;
    }

    public long getGem2() {
        return gem2;
    }

    public void setGem2(long gem2) {
        this.gem2 = gem2;
    }

    public ItemUpgradeDTO getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(ItemUpgradeDTO upgrade) {
        this.upgrade = upgrade;
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
