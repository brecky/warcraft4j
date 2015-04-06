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
public class ItemSpellDTO {
    @JsonProperty("spellId")
    private long spellId;
    @JsonProperty("spell")
    private SpellDTO spell;
    @JsonProperty("nCharges")
    private int nCharges;
    @JsonProperty("consumable")
    private boolean consumable;
    @JsonProperty("categoryId")
    private int categoryId;
    @JsonProperty("trigger")
    private String trigger;

    public long getSpellId() {
        return spellId;
    }

    public void setSpellId(long spellId) {
        this.spellId = spellId;
    }

    public SpellDTO getSpell() {
        return spell;
    }

    public void setSpell(SpellDTO spell) {
        this.spell = spell;
    }

    public int getnCharges() {
        return nCharges;
    }

    public void setnCharges(int nCharges) {
        this.nCharges = nCharges;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public void setConsumable(boolean consumable) {
        this.consumable = consumable;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
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
