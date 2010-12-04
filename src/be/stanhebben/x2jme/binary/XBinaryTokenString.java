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
 * Represents a string token in binary mode.
 *
 * @author Stan Hebben
 */
public class XBinaryTokenString extends XBinaryToken {
    private String value;
    private int terminator;

    /**
     * Constructs a new binary string constant token.
     *
     * @param value token value
     * @param terminator terminator type (see DirectX binary file format)
     */
    public XBinaryTokenString(String value, int terminator) {
        this.value = value;
        this.terminator = terminator;
    }

    /**
     * Returns the terminator type of this string token.
     *
     * @return terminator type.
     */
    public int getTerminator() {
        return terminator;
    }

    /**
     * Returns the value of this string.
     *
     * @return string value
     */
    public String getValue() {
        return value;
    }

    ////////////////////////////////
    // XBinaryToken implementation
    ////////////////////////////////

    @Override
    public int getType() {
        return STRING;
    }
    
    @Override
    public XBinaryTokenString toTokenString() {
        return this;
    }

    @Override
    public String toString() {
        return '"' + value + '"';
    }
}
