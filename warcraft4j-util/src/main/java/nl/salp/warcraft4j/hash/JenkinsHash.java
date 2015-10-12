package nl.salp.warcraft4j.hash;
/**
 * @(#) JenkinsHash.java 2011-08-18
 */

import nl.salp.warcraft4j.util.DataTypeUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This is an implementation of Bob Jenkins' hash. It can produce both 32-bit
 * and 64-bit hash values.
 * <p>
 * Generates same hash values as the <a
 * href="http://www.burtleburtle.net/bob/hash/doobs.html">original
 * implementation written by Bob Jenkins</a>.
 *
 * @author $Author: vijaykandy $
 * @version $Revision: $
 */
public class JenkinsHash {
    public static final long HASH_EMPTY_VALUE_HASHLITTLE2 = 0xDEADBEEF_DEADBEEFL;
    public static final int HASH_EMPTY_VALUE_HASHLITTLE = 0xDEADBEEF;

    /**
     * Default constructor.
     */
    public JenkinsHash() {
    }


    /**
     * Returns a 32-bit hash value.
     *
     * @return 32-bit hash value
     */
    public static int hashLittle(byte[] input, int length) {
        int pc = 0;
        int pb = 0;

        return (int) hash(input, length, pc, pb, true);
    }

    /**
     * Returns a 32-bit hash value.
     *
     * @return 32-bit hash value
     */
    public static int hashLittle(byte[] input, int length, int pc) {
        return (int) hash(input, length, pc, 0, true);
    }

    /**
     * Returns a 64-bit hash value.
     *
     * @return 64-bit hash value
     */
    public static long hashLittle2(byte[] input, int length) {
        int pc = 0;
        int pb = 0;

        return hash(input, length, pc, pb, false);
    }

    /**
     * Returns a 64-bit hash value.
     *
     * @return 64-bit hash value
     */
    public static long hashLittle2(byte[] input, int length, int pc, int pb) {
        return hash(input, length, pc, pb, false);
    }

    public static int hashLittle2a(byte[] input, int length, int pc, int pb) {
        return (int) hashLittle2(input, length, pc, pb);
    }

    public static int hashLittle2a(byte[] input, int length) {
        return (int) hashLittle2(input, length);
    }

    public static byte[] hashLittle2a(byte[] data) {
        byte[] hash = hashLittle2(data);
        return Arrays.copyOfRange(hash, hash.length / 2, hash.length);
    }

    public static int hashLittle2b(byte[] input, int length, int pc, int pb) {
        return (int) (hashLittle2(input, length, pc, pb) >>> 32);
    }

    public static int hashLittle2b(byte[] input, int length) {
        return (int) (hashLittle2(input, length) >>> 32);
    }

    public static byte[] hashLittle2b(byte[] data) {
        byte[] hash = hashLittle2(data);
        return Arrays.copyOfRange(hash, 0, hash.length / 2);
    }

    public static byte[] hashLittle2(byte[] data) {
        return DataTypeUtil.toByteArray(hashLittle2(data, data.length));
    }

    public static byte[] hashLittle2File(String filename) {
        byte[] data = filename.replace('/', '\\').toUpperCase().getBytes(StandardCharsets.US_ASCII);
        return DataTypeUtil.toByteArray(hashLittle2(data, data.length));
    }

    public static byte[] hashLittle2FileA(String filename) {
        byte[] hash = hashLittle2File(filename);
        return Arrays.copyOfRange(hash, hash.length / 2, hash.length);
    }

    public static byte[] hashLittle2FileB(String filename) {
        byte[] hash = hashLittle2File(filename);
        return Arrays.copyOfRange(hash, 0, hash.length / 2);
    }

    /**
     * Hash algorithm.
     *
     * @param k           message on which hash is computed
     * @param length      message size
     * @param pc          primary init value
     * @param pb          secondary init value
     * @param is32BitHash true if just 32-bit hash is expected.
     *
     * @return
     */
    private static long hash(byte[] k, int length, int pc, int pb, boolean is32BitHash) {
        int a, b, c;

        a = b = c = 0xdeadbeef + length + pc;
        c += pb;

        int offset = 0;
        while (length > 12) {
            a += k[offset + 0];
            a += k[offset + 1] << 8;
            a += k[offset + 2] << 16;
            a += k[offset + 3] << 24;
            b += k[offset + 4];
            b += k[offset + 5] << 8;
            b += k[offset + 6] << 16;
            b += k[offset + 7] << 24;
            c += k[offset + 8];
            c += k[offset + 9] << 8;
            c += k[offset + 10] << 16;
            c += k[offset + 11] << 24;

            // mix(a, b, c);
            a -= c;
            a ^= rot(c, 4);
            c += b;
            b -= a;
            b ^= rot(a, 6);
            a += c;
            c -= b;
            c ^= rot(b, 8);
            b += a;
            a -= c;
            a ^= rot(c, 16);
            c += b;
            b -= a;
            b ^= rot(a, 19);
            a += c;
            c -= b;
            c ^= rot(b, 4);
            b += a;

            length -= 12;
            offset += 12;
        }

        switch (length) {
            case 12:
                c += k[offset + 11] << 24;
            case 11:
                c += k[offset + 10] << 16;
            case 10:
                c += k[offset + 9] << 8;
            case 9:
                c += k[offset + 8];
            case 8:
                b += k[offset + 7] << 24;
            case 7:
                b += k[offset + 6] << 16;
            case 6:
                b += k[offset + 5] << 8;
            case 5:
                b += k[offset + 4];
            case 4:
                a += k[offset + 3] << 24;
            case 3:
                a += k[offset + 2] << 16;
            case 2:
                a += k[offset + 1] << 8;
            case 1:
                a += k[offset + 0];
                break;
            case 0:
                return is32BitHash ? c :((((long) c) << 32) | ((long) b & 0xFFFFFFFFL));
        }

        // Final mixing of thrree 32-bit values in to c
        c ^= b;
        c -= rot(b, 14);
        a ^= c;
        a -= rot(c, 11);
        b ^= a;
        b -= rot(a, 25);
        c ^= b;
        c -= rot(b, 16);
        a ^= c;
        a -= rot(c, 4);
        b ^= a;
        b -= rot(a, 14);
        c ^= b;
        c -= rot(b, 24);

        return is32BitHash ? c :((((long) c) << 32) | ((long) b & 0xFFFFFFFFL));
    }

    private static long rot(int x, int distance) {
        return (x << distance) | (x >>> (32 - distance));
        // return (x << distance) | (x >>> -distance);
    }
}