package nl.salp.warcraft4j.clientdatabase.entry;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.clientdatabase.parser.DbcFile;
import nl.salp.warcraft4j.clientdatabase.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "CharHairGeosets.dbc")
public class CharacterHairGeosetEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.CHARACTER_HAIR_GEOSET;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.CHARACTER_RACE)
    private int raceId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int gender; // 0: male, 1: female
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int variationId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int variationType;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int geosetId;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int geosetType;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int bald; // 0: hair, 1: bald
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int colorIndex;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getRaceId() {
        return raceId;
    }

    public int getGender() {
        return gender;
    }

    public boolean isMale() {
        return gender == 0;
    }

    public int getVariationId() {
        return variationId;
    }

    public int getVariationType() {
        return variationType;
    }

    public int getGeosetId() {
        return geosetId;
    }

    public int getGeosetType() {
        return geosetType;
    }

    public int getBald() {
        return bald;
    }

    public boolean isBald() {
        return (bald == 1);
    }

    public int getColorIndex() {
        return colorIndex;
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
