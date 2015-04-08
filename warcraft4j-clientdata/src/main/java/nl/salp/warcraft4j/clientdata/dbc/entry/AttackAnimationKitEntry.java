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
@DbcMapping(file = "AttackAnimKits.dbc")
public class AttackAnimationKitEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ATTACK_ANIMATION_KIT;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ANIMATION_DATA)
    private int animationDataId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ATTACK_ANIMATION_TYPE)
    private int attackAnimationTypeId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int weaponAnimationType; // 0: main-hand, 1: off-hand

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
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
