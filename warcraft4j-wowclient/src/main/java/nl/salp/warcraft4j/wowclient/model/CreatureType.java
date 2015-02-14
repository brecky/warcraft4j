package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.dbc.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Dbc("CreatureType.dbc")
public class CreatureType {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(column = 2, dataType = DbcDataType.INT32)
    private int flags;

    public CreatureType() {
    }

    public CreatureType(int id, String name, int flags) {
        this.id = id;
        this.name = name;
        this.flags = flags;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
