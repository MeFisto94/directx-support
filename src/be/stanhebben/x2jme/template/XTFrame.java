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
import be.stanhebben.x2jme.data.XDFrame;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;

/**
 * Contains the Frame template.
 *
 * @author Stan Hebben
 */
public class XTFrame extends XTemplate {
    private static XTFrame instance = new XTFrame();

    /**
     * Returns the instance of this template.
     *
     * @return this template's instance
     */
    public static XTFrame getInstance() {
        return instance;
    }

    /**
     * Internal constructor.
     */
    private XTFrame() {
        super("Frame");
    }

    ///////////////////////
    // XTemplate instance
    ///////////////////////

    @Override
    public XData acceptData(XNameSpace ns, XBinaryTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XBinaryToken.OBRACE);

        XData[] children = readChildren(ns, tokens);
        return new XDFrame(ns.getFile(), name, children);
    }

    @Override
    public XData acceptData(XNameSpace ns, XTextTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XTextToken.OBRACE);

        XData[] children = readChildren(ns, tokens);
        return new XDFrame(ns.getFile(), name, children);
    }
}
