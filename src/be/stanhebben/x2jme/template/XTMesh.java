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
import be.stanhebben.x2jme.data.XDMesh;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains the Mesh template.
 * 
 * @author Stan Hebben
 */
public class XTMesh extends XTemplate {
    private static XTMesh instance = new XTMesh();

    /**
     * Returns the instance of this template.
     *
     * @return this template's instance
     */
    public static XTMesh getInstance() {
        return instance;
    }

    /**
     * Internal constructor.
     */
    private XTMesh() {
        super("Mesh");
    }

    ///////////////////////
    // XTemplate instance
    ///////////////////////

    @Override
    public XData acceptData(XNameSpace ns, XBinaryTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XBinaryToken.OBRACE);

        int nVertices = tokens.requiredInt();
        float[] position = new float[nVertices * 3];
        for (int i = 0; i < nVertices; i++) {
            position[3 * i + 0] = tokens.requiredFloat();
            position[3 * i + 1] = tokens.requiredFloat();
            position[3 * i + 2] = tokens.requiredFloat();
        }
        int nFaces = tokens.requiredInt();
        List<Face> faces = new ArrayList<Face>();

        for (int i = 0; i < nFaces; i++) {
            int nIndices = tokens.requiredInt();
            int[] ix = new int[nIndices];

            for (int j = 0; j < nIndices; j++) {
                ix[j] = tokens.requiredInt();
            }

            triangulate(faces, position, ix);
        }

        int[] indices = new int[3 * faces.size()];
        for (int i = 0; i < faces.size(); i++) {
            indices[3 * i + 0] = faces.get(i).i1;
            indices[3 * i + 1] = faces.get(i).i2;
            indices[3 * i + 2] = faces.get(i).i3;
        }

        XData[] children = readChildren(ns, tokens);
        return new XDMesh(ns.getFile(), name, position, indices, children);
    }

    @Override
    public XData acceptData(XNameSpace ns, XTextTokenStream tokens) {
        String name = tokens.optionalName();
        tokens.required(XTextToken.OBRACE);

        int nVertices = tokens.requiredInt();
        tokens.required(XTextToken.SEMICOLON);

        float[] position = new float[nVertices * 3];
        for (int i = 0; i < nVertices; i++) {
            if (i > 0) tokens.requiredSeparator();
            position[3 * i + 0] = tokens.requiredFloat();
            tokens.required(XTextToken.SEMICOLON);
            position[3 * i + 1] = tokens.requiredFloat();
            tokens.required(XTextToken.SEMICOLON);
            position[3 * i + 2] = tokens.requiredFloat();
            tokens.required(XTextToken.SEMICOLON);
        }
        tokens.optional(XTextToken.COMMA);
        tokens.optional(XTextToken.SEMICOLON);

        int nFaces = tokens.requiredInt();
        tokens.required(XTextToken.SEMICOLON);

        List<Face> faces = new LinkedList<Face>();

        for (int i = 0; i < nFaces; i++) {
            if (i > 0) tokens.requiredSeparator();

            int nIndices = tokens.requiredInt();
            tokens.required(XTextToken.SEMICOLON);
            int[] ix = new int[nIndices];

            for (int j = 0; j < nIndices; j++) {
                if (j > 0) tokens.requiredSeparator();
                ix[j] = tokens.requiredInt();
            }
            tokens.optional(XTextToken.COMMA);
            tokens.required(XTextToken.SEMICOLON);

            triangulate(faces, position, ix);
        }
        tokens.optional(XTextToken.COMMA);
        tokens.required(XTextToken.SEMICOLON);

        int[] indices = new int[3 * faces.size()];
        for (int i = 0; i < faces.size(); i++) {
            indices[3 * i + 0] = faces.get(i).i1;
            indices[3 * i + 1] = faces.get(i).i2;
            indices[3 * i + 2] = faces.get(i).i3;
        }

        XData[] children = readChildren(ns, tokens);
        return new XDMesh(ns.getFile(), name, position, indices, children);
    }

    private void triangulate(List<Face> out, float[] position, int[] indices) {
        int center = indices[0];
        for (int i = 0; i < indices.length - 2; i++) {
            out.add(new Face(center, indices[i + 2], indices[i + 1]));
        }
    }

    private class Face {
        private int i1;
        private int i2;
        private int i3;

        public Face(int i1, int i2, int i3) {
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
        }
    }
}
