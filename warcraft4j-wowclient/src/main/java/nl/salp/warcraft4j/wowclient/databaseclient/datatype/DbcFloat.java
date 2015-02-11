package nl.salp.warcraft4j.wowclient.databaseclient.datatype;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcFloat extends BaseDbcDataType<Float> {
    @Override
    public String getName() {
        return "Float";
    }

    @Override
    public String getDbcName() {
        return "float";
    }

    @Override
    public int getByteLength() {
        return 4;
    }

    @Override
    public Class<Float> getJavaClass() {
        return Float.class;
    }

    @Override
    protected void addToByteBuffer(Float value, ByteBuffer byteBuffer) {
        byteBuffer.putFloat(value);
    }

    @Override
    protected Float readFromByteBuffer(ByteBuffer byteBuffer) {
        return byteBuffer.getFloat();
    }

    @Override
    protected void setFieldValue(Field field, Object instance, Float value) throws IllegalAccessException {
        field.setFloat(instance, value);
    }

}
