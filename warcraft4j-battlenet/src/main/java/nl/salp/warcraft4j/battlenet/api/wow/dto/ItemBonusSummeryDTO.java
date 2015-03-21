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
public class ItemBonusSummeryDTO {
    @JsonProperty("defaultBonusLists")
    private int[] defaultBonusLists;
    @JsonProperty("chanceBonusLists")
    private int[] chanceBonusLists;
    @JsonProperty("bonusChances")
    private int[] bonusChances;

    public int[] getDefaultBonusLists() {
        return defaultBonusLists;
    }

    public void setDefaultBonusLists(int[] defaultBonusLists) {
        this.defaultBonusLists = defaultBonusLists;
    }

    public int[] getChanceBonusLists() {
        return chanceBonusLists;
    }

    public void setChanceBonusLists(int[] chanceBonusLists) {
        this.chanceBonusLists = chanceBonusLists;
    }

    public int[] getBonusChances() {
        return bonusChances;
    }

    public void setBonusChances(int[] bonusChances) {
        this.bonusChances = bonusChances;
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
