/*
 * X2JME By Stan Hebben - x2jme.sourceforge.net
 *
 * This file may be used and modified without restriction, as long a proper
 * credit is given to the original author.
 *
 * The software is provided as-is, and the author cannot be held responsible for
 * any damage, directly or indirectly, from the use of this software.
 */

package be.stanhebben.x2jme.binary;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * DataInputStream variant which can be used to read files in little-endian format.
 *
 * @author Stan Hebben
 */
public final class LEDataInputStream extends InputStream {
    private InputStream input;

    /**
     * Constructs a new LEDataInputStream.
     *
     * @param input inputstream
     */
    public LEDataInputStream(InputStream input) {
        this.input = input;
    }

    /**
     * Reads a short.
     *
     * @return short value
     * @throws IOException if the short could not be read
     */
    public short readShort() throws IOException {
        int i1 = read();
        int i2 = read();
        if (i1 < 0 || i2 < 0) throw new EOFException();

        return (short)((i1 & 0xFF) | ((i2 & 0xFF) << 8));
    }

    /**
     * Reads an int.
     *
     * @return int value
     * @throws IOException if the int could not be read
     */
    public int readInt() throws IOException {
        int i1 = read();
        int i2 = read();
        int i3 = read();
        int i4 = read();

        if (i1 < 0 || i2 < 0 || i3 < 0 || i4 < 0) throw new EOFException();

        return (i1 & 0xFF) | ((i2 & 0xFF) << 8) | ((i3 & 0xFF) << 16) | ((i4 & 0xFF) << 24);
    }

    /**
     * Reads a long.
     *
     * @return long value
     * @throws IOException if the long could not be read
     */
    public long readLong() throws IOException {
        int i1 = readInt();
        int i2 = readInt();

        return ((long)i1 & 0xFFFFFFFFL) + ((long)i2 << 32L);
    }

    /**
     * Reads a data array completely.
     *
     * @param data data array
     * @throws IOException if the data could not be read
     */
    public void readFully(byte[] data) throws IOException {
        for (int i = 0; i < data.length; i++) {
            int value = read();
            if (value < 0) throw new EOFException();
            data[i] = (byte)value;
        }
    }

    /**
     * Reads a float.
     *
     * @return float value
     * @throws IOException if the float could not be read properly
     */
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads a double.
     *
     * @return double value
     * @throws IOException if the double could not be read properly
     */
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    ///////////////////////////////
    // InputStream implementation
    ///////////////////////////////

    public int read() throws IOException {
        return input.read();
    }

    @Override
    public void close() throws IOException {
        if (input != null) {
            input.close();
            input = null;
        }
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }
}
