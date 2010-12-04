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

import be.stanhebben.x2jme.XParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A binary token stream holds a list of tokens, which are processed one by one
 * by the LL parser.
 *
 * @author Stan Hebben
 */
public class XBinaryTokenStream {
    /* DirectX token constants */
    private static final int TOKEN_NAME = 1;
    private static final int TOKEN_STRING = 2;
    private static final int TOKEN_INTEGER = 3;
    private static final int TOKEN_GUID = 5;
    private static final int TOKEN_INTEGER_LIST = 6;
    private static final int TOKEN_FLOAT_LIST = 7;
    private static final int TOKEN_OBRACE = 10;
    private static final int TOKEN_CBRACE = 11;
    private static final int TOKEN_OPAREN = 12;
    private static final int TOKEN_CPAREN = 13;
    private static final int TOKEN_OBRACKET = 14;
    private static final int TOKEN_CBRACKET = 15;
    private static final int TOKEN_OANGLE = 16;
    private static final int TOKEN_CANGLE = 17;
    private static final int TOKEN_DOT = 18;
    private static final int TOKEN_COMMA = 19;
    private static final int TOKEN_SEMICOLON = 20;
    private static final int TOKEN_TEMPLATE = 31;
    private static final int TOKEN_WORD = 40;
    private static final int TOKEN_DWORD = 41;
    private static final int TOKEN_FLOAT = 42;
    private static final int TOKEN_DOUBLE = 43;
    private static final int TOKEN_CHAR = 44;
    private static final int TOKEN_UCHAR = 45;
    private static final int TOKEN_SWORD = 46;
    private static final int TOKEN_SDWORD = 47;
    private static final int TOKEN_VOID = 48;
    private static final int TOKEN_LPSTR = 49;
    private static final int TOKEN_UNICODE = 50;
    private static final int TOKEN_CSTRING = 51;
    private static final int TOKEN_ARRAY = 52;

    private XBinaryToken[] tokens;
    private int position;

