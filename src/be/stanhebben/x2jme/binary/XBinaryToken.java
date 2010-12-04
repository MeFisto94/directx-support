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
 * Represents a token in binary mode.
 *
 * @author Stan Hebben
 */
public abstract class XBinaryToken {
    /** Name token (identifier) */
    public static final int NAME = 1;
    /** String token */
    public static final int STRING = 2;
    /** Int value (either individual or as part of an integer list) */
    public static final int INTVALUE = 3;
    /** GUID token */
    public static final int GUID = 4;
    /** Float value (part of a float list) */
    public static final int FLOATVALUE = 5;
    /** Opening brace '{' */
    public static final int OBRACE = 6;
    /** Closing brace '}' */
    public static final int CBRACE = 7;
    /** Opening parenthesis '(' */
    public static final int OPAREN = 8;
    /** Closing parenthesis ')' */
    public static final int CPAREN = 9;
    /** Opening bracket '[' */
    public static final int OBRACKET = 10;
    /** Closing bracket ']' */
    public static final int CBRACKET = 11;
    /** Opening angled bracket '<' */
    public static final int OANGLE = 12;
    /** Closing angled bracket '>' */
    public static final int CANGLE = 13;
    /** Dot '.' */
    public static final int DOT = 14;
    /** Comma ',' */
    public static final int COMMA = 15;
    /** Semicolon ';' */
    public static final int SEMICOLON = 16;
    /** Keyword 'template' */
    public static final int TEMPLATE = 17;
    /** Keyword 'word' */
    public static final int WORD = 18;
    /** Keyword 'dword' */
    public static final int DWORD = 19;
    /** Keyword 'double' */
    public static final int DOUBLE = 20;
    /** Keyword 'char' */
    public static final int CHAR = 21;
    /** Keyword 'uchar' */
    public static final int UCHAR = 22;
    /** Keyword 'sword' */
    public static final int SWORD = 23;
    /** Keyword 'float' */
    public static final int FLOAT = 24;
    /** Keyword 'sdword' */
    public static final int SDWORD = 25;
    /** Keyword 'void' */
    public static final int VOID = 26;
    /** Keyword 'lpstr' */
    public static final int LPSTR = 27;
    /** Keyword 'unicode' */
    public static final int UNICODE = 28;
    /** Keyword 'cstring' */
    public static final int CSTRING = 29;
    /** Keyword 'array' */
    public static final int ARRAY = 30;

    /**
     * Returns the token type.
     *
     * @return token type
     */
    public abstract int getType();

    /**
     * Casts this token to a name token, if possible. Returns null otherwise.
     *
     * @return name token, or null
     */
    public XBinaryTokenName toTokenName() {
        return null;
    }

    /**
     * Casts this token to a string token, if possible. Returns null otherwise.
     *
     * @return string token, or null
     */
    public XBinaryTokenString toTokenString() {
        return null;
    }

    /**
     * Casts this token to an int token, if possible. Returns null otherwise.
     *
     * @return int token, or null
     */
    public XBinaryTokenInt toTokenInt() {
        return null;
    }

    /**
     * Casts this token to a float token, if possible. Returns null otherwise.
     *
     * @return float token, or null
     */
    public XBinaryTokenFloat toTokenFloat() {
        return null;
    }

    /**
     * Casts this token to a guid token, if possible. Returns null otherwise.
     *
     * @return guid token, or null
     */
    public XBinaryTokenGuid toTokenGuid() {
        return null;
    }

    /**
     * Casts this token to a symbol token, if possible. Returns null otherwise.
     *
     * @return symbol token, or null
     */
    public XBinaryTokenSymbol toTokenSymbol() {
        return null;
    }
}
