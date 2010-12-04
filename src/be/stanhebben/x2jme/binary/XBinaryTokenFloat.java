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
 * Represents a float token in binary mode.
 *
 * @author Stan Hebben
 */
public class XBinaryTokenFloat extends XBinaryToken {
    private double value;

    /**
     * Constructs a new binary float token.
     *
     * @param value float value
     */
    public XBinaryTokenFloat(double value) {
        this.value = value;
    }

    /**
     * Gets the value of this token.
     *
     * @return token value
     */
    public double getValue() {
        return value;
    }

    ////////////////////////////////
    // XBinaryToken implementation
    ////////////////////////////////

    @Override
    public XBinaryTokenFloat toTokenFloat() {
        return this;
    }

    @Override
    public int getType() {
        return FLOATVALUE;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
