package nl.salp.warcraft4j.clientdata.util.io;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link DataTypeUtil}.
 *
 * @author Barre Dijkstra
 */
public class DataTypeUtilTest {
    /** Variable size 16-bit character set. */
    private static final Charset CHARSET_16BIT_VAR = StandardCharsets.UTF_16;
    /** Variable size 8-bit character set. */
    private static final Charset CHARSET_8BIT_VAR = StandardCharsets.UTF_8;
    /** Fixed size 8-bit character set. */
    private static final Charset CHARSET_8BIT_FIXED = StandardCharsets.US_ASCII;

    @Test
    public void shouldGetAverageSizeForFixed8bitCharset() throws Exception {
        assertEquals("Incorrect average bytes per character for fixed size (8-bit) character set.", 1, DataTypeUtil.getAverageBytesPerCharacter(CHARSET_8BIT_FIXED));
    }

    @Test
    public void shouldGetAverageSizeForVariable8bitCharset() throws Exception {
        assertEquals("Incorrect average bytes per character for variable size (8-bit) character set.", 1, DataTypeUtil.getAverageBytesPerCharacter(CHARSET_8BIT_VAR));
    }


    @Test
    public void shouldGetAverageSizeForVariable16bitCharset() throws Exception {
        assertEquals("Incorrect average bytes per character for variable size (16-bit) character set.", 2, DataTypeUtil.getAverageBytesPerCharacter(CHARSET_16BIT_VAR));
    }

    @Test
    public void shouldReportZeroAverageSizeForNullCharset() throws Exception {
        assertEquals("Average bytes per character reported for null character set.", 0, DataTypeUtil.getAverageBytesPerCharacter(null));
    }


    @Test
    public void shouldGetMaximumSizeForFixed8bitCharset() throws Exception {
        assertEquals("Incorrect maximum bytes per character for fixed size (8-bit) character set.", 1, DataTypeUtil.getMaximumBytesPerCharacter(CHARSET_8BIT_FIXED));
    }

    @Test
    public void shouldGetMaximumSizeForVariable8bitCharset() throws Exception {
        // Note: .... 1F000 - 1F09F... Mahjong Tiles..... 1F600 - 1F64F... Emoji..... let's just say that they don't exist and UTF-8 = <= 3 byte for most implementations ;-)
        int maximumBytesPerCharacter = DataTypeUtil.getMaximumBytesPerCharacter(CHARSET_8BIT_VAR);
        assertTrue("Incorrect maximum bytes per character for variable size (8-bit) character set.", (maximumBytesPerCharacter == 3 || maximumBytesPerCharacter == 4));
    }


    @Test
    public void shouldGetMaximumSizeForVariable16bitCharset() throws Exception {
        assertEquals("Incorrect maximum bytes per character for variable size (16-bit) character set.", 4, DataTypeUtil.getMaximumBytesPerCharacter(CHARSET_16BIT_VAR));
    }

    @Test
    public void shouldReportZeroMaximumSizeForNullCharset() throws Exception {
        assertEquals("Maximum bytes per character reported for null character set.", 0, DataTypeUtil.getMaximumBytesPerCharacter(null));
    }

}
