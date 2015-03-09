package nl.salp.warcraft4j.wowclient.data;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 * <p/>
 * FIXME Field names (and maybe types) are incorrect, data on pxr.dk is not looking correct ;-)
 */
@Deprecated
@Dbc("ChrSpecialization.dbc")
public class CharacterSpecialization {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String backgroundFile;
    @DbcField(column = 2, dataType = DbcDataType.INT32)
    private int classId;
    @DbcField(column = 3, dataType = DbcDataType.INT32)
    private int masterySpellId;
    @DbcField(column = 5, dataType = DbcDataType.INT32)
    private int orderIndex;
    @DbcField(column = 6, dataType = DbcDataType.INT32)
    private int petTalentType;
    @DbcField(column = 7, dataType = DbcDataType.INT32)
    private int role;
    @DbcField(column = 8, dataType = DbcDataType.INT32)
    private int spellIconId;
    @DbcField(column = 9, dataType = DbcDataType.INT32)
    private int raidBuffs;
    @DbcField(column = 10, dataType = DbcDataType.INT32)
    private int flags;
    @DbcField(column = 11, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(column = 12, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(column = 13, dataType = DbcDataType.INT32)
    private int maxBuffs;
    @DbcField(column = 14, dataType = DbcDataType.INT32)
    private int primaryStat;
    @DbcField(column = 15, dataType = DbcDataType.INT32)
    private int secondaryStat;
    @DbcField(column = 16, dataType = DbcDataType.INT32)
    private int tertiaryStat;

    public CharacterSpecialization() {
    }

    public CharacterSpecialization(int id, String backgroundFile, int classId, int masterySpellId, int orderIndex, int petTalentType, int role, int spellIconId, int raidBuffs, int flags, String name, String description, int maxBuffs, int primaryStat, int secondaryStat, int tertiaryStat) {
        this.id = id;
        this.backgroundFile = backgroundFile;
        this.classId = classId;
        this.masterySpellId = masterySpellId;
        this.orderIndex = orderIndex;
        this.petTalentType = petTalentType;
        this.role = role;
        this.spellIconId = spellIconId;
        this.raidBuffs = raidBuffs;
        this.flags = flags;
        this.name = name;
        this.description = description;
        this.maxBuffs = maxBuffs;
        this.primaryStat = primaryStat;
        this.secondaryStat = secondaryStat;
        this.tertiaryStat = tertiaryStat;
    }

    public int getId() {
        return id;
    }

    public String getBackgroundFile() {
        return backgroundFile;
    }

    public int getClassId() {
        return classId;
    }

    public int getMasterySpellId() {
        return masterySpellId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public int getPetTalentType() {
        return petTalentType;
    }

    public int getRole() {
        return role;
    }

    public int getSpellIconId() {
        return spellIconId;
    }

    public int getRaidBuffs() {
        return raidBuffs;
    }

    public int getFlags() {
        return flags;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxBuffs() {
        return maxBuffs;
    }

    public int getPrimaryStat() {
        return primaryStat;
    }

    public int getSecondaryStat() {
        return secondaryStat;
    }

    public int getTertiaryStat() {
        return tertiaryStat;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
