package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.dbc.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Dbc("GameTables.dbc")
public class GameTables {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(column = 2, dataType = DbcDataType.INT32)
    private int numberRows;
    @DbcField(column = 3, dataType = DbcDataType.INT32)
    private int numberColumns;

    public GameTables() {
    }

    public GameTables(int id, String name, int numberRows, int numberColumns) {
        this.id = id;
        this.name = name;
        this.numberRows = numberRows;
        this.numberColumns = numberColumns;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberRows() {
        return numberRows;
    }

    public int getNumberColumns() {
        return numberColumns;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
