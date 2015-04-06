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
@DbcFile(file = "CombatCondition.dbc")
public class CombatConditionEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.COMBAT_CONDITION;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int worldStateExpressionId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int selfConditionId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int targetConditionId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] friendConditionId; // 2
    @DbcField(order = 6, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] friendConditionOp; // 2
    @DbcField(order = 7, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] friendConditionCount; // 2
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int friendConditionLogic;
    @DbcField(order = 9, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] enemyConditionId; // 2
    @DbcField(order = 10, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] enemyConditionOp; // 2
    @DbcField(order = 11, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] enemyConditionCount; // 2
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int enemyConditionLogic;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getWorldStateExpressionId() {
        return worldStateExpressionId;
    }

    public int getSelfConditionId() {
        return selfConditionId;
    }

    public int getTargetConditionId() {
        return targetConditionId;
    }

    public int[] getFriendConditionId() {
        return friendConditionId;
    }

    public int[] getFriendConditionOp() {
        return friendConditionOp;
    }

    public int[] getFriendConditionCount() {
        return friendConditionCount;
    }

    public int getFriendConditionLogic() {
        return friendConditionLogic;
    }

    public int[] getEnemyConditionId() {
        return enemyConditionId;
    }

    public int[] getEnemyConditionOp() {
        return enemyConditionOp;
    }

    public int[] getEnemyConditionCount() {
        return enemyConditionCount;
    }

    public int getEnemyConditionLogic() {
        return enemyConditionLogic;
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
