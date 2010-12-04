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
import be.stanhebben.x2jme.data.XDFrameTransformMatrix;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;

/**
 * Contains the FrameTransformMatrix template.
 *
 * @author Stan Hebben
 */
public class XTFrameTransformMatrix extends XTemplate {
    private static XTFrameTransformMatrix instance = new XTFrameTransformMatrix();

    /**
     * Returns the instance of this template.
     *
     * @return this template's instance
     */
    public static XTFrameTransformMatrix getInstance() {
        return instance;
    }

    /**
     * Internal constructor.
     */
    public XTFrameTransformMatrix() {
        super("FrameTransformMatrix");
    }

    ///////////////////////
    // XTemplate instance
    ///////////////////////

    @Override
    public XData acceptData(XNameSpace ns, XBinaryTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XBinaryToken.OBRACE);

        float[] value = new float[16];
        for (int i = 0; i < 16; i++) {
            value[i] = tokens.requiredFloat();
        }

        tokens.required(XBinaryToken.CBRACE);
        return new XDFrameTransformMatrix(ns.getFile(), name, value);
    }

    @Override
    public XData acceptData(XNameSpace ns, XTextTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XTextToken.OBRACE);

        float[] value = new float[16];
        for (int i = 0; i < 16; i++) {
            if (i > 0) tokens.requiredSeparator();
            value[i] = tokens.requiredFloat();
        }
        
        tokens.optional(XTextToken.COMMA);
        tokens.required(XTextToken.SEMICOLON);
        tokens.optional(XTextToken.SEMICOLON);
        tokens.required(XTextToken.CBRACE);
        return new XDFrameTransformMatrix(ns.getFile(), name, value);
    }
}
