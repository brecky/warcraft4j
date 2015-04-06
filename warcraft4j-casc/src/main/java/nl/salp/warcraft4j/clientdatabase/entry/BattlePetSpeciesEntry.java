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
@DbcFile(file = "BattlePetSpecies.db2")
public class BattlePetSpeciesEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.BATTLE_PET_SPECIES;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int creatureId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int iconId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int spellId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int petFamilyId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknownField1;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 8, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String source;
    @DbcField(order = 9, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getPetFamilyId() {
        return petFamilyId;
    }

    public int getUnknownField1() {
        return unknownField1;
    }

    public int getFlags() {
        return flags;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
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
