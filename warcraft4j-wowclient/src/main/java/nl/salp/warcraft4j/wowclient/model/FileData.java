package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.dbc.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 */
@Dbc("FileData.dbc")
public class FileData {
    @DbcField(column = 0, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(column = 1, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String fileName;
    @DbcField(column = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String filePath;

    public FileData() {
    }

    public FileData(int id, String fileName, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
