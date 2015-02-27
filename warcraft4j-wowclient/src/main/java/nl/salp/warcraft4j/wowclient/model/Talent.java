package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Deprecated
@Dbc("Talent.dbc")
public class Talent {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.INT32)
    private int specId;
    @DbcField(column = 2, dataType = DbcDataType.INT32)
    private int tierId;
    @DbcField(column = 3, dataType = DbcDataType.INT32)
    private int columnIndex;
    @DbcField(column = 4, dataType = DbcDataType.INT32)
    private int spellId;
    @DbcField(column = 5, dataType = DbcDataType.INT32)
    private int flags;
    @DbcField(column = 6, dataType = DbcDataType.INT32)
    private int categoryMask;
    @DbcField(column = 7, dataType = DbcDataType.INT32)
    private int unknown;
    @DbcField(column = 8, dataType = DbcDataType.INT32)
    private int classId;
    @DbcField(column = 9, dataType = DbcDataType.INT32)
    private int overridesSpellId;
    @DbcField(column = 10, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;

    public Talent() {
    }

    public Talent(int id, int specId, int tierId, int columnIndex, int spellId, int flags, int categoryMask, int unknown, int classId, int overridesSpellId, String description) {
        this.id = id;
        this.specId = specId;
        this.tierId = tierId;
        this.columnIndex = columnIndex;
        this.spellId = spellId;
        this.flags = flags;
        this.categoryMask = categoryMask;
        this.unknown = unknown;
        this.classId = classId;
        this.overridesSpellId = overridesSpellId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getSpecId() {
        return specId;
    }

    public int getTierId() {
        return tierId;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getFlags() {
        return flags;
    }

    public int getCategoryMask() {
        return categoryMask;
    }

    public int getUnknown() {
        return unknown;
    }

    public int getClassId() {
        return classId;
    }

    public int getOverridesSpellId() {
        return overridesSpellId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
