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
import be.stanhebben.x2jme.data.XDMeshTextureCoords;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;

/**
 * Contains the MeshTextureCoords template.
 *
 * @author Stan Hebben
 */
public class XTMeshTextureCoords extends XTemplate {
    private static XTMeshTextureCoords instance = new XTMeshTextureCoords();

    /**
     * Returns the instance of this template.
     *
     * @return this template's instance
     */
    public static XTMeshTextureCoords getInstance() {
        return instance;
    }

    /**
     * Internal constructor.
     */
    private XTMeshTextureCoords() {
        super("MeshTextureCoords");
    }

    ///////////////////////
    // XTemplate instance
    ///////////////////////

    @Override
    public XData acceptData(XNameSpace ns, XBinaryTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XBinaryToken.OBRACE);

        int nTextureCoords = tokens.requiredInt();
        float[] textureCoords = new float[2 * nTextureCoords];
        for (int i = 0; i < nTextureCoords; i++) {
            textureCoords[2 * i + 0] = tokens.requiredFloat();
            textureCoords[2 * i + 1] = tokens.requiredFloat();
        }

        tokens.required(XBinaryToken.CBRACE);
        return new XDMeshTextureCoords(ns.getFile(), textureCoords);
    }

    @Override
    public XData acceptData(XNameSpace ns, XTextTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XTextToken.OBRACE);

        int nTextureCoords = tokens.requiredInt();
        tokens.required(XTextToken.SEMICOLON);

        float[] textureCoords = new float[2 * nTextureCoords];
        for (int i = 0; i < nTextureCoords; i++) {
            if (i > 0) tokens.requiredSeparator();
            textureCoords[2 * i + 0] = tokens.requiredFloat();
            tokens.required(XTextToken.SEMICOLON);
            textureCoords[2 * i + 1] = tokens.requiredFloat();
            tokens.required(XTextToken.SEMICOLON);
        }
        tokens.optional(XTextToken.COMMA);
        tokens.required(XTextToken.SEMICOLON);
        tokens.required(XTextToken.CBRACE);

        return new XDMeshTextureCoords(ns.getFile(), textureCoords);
    }
}
