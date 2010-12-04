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

import be.stanhebben.x2jme.XParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains a list of tokens and presents them one by one to the LL parser.
 *
 * @author Stan Hebben
 */
public class XTextTokenStream {
    /* State machine definition used by the tokener */
    private static final HashMap<Integer, Integer>[] states;
    private static final int[] finals = {
        -1,
        XTextToken.ID,
        XTextToken.INT,
        XTextToken.FLOAT,
        -1,
        XTextToken.STRING,
        XTextToken.OBRACE,
        XTextToken.CBRACE,
        XTextToken.OPAREN,
        XTextToken.CPAREN,
        XTextToken.COMMA,
        -1,
        -1,
        XTextToken.ETC,
        XTextToken.SEMICOLON,
        XTextToken.OBRACKET,
        XTextToken.CBRACKET,
        -1,
        XTextToken.UUID
    };

    /* State machine states */
    private static final int STATE_START = 0;
    private static final int STATE_ID = 1;
    private static final int STATE_INT = 2;
    private static final int STATE_FLOAT = 3;
    private static final int STATE_STR = 4;
    private static final int STATE_STREND = 5;
    private static final int STATE_BROPEN = 6;
    private static final int STATE_BRCLOSE = 7;
    private static final int STATE_POPEN = 8;
    private static final int STATE_PCLOSE = 9;
    private static final int STATE_COMMA = 10;
    private static final int STATE_DOT1 = 11;
    private static final int STATE_DOT2 = 12;
    private static final int STATE_DOT3 = 13;
    private static final int STATE_SEMICOLON = 14;
    private static final int STATE_SQBROPEN = 15;
    private static final int STATE_SQBRCLOSE = 16;
    private static final int STATE_UUID = 17;
    private static final int STATE_UUIDEND = 18;

    private static final int NUM_STATES = 19;

    static {
        /* Construct the state machine */
        states = new HashMap[NUM_STATES];
        states[STATE_START] = new HashMap<Integer, Integer>();
        for (int i = 'a'; i <= 'z'; i++) states[STATE_START].put(i, STATE_ID);
        for (int i = 'A'; i <= 'Z'; i++) states[STATE_START].put(i, STATE_ID);
        states[STATE_START].put((int)'_', STATE_ID);
        for (int i = '0'; i <= '9'; i++) states[STATE_START].put(i, STATE_INT);
        states[STATE_START].put((int)'-', STATE_INT);
        states[STATE_START].put((int)'"', STATE_STR);
        states[STATE_START].put((int)'{', STATE_BROPEN);
        states[STATE_START].put((int)'}', STATE_BRCLOSE);
        states[STATE_START].put((int)'(', STATE_POPEN);
        states[STATE_START].put((int)')', STATE_PCLOSE);
        states[STATE_START].put((int)',', STATE_COMMA);
        states[STATE_START].put((int)'.', STATE_DOT1);
        states[STATE_START].put((int)';', STATE_SEMICOLON);
        states[STATE_START].put((int)'[', STATE_SQBROPEN);
        states[STATE_START].put((int)']', STATE_SQBRCLOSE);
        states[STATE_START].put((int)'<', STATE_UUID);

        states[STATE_ID] = new HashMap<Integer, Integer>();
        for (int i = 'a'; i <= 'z'; i++) states[STATE_ID].put(i, STATE_ID);
        for (int i = 'A'; i <= 'Z'; i++) states[STATE_ID].put(i, STATE_ID);
        states[STATE_ID].put((int)'_', STATE_ID);
        states[STATE_ID].put((int)'-', STATE_ID);
        states[STATE_ID].put((int)'+', STATE_ID);
        for (int i = '0'; i <= '9'; i++) states[STATE_ID].put(i, STATE_ID);

        states[STATE_INT] = new HashMap<Integer, Integer>();
        for (int i = '0'; i <= '9'; i++) states[STATE_INT].put(i, STATE_INT);
        states[STATE_INT].put((int)'.', STATE_FLOAT);

        states[STATE_FLOAT] = new HashMap<Integer, Integer>();
        for (int i = '0'; i <= '9'; i++) states[STATE_FLOAT].put(i, STATE_FLOAT);

        states[STATE_STR] = new HashMap<Integer, Integer>();
        for (int i = 0; i < 128; i++) states[STATE_STR].put(i, STATE_STR);
        states[STATE_STR].put((int)'"', STATE_STREND);

        states[STATE_DOT1] = new HashMap<Integer, Integer>();
        states[STATE_DOT1].put((int)'.', STATE_DOT2);
        states[STATE_DOT2] = new HashMap<Integer, Integer>();
        states[STATE_DOT2].put((int)'.', STATE_DOT3);

        states[STATE_UUID] = new HashMap<Integer, Integer>();
        for (int i = 'a'; i <= 'z'; i++) states[STATE_UUID].put(i, STATE_UUID);
        for (int i = 'A'; i <= 'Z'; i++) states[STATE_UUID].put(i, STATE_UUID);
        for (int i = '0'; i <= '9'; i++) states[STATE_UUID].put(i, STATE_UUID);
        states[STATE_UUID].put((int)'-', STATE_UUID);
        states[STATE_UUID].put((int)'>', STATE_UUIDEND);
    }

