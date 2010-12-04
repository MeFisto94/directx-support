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

import java.io.IOException;

/**
 * Represents a GUID token in binary mode.
 *
 * @author Stan Hebben
 */
public class XBinaryTokenGuid extends XBinaryToken {
    private int data1;
    private int data2;
    private int data3;
    private byte[] data4;

    /**
     * Constructs a new binary GUID token.
     *
     * @param in input to read the token from
     * @throws IOException if the token data could not be read
     */
    public XBinaryTokenGuid(LEDataInputStream in) throws IOException {
        data1 = in.readInt();
        data2 = in.readShort();
        data3 = in.readShort();
        data4 = new byte[8];
        in.readFully(data4);
    }

    ////////////////////////////////
    // XBinaryToken implementation
    ////////////////////////////////
    
    public int getType() {
        return GUID;
    }

    @Override
    public XBinaryTokenGuid toTokenGuid() {
        return this;
    }

    @Override
    public String toString() {
        return "<" + hex(data1, 8) + "-" + hex(data2, 4) + "-" + hex(data3, 4) + "-"
                + hex(data4[0], 2) + hex(data4[1], 2) + hex(data4[2], 2) + hex(data4[3], 2)
                + hex(data4[4], 2) + hex(data4[6], 2) + hex(data4[6], 2) + hex(data4[7], 2) + ">";
    }

    ////////////////////
    // Private methods
    ////////////////////

    /**
     * Converts an integer to a string with a fixed length. Zeroes are added if
     * necessary.
     *
     * @param value integer value
     * @param digits number of digits
     * @return converted integer
     */
    private String hex(int value, int digits) {
        String result = Long.toHexString(value & 0xFFFFFFFFL);
        while (result.length() < digits) result = "0" + result;
        return result;
    }
}
