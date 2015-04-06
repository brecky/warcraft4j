package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "GlyphProperties.dbc")
public class GlyphPropertyEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.GLYPH_PROPERTY;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int spellId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int typeFlags;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int iconId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int glyphExclusiveCategoryId;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getTypeFlags() {
        return typeFlags;
    }

    public int getIconId() {
        return iconId;
    }

    public int getGlyphExclusiveCategoryId() {
        return glyphExclusiveCategoryId;
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
