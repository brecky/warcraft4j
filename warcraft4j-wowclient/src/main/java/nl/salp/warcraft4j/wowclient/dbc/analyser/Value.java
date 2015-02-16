package nl.salp.warcraft4j.wowclient.dbc.analyser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * TODO Document class.
 */
public class Value {
    public static final int EMPTY_VALUE = Integer.MIN_VALUE;
    public static final Value EMPTY = new Value(EMPTY_VALUE);
    private final int value;
    private final int hashCode;
    private final byte[] data;

    public Value(int value) {
        this.value = value;
        hashCode = String.valueOf(value).hashCode();
        data = toByteArray();
    }

    private byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        buffer.rewind();
        byte[] data = new byte[4];
        buffer.get(data);
        return data;
    }

    private ByteBuffer data() {
        return ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
    }

    public String asString() {
        return new String(data, StandardCharsets.UTF_8);
    }

    public float asFloat() {
        return data().getFloat();
    }

    public byte[] asByteArray() {
        byte[] data = new byte[4];
        data().get(data);
        return data;
    }

    public short[] asShortArray() {
        short[] data = new short[2];
        ByteBuffer buffer = data();
        data[0] = buffer.getShort();
        data[1] = buffer.getShort();
        return data;
    }

    public char[] asCharArray() {
        char[] data = new char[2];
        ByteBuffer buffer = data();
        data[0] = buffer.getChar();
        data[1] = buffer.getChar();
        return data;
    }


    public int get() {
        return value;
    }

    public boolean isEmpty() {
        return value != EMPTY_VALUE;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj != null && Value.class.isAssignableFrom(obj.getClass())) {
            eq = get() == ((Value) obj).get();
        }
        return eq;
    }

    @Override
    public String toString() {
        return isEmpty() ? "null" : String.valueOf(value);
    }
}
