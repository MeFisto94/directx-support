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
import be.stanhebben.x2jme.data.XDMaterial;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;
import com.jme3.math.ColorRGBA;

/**
 * Contains the Material template.
 * 
 * @author Stan Hebben
 */
public class XTMaterial extends XTemplate {
    private static XTMaterial instance = new XTMaterial();

    /**
     * Returns the instance of this template.
     *
     * @return this template's instance
     */
    public static XTMaterial getInstance() {
        return instance;
    }

    /**
     * Internal constructor.
     */
    private XTMaterial() {
        super("Material");
    }

    ///////////////////////
    // XTemplate instance
    ///////////////////////

    @Override
    public XData acceptData(XNameSpace ns, XBinaryTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XBinaryToken.OBRACE);

        float cr = tokens.requiredFloat();
        float cg = tokens.requiredFloat();
        float cb = tokens.requiredFloat();
        float ca = tokens.requiredFloat();
        float power = tokens.requiredFloat();
        float sr = tokens.requiredFloat();
        float sg = tokens.requiredFloat();
        float sb = tokens.requiredFloat();
        float er = tokens.requiredFloat();
        float eg = tokens.requiredFloat();
        float eb = tokens.requiredFloat();

        XData[] children = readChildren(ns, tokens);

        return new XDMaterial(
                ns.getFile(),
                name,
                new ColorRGBA(cr, cg, cb, ca),
                power,
                new ColorRGBA(sr, sg, sb, 1.0f),
                new ColorRGBA(er, eg, eb, 1.0f),
                children);
    }

    @Override
    public XData acceptData(XNameSpace ns, XTextTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XTextToken.OBRACE);

        float cr = tokens.requiredFloat();
        tokens.requiredSeparator();
        float cg = tokens.requiredFloat();
        tokens.requiredSeparator();
        float cb = tokens.requiredFloat();
        tokens.requiredSeparator();
        float ca = tokens.requiredFloat();
        tokens.requiredSeparator();
        tokens.optional(XTextToken.SEMICOLON);
        float power = tokens.requiredFloat();
        tokens.required(XTextToken.SEMICOLON);
        float sr = tokens.requiredFloat();
        tokens.requiredSeparator();
        float sg = tokens.requiredFloat();
        tokens.requiredSeparator();
        float sb = tokens.requiredFloat();
        tokens.requiredSeparator();
        tokens.optional(XTextToken.SEMICOLON);
        float er = tokens.requiredFloat();
        tokens.requiredSeparator();
        float eg = tokens.requiredFloat();
        tokens.requiredSeparator();
        float eb = tokens.requiredFloat();
        tokens.required(XTextToken.SEMICOLON);
        tokens.optional(XTextToken.SEMICOLON);

        XData[] children = readChildren(ns, tokens);

        return new XDMaterial(
                ns.getFile(),
                name,
                new ColorRGBA(cr, cg, cb, ca),
                power,
                new ColorRGBA(sr, sg, sb, 1.0f),
                new ColorRGBA(er, eg, eb, 1.0f),
                children);
    }
}
