package nl.salp.warcraft4j.wowclient.data;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Dbc("StringLookups.dbc")
public class StringLookups {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String string;

    public StringLookups() {
    }

    public StringLookups(int id, String string) {
        this.id = id;
        this.string = string;
    }

    public int getId() {
        return id;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
