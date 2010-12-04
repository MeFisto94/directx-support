/*
 * X2JME By Stan Hebben - x2jme.sourceforge.net
 *
 * This file may be used and modified without restriction, as long a proper
 * credit is given to the original author.
 *
 * The software is provided as-is, and the author cannot be held responsible for
 * any damage, directly or indirectly, from the use of this software.
 */

package be.stanhebben.x2jme.text;

/**
 * Represents a token in a text token stream.
 *
 * @author Stan Hebben
 */
public class XTextToken {
    /** Integer token type */
    public static final int INT = 1;
    /** Float token type */
    public static final int FLOAT = 2;
    /** String token type */
    public static final int STRING = 3;
    /** Identifier token type */
    public static final int ID = 4;
    /** UUID token type */
    public static final int UUID = 5;
    /** Comma ',' */
    public static final int COMMA = 6;
    /** Semicolon ';' */
    public static final int SEMICOLON = 7;
    /** Opening brace '{' */
    public static final int OBRACE = 8;
    /** Closing brace '}' */
    public static final int CBRACE = 9;
    /** Opening parenthesis '(' */
    public static final int OPAREN = 10;
    /** Closing parenthesis ')' */
    public static final int CPAREN = 11;
    /** Opening bracket '[' */
    public static final int OBRACKET = 12;
    /** Closing bracket ']' */
    public static final int CBRACKET = 13;
    /** Three dots '...' */
    public static final int ETC = 14;

    private int type;
    private String value;

    /**
     * Constructs a new token in text mode.
     *
     * @param type token type
     * @param value token value
     */
    public XTextToken(int type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns this token's type.
     *
     * @return token type
     */
    public int getType() {
        return type;
    }

    /**
     * Returns this token's value.
     *
     * @return token value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
