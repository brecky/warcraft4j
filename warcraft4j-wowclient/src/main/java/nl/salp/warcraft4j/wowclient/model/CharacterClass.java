package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Dbc("ChrClasses.dbc")
public class CharacterClass {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.INT32)
    private int displayPower;
    @DbcField(column = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String petName;
    @DbcField(column = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(column = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String femaleName;
    @DbcField(column = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String maleName;
    @DbcField(column = 6, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String fileName;
    @DbcField(column = 7, dataType = DbcDataType.INT32)
    private int spellClassSet;
    @DbcField(column = 8, dataType = DbcDataType.INT32)
    private int flags;
    @DbcField(column = 9, dataType = DbcDataType.INT32)
    private int cinematicSequenceId;
    @DbcField(column = 10, dataType = DbcDataType.INT32)
    private int attackPowerPerStrength;
    @DbcField(column = 11, dataType = DbcDataType.INT32)
    private int attackPowerPerAgility;
    @DbcField(column = 12, dataType = DbcDataType.INT32)
    private int rangedAttackPowerPerAgility;
    @DbcField(column = 13, dataType = DbcDataType.INT32)
    private int defaultSpec;
    @DbcField(column = 14, dataType = DbcDataType.INT32)
    private int unknown;
    @DbcField(column = 15, dataType = DbcDataType.INT32)
    private int createScreenFileDataId;
    @DbcField(column = 16, dataType = DbcDataType.INT32)
    private int selectScreenFileDataId;
    @DbcField(column = 17, dataType = DbcDataType.INT32)
    private int lowResScreenFileDataId;
    @DbcField(column = 18, dataType = DbcDataType.INT32)
    private int iconFileDataId;

    public CharacterClass() {
    }

    public CharacterClass(int id, int displayPower, String petName, String name, String femaleName, String maleName, String fileName, int spellClassSet, int flags, int cinematicSequenceId, int attackPowerPerStrength, int attackPowerPerAgility, int rangedAttackPowerPerAgility, int defaultSpec, int unknown, int createScreenFileDataId, int selectScreenFileDataId, int lowResScreenFileDataId, int iconFileDataId) {
        this.id = id;
        this.displayPower = displayPower;
        this.petName = petName;
        this.name = name;
        this.femaleName = femaleName;
        this.maleName = maleName;
        this.fileName = fileName;
        this.spellClassSet = spellClassSet;
        this.flags = flags;
        this.cinematicSequenceId = cinematicSequenceId;
        this.attackPowerPerStrength = attackPowerPerStrength;
        this.attackPowerPerAgility = attackPowerPerAgility;
        this.rangedAttackPowerPerAgility = rangedAttackPowerPerAgility;
        this.defaultSpec = defaultSpec;
        this.unknown = unknown;
        this.createScreenFileDataId = createScreenFileDataId;
        this.selectScreenFileDataId = selectScreenFileDataId;
        this.lowResScreenFileDataId = lowResScreenFileDataId;
        this.iconFileDataId = iconFileDataId;
    }

    public int getId() {
        return id;
    }

    public int getDisplayPower() {
        return displayPower;
    }

    public String getPetName() {
        return petName;
    }

    public String getName() {
        return name;
    }

    public String getFemaleName() {
        return femaleName;
    }

    public String getMaleName() {
        return maleName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getSpellClassSet() {
        return spellClassSet;
    }

    public int getFlags() {
        return flags;
    }

    public int getCinematicSequenceId() {
        return cinematicSequenceId;
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

    public int getUnknown() {
        return unknown;
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

    public int getIconFileDataId() {
        return iconFileDataId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
