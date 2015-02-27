package nl.salp.warcraft4j.wowclient.io;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DataTypeUtil}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.wowclient.io.DataTypeUtil
 */
public class DataTypeUtilTest {

    /**
     * Ensure that the average bytes per character are {@code 0} for an undefined ({@code null}) charset.
     *
     * @see nl.salp.warcraft4j.wowclient.io.DataTypeUtil#getAverageBytesPerCharacter(java.nio.charset.Charset)
     */
    @Test
    public void shouldDetermineAverageBytesPerCharacterForNullCharset() {
        assertEquals("Reported bytes per character for a null charset.", 0, DataTypeUtil.getAverageBytesPerCharacter(null));
    }

    /**
     * Ensure that the average bytes per character are determined for a single-byte charset (US_ASCII).
     * <p>
     * The average bits of a single US_ASCII character is 7, with possible extensions to 8-bit, resulting in 1-Byte average.
     *
     * @see nl.salp.warcraft4j.wowclient.io.DataTypeUtil#getAverageBytesPerCharacter(java.nio.charset.Charset)
     */
    @Test
    public void shouldDetermineAverageBytesPerCharacterForFixedSingleByteCharset() {
        assertEquals("Reported wrong maximum bytes per character for a fixed single byte charset.", 1, DataTypeUtil.getAverageBytesPerCharacter(StandardCharsets.US_ASCII));
    }

    /**
     * Ensure that the average bytes per character are determined for a variable single-byte charset (UTF-8).
     * <p>
     * UTF-8 uses 8-bit per code point with the non-extended (average) set using a single code point, resulting in a 1-Byte average.
     *
     * @see nl.salp.warcraft4j.wowclient.io.DataTypeUtil#getAverageBytesPerCharacter(java.nio.charset.Charset)
     */
    @Test
    public void shouldDetermineAverageBytesPerCharacterForVariableSingleByteCharset() {
        assertEquals("Reported wrong average bytes per character for a variable single byte charset.", 1, DataTypeUtil.getAverageBytesPerCharacter(StandardCharsets.UTF_8));
    }

    /**
     * Ensure that the maximum bytes per character are determined for a variable single-byte charset (UTF-8).
     * <p>
     * UTF-8 uses 8-bit per code point with a maximum of 3 code points, resulting in a 3-Byte maximum.
     *
     * @see nl.salp.warcraft4j.wowclient.io.DataTypeUtil#getMaximumBytesPerCharacter(java.nio.charset.Charset)
     */
    @Test
    public void shouldDetermineMaxBytesPerCharacterForVariableSingleByteCharset() {
        assertEquals("Reported wrong maximum bytes per character for a variable single-byte charset.", 3, DataTypeUtil.getMaximumBytesPerCharacter(StandardCharsets.UTF_8));
    }

    /**
     * Ensure that the average bytes per character are determined for a variable multi-byte charset (UTF-16).
     * <p>
     * UTF-16 uses 16-bit per code point with the non-extended (average) set using a single code point, resulting in a 2-Byte average.
     *
     * @see nl.salp.warcraft4j.wowclient.io.DataTypeUtil#getAverageBytesPerCharacter(java.nio.charset.Charset)
     */
    @Test
    public void shouldDetermineAverageBytesPerCharacterForVariableMultiByteCharset() {
        assertEquals("Reported wrong average bytes per character for a variable multi-byte charset.", 2, DataTypeUtil.getAverageBytesPerCharacter(StandardCharsets.UTF_16));
    }

    /**
     * Ensure that the maximum bytes per character are determined for a variable multi-byte charset (UTF-16).
     * <p>
     * UTF-16 uses 16-bit per code point with a maximum of 2 code points, resulting in a 4-Byte maximum.
     *
     * @see nl.salp.warcraft4j.wowclient.io.DataTypeUtil#getMaximumBytesPerCharacter(java.nio.charset.Charset)
     */
    @Test
    public void shouldDetermineMaxBytesPerCharacterForVariableMultiByteCharset() {
        assertEquals("Reported wrong maximum bytes per character for a variable multi-byte charset.", 4, DataTypeUtil.getMaximumBytesPerCharacter(StandardCharsets.UTF_16));
    }
}