    /**
     * Creates a new token stream from the specified InputStream. The header
     * of the InputStream should already be read and processed (16 bytes).
     *
     * @param input stream to read data from
     * @param float64 use 64-bit float?
     * @throws IOException if the input could not be read
     */
    public XBinaryTokenStream(InputStream input, boolean float64) throws IOException {
        ArrayList<XBinaryToken> tokens = new ArrayList<XBinaryToken>();
        LEDataInputStream din = new LEDataInputStream(input);

        /* Process all tokens into an array */
        while (din.available() > 0) {
            int type = din.readShort();
            switch (type) {
                case TOKEN_NAME: {
                    int count = din.readInt();
                    byte[] data = new byte[count];
                    din.readFully(data);

                    tokens.add(new XBinaryTokenName(new String(data)));
                    break;
                }
                case TOKEN_STRING: {
                    int count = din.readInt();
                    byte[] data = new byte[count];
                    din.readFully(data);
                    int terminator = din.readShort();

                    tokens.add(new XBinaryTokenString(new String(data), terminator));
                    break;
                }
                case TOKEN_INTEGER: {
                    tokens.add(new XBinaryTokenInt(din.readInt()));
                    break;
                }
                case TOKEN_GUID: {
                    tokens.add(new XBinaryTokenGuid(din));
                    break;
                }
                case TOKEN_INTEGER_LIST: {
                    int count = din.readInt();
                    for (int i = 0; i < count; i++) {
                        tokens.add(new XBinaryTokenInt(din.readInt()));
                    }
                    break;
                }
                case TOKEN_FLOAT_LIST: {
                    int count = din.readInt();
                    if (float64) {
                        for (int i = 0; i < count; i++) {
                            tokens.add(new XBinaryTokenFloat(din.readDouble()));
                        }
                    } else {
                        for (int i = 0; i < count; i++) {
                            tokens.add(new XBinaryTokenFloat(din.readFloat()));
                        }
                    }
                    break;
                }
                case TOKEN_OBRACE:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.OBRACE));
                    break;
                case TOKEN_CBRACE:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.CBRACE));
                    break;
                case TOKEN_OPAREN:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.OPAREN));
                    break;
                case TOKEN_CPAREN:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.CPAREN));
                    break;
                case TOKEN_OBRACKET:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.OBRACKET));
                    break;
                case TOKEN_CBRACKET:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.CBRACKET));
                    break;
                case TOKEN_OANGLE:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.OANGLE));
                    break;
                case TOKEN_CANGLE:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.CANGLE));
                    break;
                case TOKEN_DOT:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.DOT));
                    break;
                case TOKEN_COMMA:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.COMMA));
                    break;
                case TOKEN_SEMICOLON:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.SEMICOLON));
                    break;
                case TOKEN_TEMPLATE:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.TEMPLATE));
                    break;
                case TOKEN_WORD:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.WORD));
                    break;
                case TOKEN_DWORD:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.DWORD));
                    break;
                case TOKEN_FLOAT:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.FLOAT));
                    break;
                case TOKEN_DOUBLE:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.DOUBLE));
                    break;
                case TOKEN_CHAR:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.CHAR));
                    break;
                case TOKEN_UCHAR:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.UCHAR));
                    break;
                case TOKEN_SWORD:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.SWORD));
                    break;
                case TOKEN_SDWORD:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.SDWORD));
                    break;
                case TOKEN_VOID:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.VOID));
                    break;
                case TOKEN_LPSTR:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.LPSTR));
                    break;
                case TOKEN_UNICODE:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.UNICODE));
                    break;
                case TOKEN_CSTRING:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.CSTRING));
                    break;
                case TOKEN_ARRAY:
                    tokens.add(new XBinaryTokenSymbol(XBinaryToken.ARRAY));
                    break;
                default:
                    throw new XParserException("Invalid token type: " + type);
            }
        }

        this.tokens = tokens.toArray(new XBinaryToken[tokens.size()]);
    }

    /**
     * Processes an optional token. If the next token is of the specified type,
     * it will be skipped. Otherwise, nothing happens. Mainly used for symbol
     * tokens.
     *
     * @param type token type
     * @return true if a token has been skipped
     */
    public boolean optional(int type) {
        return match(type) != null;
    }

    /**
     * Processes a required token. If the next token is of the specified type,
     * it will be skipped. Otherwise, an XParserException is thrown.
     *
     * @param type token type
     */
    public void required(int type) {
        require(type);
    }

    /**
     * Processes a required separator. Accepts either a comma or semicolon.
     */
    public void requiredSeparator() {
        if (match(XBinaryToken.COMMA) != null) return;
        if (match(XBinaryToken.SEMICOLON) != null) return;
        throw new XParserException("Unexpected token: " + next());
    }

    /**
     * Processes a required int token and returns its value. If the next token
     * is not an int, an XParserException is thrown.
     *
     * @return the int token value
     */
    public int requiredInt() {
        return require(XBinaryToken.INTVALUE).toTokenInt().getValue();
    }

    /**
     * Processes a required float token and returns its value. If the next token
     * is not a float, an XParserException is thrown.
     *
     * @return the float token value
     */
    public float requiredFloat() {
        return (float)require(XBinaryToken.FLOATVALUE).toTokenFloat().getValue();
    }

    /**
     * Processes an optional name token and returns its value. If the next token
     * is not a name token, null is returned instead.
     *
     * @return the name token value, or null
     */
    public String optionalName() {
        XBinaryToken token = match(XBinaryToken.NAME);
        if (token == null) return null;
        return token.toTokenName().getValue();
    }

    /**
     * Processes a required name token and returns its value. If the next token
     * is not a name token, an XParserException is thrown.
     *
     * @return the name token value
     */
    public String requiredName() {
        return require(XBinaryToken.NAME).toTokenName().getValue();
    }

    /**
     * Processes a required string token and returns its value. If the next token
     * is not a string token, an XParserException is thrown.
     *
     * @return the string token value
     */
    public String requiredString() {
        return require(XBinaryToken.STRING).toTokenString().getValue();
    }

    /**
     * Returns the next token (and advances the stream).
     *
     * @return the next token
     */
    public XBinaryToken next() {
        return tokens[position++];
    }

    /**
     * Checks if there are any more available tokens in this stream.
     *
     * @return true if there are available tokens
     */
    public boolean hasMore() {
        return position < tokens.length;
    }

    ////////////////////
    // Private methods
    ////////////////////

    /**
     * Returns the next token if its type matches the specified type. Otherwise,
     * does nothing and returns null.
     *
     * @param type required token type
     * @return the matched token, or null
     */
    private XBinaryToken match(int type) {
        if (position >= tokens.length) {
            throw new XParserException("No more tokens!");
        }
        if (tokens[position].getType() == type) return tokens[position++];
        return null;
    }

    /**
     * Returns the next token if its type matches the specified type. Otherwise,
     * throws an XParserException.
     *
     * @param type required token type
     * @return the matched token
     */
    private XBinaryToken require(int type) {
        XBinaryToken result = match(type);
        if (result == null) {
            throw new XParserException("Unexpected token: " + next());
        }
        return result;
    }
}
