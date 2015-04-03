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
@DbcFile(file = "ChrRaces.dbc")
public class CharacterRaceEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.CHARACTER_RACE;
    // TODO Fix fields.
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int factionId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int explorationSoundId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int maleDisplayId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int femaleDisplayId;
    @DbcField(order = 7, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String clientPrefix;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int baseLanguage;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int creatureType;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int resSicknessSpellId;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int splashSoundId;
    @DbcField(order = 12, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String clientFileString;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int cinematicSequenceId;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int alliance;
    @DbcField(order = 15, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 16, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String nameFemale;
    @DbcField(order = 17, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String nameMale;
    @DbcField(order = 18, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String facialHairCustomizationMale;
    @DbcField(order = 19, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String facialHairCustomizationFemale;
    @DbcField(order = 20, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String hairCustomization;
    @DbcField(order = 21, dataType = DbcDataType.UINT32)
    private int raceRelated;
    @DbcField(order = 22, dataType = DbcDataType.UINT32)
    private int unalteredVisualRaceId;
    @DbcField(order = 23, dataType = DbcDataType.UINT32)
    private int unalteredMaleCreatureSoundDataId;
    @DbcField(order = 24, dataType = DbcDataType.UINT32)
    private int unalteredFemaleCreatureSoundDataId;
    @DbcField(order = 25, dataType = DbcDataType.UINT32)
    private int charComponentTextureLayoutId;
    @DbcField(order = 26, dataType = DbcDataType.UINT32)
    private int defaultClassID;
    @DbcField(order = 27, dataType = DbcDataType.UINT32)
    private int createScreenFileDataId;
    @DbcField(order = 28, dataType = DbcDataType.UINT32)
    private int selectScreenFileDataId;
    @DbcField(order = 29, dataType = DbcDataType.FLOAT, numberOfEntries = 3)
    private float[] maleCustomizeOffset;
    @DbcField(order = 30, dataType = DbcDataType.FLOAT, numberOfEntries = 3)
    private float[] femaleCustomizeOffset;
    @DbcField(order = 31, dataType = DbcDataType.UINT32)
    private int neutralRaceId;
    @DbcField(order = 32, dataType = DbcDataType.UINT32)
    private int lowResScreenFileDataId;
    @DbcField(order = 33, dataType = DbcDataType.UINT32)
    private int highResMaleDisplayId;
    @DbcField(order = 34, dataType = DbcDataType.UINT32)
    private int highResFemaleDisplayId;
    @DbcField(order = 35, dataType = DbcDataType.UINT32)
    private int charComponentTexLayoutHiResId;
    @DbcField(order = 36, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getFlags() {
        return flags;
    }

    public int getFactionId() {
        return factionId;
    }

    public int getExplorationSoundId() {
        return explorationSoundId;
    }

    public int getMaleDisplayId() {
        return maleDisplayId;
    }

    public int getFemaleDisplayId() {
        return femaleDisplayId;
    }

    public String getClientPrefix() {
        return clientPrefix;
    }

    public int getBaseLanguage() {
        return baseLanguage;
    }

    public int getCreatureType() {
        return creatureType;
    }

    public int getResSicknessSpellId() {
        return resSicknessSpellId;
    }

    public int getSplashSoundId() {
        return splashSoundId;
    }

    public String getClientFileString() {
        return clientFileString;
    }

    public int getCinematicSequenceId() {
        return cinematicSequenceId;
    }

    public int getAlliance() {
        return alliance;
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

    public String getFacialHairCustomizationMale() {
        return facialHairCustomizationMale;
    }

    public String getFacialHairCustomizationFemale() {
        return facialHairCustomizationFemale;
    }

    public String getHairCustomization() {
        return hairCustomization;
    }

    public int getRaceRelated() {
        return raceRelated;
    }

    public int getUnalteredVisualRaceId() {
        return unalteredVisualRaceId;
    }

    public int getUnalteredMaleCreatureSoundDataId() {
        return unalteredMaleCreatureSoundDataId;
    }

    public int getUnalteredFemaleCreatureSoundDataId() {
        return unalteredFemaleCreatureSoundDataId;
    }

    public int getCharComponentTextureLayoutId() {
        return charComponentTextureLayoutId;
    }

    public int getDefaultClassID() {
        return defaultClassID;
    }

    public int getCreateScreenFileDataId() {
        return createScreenFileDataId;
    }

    public int getSelectScreenFileDataId() {
        return selectScreenFileDataId;
    }

    public float[] getMaleCustomizeOffset() {
        return maleCustomizeOffset;
    }

    public float[] getFemaleCustomizeOffset() {
        return femaleCustomizeOffset;
    }

    public int getNeutralRaceId() {
        return neutralRaceId;
    }

    public int getLowResScreenFileDataId() {
        return lowResScreenFileDataId;
    }

    public int getHighResMaleDisplayId() {
        return highResMaleDisplayId;
    }

    public int getHighResFemaleDisplayId() {
        return highResFemaleDisplayId;
    }

    public int getCharComponentTexLayoutHiResId() {
        return charComponentTexLayoutHiResId;
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