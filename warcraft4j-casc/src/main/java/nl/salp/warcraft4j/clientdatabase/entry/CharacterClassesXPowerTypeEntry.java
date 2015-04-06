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
 * <p/>
 * <pre>Field Descriptions
 * <p/>
 * The meaning of some of the fields change depending on the GeneralType value.
 * /	0 - Base Skin	1 - Face	2 - Facial Hair	3 - Hair	4 - Underwear
 * Type	-	FaceType	FacialHairType	HairStyle	-
 * Color	SkinColor	SkinColor	HairColor	HairColor	SkinColor
 * Texture1	SkinTexture	FaceLowerTexture	FacialLowerTexture	HairTexture	PelvisTexture
 * Texture2	ExtraSkinTexture	FaceUpperTexture	FacialUpperTexture	ScalpLowerTexture	TorsoTexture
 * Texture3	-	-	-	ScalpUpperTexture	-
 * Flags
 * <p/>
 * Flag	Description
 * 0x1	Playable (probably)
 * 0x2
 * 0x4	Death Knight texture
 * 0x8	NPC skin (e.g. the necromancer human skins with facial marking etc)
 * 0x10</pre>
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "ChrClassesXPowerTypes.dbc")
public class CharacterClassesXPowerTypeEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.CHARACTER_CLASSES_X_POWER_TYPE;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int classId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int powerType;


    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getClassId() {
        return classId;
    }

    public int getPowerType() {
        return powerType;
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