/*
 * X2JME By Stan Hebben - x2jme.sourceforge.net
 *
 * This file may be used and modified without restriction, as long a proper
 * credit is given to the original author.
 *
 * The software is provided as-is, and the author cannot be held responsible for
 * any damage, directly or indirectly, from the use of this software.
 */

package be.stanhebben.x2jme.template;

import be.stanhebben.x2jme.XData;
import be.stanhebben.x2jme.XNameSpace;
import be.stanhebben.x2jme.XTemplate;
import be.stanhebben.x2jme.binary.XBinaryToken;
import be.stanhebben.x2jme.binary.XBinaryTokenStream;
import be.stanhebben.x2jme.data.XDTextureFilename;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;

/**
 * Contains the TextureFilename template.
 * 
 * @author Stan Hebben
 */
public class XTTextureFilename extends XTemplate {
    private static XTTextureFilename instance = new XTTextureFilename();

    /**
     * Returns the instance of this template.
     *
     * @return this template's instance
     */
    public static XTTextureFilename getInstance() {
        return instance;
    }

    /**
     * Internal constructor.
     */
    private XTTextureFilename() {
        super("TextureFilename");
    }

    ///////////////////////
    // XTemplate instance
    ///////////////////////

    @Override
    public XData acceptData(XNameSpace ns, XBinaryTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XBinaryToken.OBRACE);

        String filename = tokens.requiredString();

        tokens.required(XBinaryToken.CBRACE);
        return new XDTextureFilename(ns.getFile(), filename);
    }

    @Override
    public XData acceptData(XNameSpace ns, XTextTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XTextToken.OBRACE);

        String filename = tokens.requiredString();
        tokens.required(XTextToken.SEMICOLON);

        tokens.required(XTextToken.CBRACE);
        return new XDTextureFilename(ns.getFile(), filename);
    }
}
