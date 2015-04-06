package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "CharacterFacialHairStyles.dbc")
public class CharacterFacialHairStyleEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CHARACTER_FACIAL_HAIR_STYLE;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.CHARACTER_RACE)
    private int raceId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int sexId; // 0: male, 1: female
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int variation;
    /*
    [0] This is the small part of Geoset identificator. Determines mixing of different facial elements, on Night Elves this is chin hair.
    [1] ... on Night Elves, this controls eyebrows
    [2] ... on Night Elves, this controls mustache
    [3] 0
    [4] 0 or 2 - these both rows have strange values for foresttrolls. This controls small\big ears for BloodElves( geo 701,702)
     */
    @DbcField(order = 5, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] geosets; // 5

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getRaceId() {
        return raceId;
    }

    public int getSexId() {
        return sexId;
    }

    public int getVariation() {
        return variation;
    }

    public int[] getGeosets() {
        return geosets;
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
