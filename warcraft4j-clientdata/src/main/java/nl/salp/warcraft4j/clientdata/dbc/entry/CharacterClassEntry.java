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
@DbcMapping(file = "ChrClasses.dbc")
public class CharacterClassEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CHARACTER_CLASS;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int powerTypeId;
    @DbcField(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String petNameToken;
    @DbcField(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String nameFemale;
    @DbcField(order = 6, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String nameMale;
    @DbcField(order = 7, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String capitalizedName;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int spellClassSet;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int flags; // (0x08 HasRelicSlot)
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int cinematicSequenceID;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int attackPowerPerStrength;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int attackPowerPerAgility;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int rangedAttackPowerPerAgility;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int defaultSpec;
    @DbcField(order = 15, dataType = DbcDataType.UINT32)
    private int createScreenFileDataId;
    @DbcField(order = 16, dataType = DbcDataType.UINT32)
    private int selectScreenFileDataId;
    @DbcField(order = 17, dataType = DbcDataType.UINT32)
    private int lowResScreenFileDataId;
    @DbcField(order = 18, dataType = DbcDataType.UINT32)
    private int iconId;
    @DbcField(order = 19, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown1;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getPowerTypeId() {
        return powerTypeId;
    }

    public String getPetNameToken() {
        return petNameToken;
    }

    public String getName() {
        return name;
    }

    public String getNameFemale() {
        return nameFemale;
    }

    public String getNameMale() {
        return nameMale;
    }

    public String getCapitalizedName() {
        return capitalizedName;
    }

    public int getSpellClassSet() {
        return spellClassSet;
    }

    public int getFlags() {
        return flags;
    }

    public int getCinematicSequenceID() {
        return cinematicSequenceID;
    }

    public int getAttackPowerPerStrength() {
        return attackPowerPerStrength;
    }

    public int getAttackPowerPerAgility() {
        return attackPowerPerAgility;
    }

    public int getRangedAttackPowerPerAgility() {
        return rangedAttackPowerPerAgility;
    }

    public int getDefaultSpec() {
        return defaultSpec;
    }

    public int getCreateScreenFileDataId() {
        return createScreenFileDataId;
    }

    public int getSelectScreenFileDataId() {
        return selectScreenFileDataId;
    }

    public int getLowResScreenFileDataId() {
        return lowResScreenFileDataId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getUnknown1() {
        return unknown1;
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
