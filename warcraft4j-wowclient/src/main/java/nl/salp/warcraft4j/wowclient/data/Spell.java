package nl.salp.warcraft4j.wowclient.data;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Dbc("Spell.dbc")
public class Spell {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(column = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String nameSubtext;
    @DbcField(column = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(column = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String auraDescription;
    @DbcField(column = 5, dataType = DbcDataType.INT32)
    private int runeCostId;
    @DbcField(column = 6, dataType = DbcDataType.INT32)
    private int spellMissileId;
    @DbcField(column = 7, dataType = DbcDataType.INT32)
    private int descriptionVariablesId;
    @DbcField(column = 8, dataType = DbcDataType.INT32)
    private int scalingId;
    @DbcField(column = 9, dataType = DbcDataType.INT32)
    private int auraOptionsId;
    @DbcField(column = 10, dataType = DbcDataType.INT32)
    private int auraRestrictionsId;
    @DbcField(column = 11, dataType = DbcDataType.INT32)
    private int castingRequirementsId;
    @DbcField(column = 12, dataType = DbcDataType.INT32)
    private int categoriesId;
    @DbcField(column = 13, dataType = DbcDataType.INT32)
    private int classOptionsId;
    @DbcField(column = 14, dataType = DbcDataType.INT32)
    private int cooldownsId;
    @DbcField(column = 15, dataType = DbcDataType.INT32)
    private int equippedItemsId;
    @DbcField(column = 16, dataType = DbcDataType.INT32)
    private int interruptsId;
    @DbcField(column = 17, dataType = DbcDataType.INT32)
    private int levelsId;
    @DbcField(column = 18, dataType = DbcDataType.INT32)
    private int reagentsId;
    @DbcField(column = 19, dataType = DbcDataType.INT32)
    private int shapeshiftId;
    @DbcField(column = 20, dataType = DbcDataType.INT32)
    private int targetRestrictionsId;
    @DbcField(column = 21, dataType = DbcDataType.INT32)
    private int totemsId;
    @DbcField(column = 22, dataType = DbcDataType.INT32)
    private int requiredProjectId;
    @DbcField(column = 23, dataType = DbcDataType.INT32)
    private int miscId;

    public Spell() {
    }

    public Spell(int id, String name, String nameSubtext, String description, String auraDescription, int runeCostId, int spellMissileId, int descriptionVariablesId, int scalingId, int auraOptionsId, int auraRestrictionsId, int castingRequirementsId, int categoriesId, int classOptionsId, int cooldownsId, int equippedItemsId, int interruptsId, int levelsId, int reagentsId, int shapeshiftId, int targetRestrictionsId, int totemsId, int requiredProjectId, int miscId) {
        this.id = id;
        this.name = name;
        this.nameSubtext = nameSubtext;
        this.description = description;
        this.auraDescription = auraDescription;
        this.runeCostId = runeCostId;
        this.spellMissileId = spellMissileId;
        this.descriptionVariablesId = descriptionVariablesId;
        this.scalingId = scalingId;
        this.auraOptionsId = auraOptionsId;
        this.auraRestrictionsId = auraRestrictionsId;
        this.castingRequirementsId = castingRequirementsId;
        this.categoriesId = categoriesId;
        this.classOptionsId = classOptionsId;
        this.cooldownsId = cooldownsId;
        this.equippedItemsId = equippedItemsId;
        this.interruptsId = interruptsId;
        this.levelsId = levelsId;
        this.reagentsId = reagentsId;
        this.shapeshiftId = shapeshiftId;
        this.targetRestrictionsId = targetRestrictionsId;
        this.totemsId = totemsId;
        this.requiredProjectId = requiredProjectId;
        this.miscId = miscId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameSubtext() {
        return nameSubtext;
    }

    public String getDescription() {
        return description;
    }

    public String getAuraDescription() {
        return auraDescription;
    }

    public int getRuneCostId() {
        return runeCostId;
    }

    public int getSpellMissileId() {
        return spellMissileId;
    }

    public int getDescriptionVariablesId() {
        return descriptionVariablesId;
    }

    public int getScalingId() {
        return scalingId;
    }

    public int getAuraOptionsId() {
        return auraOptionsId;
    }

    public int getAuraRestrictionsId() {
        return auraRestrictionsId;
    }

    public int getCastingRequirementsId() {
        return castingRequirementsId;
    }

    public int getCategoriesId() {
        return categoriesId;
    }

    public int getClassOptionsId() {
        return classOptionsId;
    }

    public int getCooldownsId() {
        return cooldownsId;
    }

    public int getEquippedItemsId() {
        return equippedItemsId;
    }

    public int getInterruptsId() {
        return interruptsId;
    }

    public int getLevelsId() {
        return levelsId;
    }

    public int getReagentsId() {
        return reagentsId;
    }

    public int getShapeshiftId() {
        return shapeshiftId;
    }

    public int getTargetRestrictionsId() {
        return targetRestrictionsId;
    }

    public int getTotemsId() {
        return totemsId;
    }

    public int getRequiredProjectId() {
        return requiredProjectId;
    }

    public int getMiscId() {
        return miscId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
