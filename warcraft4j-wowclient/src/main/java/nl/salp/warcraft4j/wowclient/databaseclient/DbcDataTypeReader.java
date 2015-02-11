package nl.salp.warcraft4j.wowclient.databaseclient;

import java.nio.ByteBuffer;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public interface DbcDataTypeReader<T> {
    T read(ByteBuffer buffer);
}
