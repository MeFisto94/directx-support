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
 * Represents a symbolic (non - record bearing) token in binary mode.
 *
 * @author Stan Hebben
 */
public class XBinaryTokenSymbol extends XBinaryToken {
    /* Contains string values for each symbol type - used for printing tokens */
    private static final String[] symbols = {
        null,
        "(name)",
        "(string)",
        "(int)",
        "(guid)",
        "(float)",
        "{",
        "}",
        "(",
        ")",
        "[",
        "]",
        "<",
        ">",
        ".",
        ",",
        ";",
        "template",
        "word",
        "dword",
        "double",
        "char",
        "uchar",
        "sword",
        "float",
        "sdword",
        "void",
        "void",
        "lpstr",
        "unicode",
        "cstring",
        "array"
    };

    private int symbol;

    /**
     * Constructs a new symbol token.
     *
     * @param symbol symbol
     */
    public XBinaryTokenSymbol(int symbol) {
        this.symbol = symbol;
    }

    ////////////////////////////////
    // XBinaryToken implementation
    ////////////////////////////////

    public int getType() {
        return symbol;
    }

    @Override
    public XBinaryTokenSymbol toTokenSymbol() {
        return this;
    }

    @Override
    public String toString() {
        return symbols[symbol];
    }
}
