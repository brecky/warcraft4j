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
@DbcFile(file = "AnimKit.dbc")
public class AnimationKitEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.ANIMATION_KIT;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int oneShotDuration;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int oneShotStopAnimationKitId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int lowDefinitionAnimationKitId;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getOneShotDuration() {
        return oneShotDuration;
    }

    public int getOneShotStopAnimationKitId() {
        return oneShotStopAnimationKitId;
    }

    public int getLowDefinitionAnimationKitId() {
        return lowDefinitionAnimationKitId;
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
