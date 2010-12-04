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

/**
 * Represents an int token in binary mode. Int arrays are converted into multiple
 * int tokens.
 *
 * @author Stan Hebben
 */
public class XBinaryTokenInt extends XBinaryToken {
    private int value;

    /**
     * Constructs a new int token in binary mode.
     *
     * @param value token value
     */
    public XBinaryTokenInt(int value) {
        this.value = value;
    }

    /**
     * Returns the value of this token.
     * 
     * @return token value
     */
    public int getValue() {
        return value;
    }

    ////////////////////////////////
    // XBinaryToken implementation
    ////////////////////////////////

    public int getType() {
        return INTVALUE;
    }

    @Override
    public XBinaryTokenInt toTokenInt() {
        return this;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
