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
 * Represents a name (identifier) token in binary mode.
 *
 * @author Stan Hebben
 */
public class XBinaryTokenName extends XBinaryToken {
    private String value;

    /**
     * Constructs a new name token.
     *
     * @param value name value
     */
    public XBinaryTokenName(String value) {
        this.value = value;
    }

    /**
     * Returns the name value of this token.
     *
     * @return name value
     */
    public String getValue() {
        return value;
    }

    ////////////////////////////////
    // XBinaryToken implementation
    ////////////////////////////////

    public int getType() {
        return NAME;
    }

    @Override
    public XBinaryTokenName toTokenName() {
        return this;
    }

    @Override
    public String toString() {
        return value;
    }
}
