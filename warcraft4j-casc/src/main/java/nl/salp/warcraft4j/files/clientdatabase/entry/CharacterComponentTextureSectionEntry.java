package nl.salp.warcraft4j.files.clientdatabase.entry;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcFile;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "CharComponentTextureSections.dbc")
public class CharacterComponentTextureSectionEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.CHARACTER_COMPONENT_TEXTURE_SECTION;
    /*
    TEXTURE_SECTION_ARMS_UPPER    = 0,
    TEXTURE_SECTION_ARMS_LOWER,
    TEXTURE_SECTION_HANDS,
    TEXTURE_SECTION_TORSO_UPPER,
    TEXTURE_SECTION_TORSO_LOWER,
    TEXTURE_SECTION_LEGS_UPPER,
    TEXTURE_SECTION_LEGS_LOWER,
    TEXTURE_SECTION_FEET,
    TEXTURE_SECTION_UNK8,                  // Only used in Layout 2 (1024x512)
    TEXTURE_SECTION_SCALP_UPPER,
    TEXTURE_SECTION_SCALP_LOWER
     */

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = ClientDatabaseEntryType.CHARACTER_COMPONENT_TEXTURE_LAYOUT)
    private int characterComponentTextureLayoutId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int sectionType;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int x;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int y;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int width;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int height;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCharacterComponentTextureLayoutId() {
        return characterComponentTextureLayoutId;
    }

    public int getSectionType() {
        return sectionType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
