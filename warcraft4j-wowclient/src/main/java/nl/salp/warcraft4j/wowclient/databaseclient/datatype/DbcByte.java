package nl.salp.warcraft4j.wowclient.databaseclient.datatype;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcByte extends BaseDbcDataType<Byte> {
    @Override
    protected void addToByteBuffer(Byte value, ByteBuffer byteBuffer) {
        byteBuffer.put(value);
    }

    @Override
    protected Byte readFromByteBuffer(ByteBuffer byteBuffer) {
        return byteBuffer.get();
    }

    @Override
    protected void setFieldValue(Field field, Object instance, Byte value) throws IllegalAccessException {
        field.setByte(instance, value);
    }

    @Override
    public String getName() {
        return "Byte";
    }

    @Override
    public String getDbcName() {
        return "Byte";
    }

    @Override
    public int getByteLength() {
        return 1;
    }

    @Override
    public Class<Byte> getJavaClass() {
        return Byte.class;
    }
}
