package nl.salp.warcraft4j.wowclient.databaseclient.datatype;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcBoolean extends BaseDbcDataType<Boolean> {
    private static final byte BYTE_TRUE = 1;
    private static final byte BYTE_FALSE = 0;

    private byte byteValue(boolean value) {
        return value ? BYTE_TRUE : BYTE_FALSE;
    }

    private boolean booleanValue(byte value) {
        boolean bool;
        if (BYTE_TRUE == value) {
            bool = true;
        } else if (BYTE_FALSE == value) {
            bool = false;
        } else {
            throw new IllegalArgumentException(format("Received an unknown boolean byte value %d", value));
        }
        return bool;
    }

    @Override
    protected void addToByteBuffer(Boolean value, ByteBuffer byteBuffer) {
        byteBuffer.put(byteValue(value));
    }

    @Override
    protected Boolean readFromByteBuffer(ByteBuffer byteBuffer) {
        return booleanValue(byteBuffer.get());
    }

    @Override
    protected void setFieldValue(Field field, Object instance, Boolean value) throws IllegalAccessException {
        field.setBoolean(instance, value);
    }

    @Override
    public String getName() {
        return "Boolean";
    }

    @Override
    public String getDbcName() {
        return "Boolean";
    }

    @Override
    public int getByteLength() {
        return 1;
    }

    @Override
    public Class<Boolean> getJavaClass() {
        return Boolean.class;
    }
}
