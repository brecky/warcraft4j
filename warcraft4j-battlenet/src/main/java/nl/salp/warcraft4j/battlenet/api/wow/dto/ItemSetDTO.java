package nl.salp.warcraft4j.battlenet.api.wow.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * World of Warcraft item set DTO.
 *
 * @author Barre Dijkstra
 */
public class ItemSetDTO {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("setBonuses")
    private ItemSetBonusDTO[] setBonuses;
    @JsonProperty("items")
    private int[] items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemSetBonusDTO[] getSetBonuses() {
        return setBonuses;
    }

    public void setSetBonuses(ItemSetBonusDTO[] setBonuses) {
        this.setBonuses = setBonuses;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
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
