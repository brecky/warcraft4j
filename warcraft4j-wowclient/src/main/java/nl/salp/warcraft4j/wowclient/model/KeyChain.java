package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Deprecated
@Dbc("KeyChain.db2")
public class KeyChain {
    @DbcField(column = 1, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 2, length=32, dataType = DbcDataType.STRING)
    private String key;

    public KeyChain() {
    }

    public KeyChain(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