    private XTextToken[] tokens;
    private int position;

    /**
     * Constructs a new text token stream from the specified InputStream. This
     * constructor assumes that the header has already been read and processed.
     *
     * @param in InputStream to read data from
     * @throws IOException if the file could not be read properly
     */
    public XTextTokenStream(InputStream in) throws IOException {
        tokens = tokenize(in);
        position = 0;
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
        if (match(XTextToken.COMMA) != null) return;
        if (match(XTextToken.SEMICOLON) != null) return;
        throw new XParserException("Unexpected token: " + next());
    }

    /**
     * Processes a required int token and returns its value. If the next token
     * is not an int, an XParserException is thrown.
     *
     * @return the int token value
     */
    public int requiredInt() {
        return Integer.parseInt(require(XTextToken.INT).getValue());
    }

    /**
     * Processes a required float token and returns its value. If the next token
     * is not a float, an XParserException is thrown.
     *
     * @return the float token value
     */
    public float requiredFloat() {
        return Float.parseFloat(require(XTextToken.FLOAT).getValue());
    }

    /**
     * Processes an optional name token and returns its value. If the next token
     * is not a name token, null is returned instead.
     *
     * @return the name token value, or null
     */
    public String optionalName() {
        XTextToken token = match(XTextToken.ID);
        if (token == null) return null;
        return token.getValue();
    }

    /**
     * Processes an optional name token with a specific value. If the next token
     * is not a name token, or the value of the next token does not equal the
     * requested value, false is returned.
     * 
     * @param value the requested value
     * @return true if the next token does not equal the specified value
     */
    public boolean optionalName(String value) {
        return match(XTextToken.ID, value) != null;
    }

    /**
     * Processes a required name token and returns its value. If the next token
     * is not a name token, an XParserException is thrown.
     *
     * @return the name token value
     */
    public String requiredName() {
        return require(XTextToken.ID).getValue();
    }

    /**
     * Processes a required string token and returns its value. If the next token
     * is not a string token, an XParserException is thrown.
     *
     * @return the string token value
     */
    public String requiredString() {
        String svalue = require(XTextToken.STRING).getValue();
        return svalue.substring(1, svalue.length() - 1);
    }

    /**
     * Returns the next token (and advances the stream).
     *
     * @return the next token
     */
    public XTextToken next() {
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
    private XTextToken match(int type) {
        if (position >= tokens.length) {
            throw new XParserException("Out of tokens");
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
    private XTextToken require(int type) {
        XTextToken result = match(type);
        if (result == null) {
            throw new XParserException("Unexpected token: " + next());
        }
        return result;
    }

    /**
     * Returns the next token if its type matches the specified type and its
     * value equals the requested value. Otherwise, does nothing and returns null.
     *
     * @param type required token type
     * @param value required token value
     * @return the matched token, or null
     */
    private XTextToken match(int type, String value) {
        if (tokens[position].getType() == type && tokens[position].getValue().equals(value)) {
            return tokens[position++];
        }
        return null;
    }

    ///////////////////////////
    // Private static methods
    ///////////////////////////

    /**
     * Splits the input in tokens, skipping whitespace.
     *
     * @param input InputStream to read all tokens from
     * @return the input split into tokens
     * @throws IOException if the input could not be read properly
     */
    private static XTextToken[] tokenize(InputStream input) throws IOException {
        ArrayList<XTextToken> tokens = new ArrayList<XTextToken>();

        int ch = skipWhiteSpace(input.read(), input);
        int state = STATE_START;
        StringBuilder sb = new StringBuilder();

        while (ch >= 0) {
            if (states[state] == null || !states[state].containsKey(ch)) {
                if (finals[state] >= 0) {
                    tokens.add(new XTextToken(finals[state], sb.toString()));
                    sb = new StringBuilder();
                } else {
                    throw new IllegalArgumentException("Could not tokenize the input");
                }

                ch = skipWhiteSpace(ch, input);
                state = STATE_START;
            } else {
                state = states[state].get(ch);
                sb.append((char)ch);
                ch = input.read();
            }
        }
        if (finals[state] > 0) {
            tokens.add(new XTextToken(finals[state], sb.toString()));
        }

        return tokens.toArray(new XTextToken[tokens.size()]);
    }

    /**
     * Skips whitespace, and return the first character after whitespace. Returns
     * -1 if the end of the stream has been reached.
     *
     * @param ch the current character
     * @param in the InputStream to read characters from
     * @return the next non-whitespace character
     * @throws IOException
     */
    private static int skipWhiteSpace(int ch, InputStream in) throws IOException {
        while (true) {
            if (ch < 0) return ch;
            if (ch == '#') {
                while (ch != '\r' && ch != '\n' && ch >= 0) ch = in.read();
                continue;
            }
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
                ch = in.read();
                continue;
            }
            if (ch == '/') {
                while (ch != '\r' && ch != '\n' && ch >= 0) ch = in.read();
                continue;
            }
            break;
        }
        return ch;
    }
}
