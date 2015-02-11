package nl.salp.warcraft4j.wowclient.databaseclient.datatype;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcInt32 extends BaseDbcDataType<Integer> {
    @Override
    public String getName() {
        return "Integer";
    }

    @Override
    public String getDbcName() {
        return "int32";
    }

    @Override
    public int getByteLength() {
        return 4;
    }

    @Override
    public Class<Integer> getJavaClass() {
        return Integer.class;
    }

    @Override
    protected void addToByteBuffer(Integer value, ByteBuffer byteBuffer) {
        byteBuffer.putInt(value);
    }

    @Override
    protected Integer readFromByteBuffer(ByteBuffer byteBuffer) {
        return byteBuffer.getInt();
    }

    @Override
    protected void setFieldValue(Field field, Object instance, Integer value) throws IllegalAccessException {
        field.setInt(instance, value);
    }
}
