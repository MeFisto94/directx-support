/*
 * X2JME By Stan Hebben - x2jme.sourceforge.net
 *
 * This file may be used and modified without restriction, as long a proper
 * credit is given to the original author.
 *
 * The software is provided as-is, and the author cannot be held responsible for
 * any damage, directly or indirectly, from the use of this software.
 */

package be.stanhebben.x2jme;

/**
 * This exception is thrown when an input file could not be parsed properly.
 *
 * @author Stan Hebben
 */
public class XParserException extends RuntimeException {
    /**
     * Creates a new parser exception.
     *
     * @param message message string
     */
    public XParserException(String message) {
        super(message);
    }

    /**
     * Creates a new parser exception which is caused by an other exception.
     *
     * @param message message string
     * @param cause exception cause
     */
    public XParserException(String message, Exception cause) {
        super(message, cause);
    }
}
