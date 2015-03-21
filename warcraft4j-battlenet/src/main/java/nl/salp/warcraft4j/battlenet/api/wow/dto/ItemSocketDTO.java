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
public class ItemSocketDTO {
    @JsonProperty("sockets")
    private SocketDTO[] sockets;
    @JsonProperty("socketBonus")
    private String socketBonus;

    public SocketDTO[] getSockets() {
        return sockets;
    }

    public void setSockets(SocketDTO[] sockets) {
        this.sockets = sockets;
    }

    public String getSocketBonus() {
        return socketBonus;
    }

    public void setSocketBonus(String socketBonus) {
        this.socketBonus = socketBonus;
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
