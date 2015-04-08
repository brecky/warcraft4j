package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "CharTitles.dbc")
public class CharacterTitleEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CHARACTER_TITLE;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int conditionId;
    @DbcField(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String title;
    @DbcField(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String title2;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int maskId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int flags;


    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getConditionId() {
        return conditionId;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle2() {
        return title2;
    }

    public int getMaskId() {
        return maskId;
    }

    public int getFlags() {
        return flags;
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