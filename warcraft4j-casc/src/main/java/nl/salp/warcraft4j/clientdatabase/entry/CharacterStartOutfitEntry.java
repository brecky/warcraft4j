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
@DbcFile(file = "CharStartOutfit.dbc")
public class CharacterStartOutfitEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.CHARACTER_START_OUTFIT;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.BYTE)
    private byte raceId;
    @DbcField(order = 3, dataType = DbcDataType.BYTE)
    private byte classId;
    @DbcField(order = 4, dataType = DbcDataType.BYTE)
    private byte gender;
    @DbcField(order = 5, dataType = DbcDataType.BYTE)
    private byte outfitId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32, numberOfEntries = 24)
    private int[] itemIds; // 24
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int startPetDisplayId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int startPetFamilyId;

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

    public int getClassId() {
        return classId;
    }

    public int getGender() {
        return gender;
    }

    public int getOutfitId() {
        return outfitId;
    }

    public int[] getItemIds() {
        return itemIds;
    }

    public int getStartPetDisplayId() {
        return startPetDisplayId;
    }

    public int getStartPetFamilyId() {
        return startPetFamilyId;
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
