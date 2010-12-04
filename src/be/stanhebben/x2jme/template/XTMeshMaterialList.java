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
import be.stanhebben.x2jme.data.XDMeshMaterialList;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;

/**
 * Contains the MeshMaterialList template.
 * 
 * @author Stan Hebben
 */
public class XTMeshMaterialList extends XTemplate {
    private static XTMeshMaterialList instance = new XTMeshMaterialList();

    /**
     * Returns the instance of this template.
     *
     * @return this template's instance
     */
    public static XTMeshMaterialList getInstance() {
        return instance;
    }

    /**
     * Internal constructor.
     */
    private XTMeshMaterialList() {
        super("MeshMaterialList");
    }

    ///////////////////////
    // XTemplate instance
    ///////////////////////

    @Override
    public XData acceptData(XNameSpace ns, XBinaryTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XBinaryToken.OBRACE);

        int nMaterials = tokens.requiredInt();
        int nFaceIndexes = tokens.requiredInt();
        int[] faceIndexes = new int[nFaceIndexes];
        for (int i = 0; i < nFaceIndexes; i++) {
            faceIndexes[i] = tokens.requiredInt();
        }

        XData[] children = readChildren(ns, tokens);
        return new XDMeshMaterialList(ns.getFile(), name, faceIndexes, children);
    }

    @Override
    public XData acceptData(XNameSpace ns, XTextTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XTextToken.OBRACE);

        int nMaterials = tokens.requiredInt();
        tokens.required(XTextToken.SEMICOLON);
        int nFaceIndexes = tokens.requiredInt();
        tokens.required(XTextToken.SEMICOLON);

        int[] faceIndexes = new int[nFaceIndexes];
        for (int i = 0; i < nFaceIndexes; i++) {
            if (i > 0) tokens.requiredSeparator();
            faceIndexes[i] = tokens.requiredInt();
        }

        tokens.optional(XTextToken.COMMA);
        tokens.required(XTextToken.SEMICOLON);
        tokens.optional(XTextToken.SEMICOLON);

        XData[] children = readChildren(ns, tokens);
        return new XDMeshMaterialList(ns.getFile(), name, faceIndexes, children);
    }
}
