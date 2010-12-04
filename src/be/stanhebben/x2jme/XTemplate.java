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

import be.stanhebben.x2jme.binary.XBinaryToken;
import be.stanhebben.x2jme.binary.XBinaryTokenStream;
import be.stanhebben.x2jme.template.XTFrame;
import be.stanhebben.x2jme.template.XTFrameTransformMatrix;
import be.stanhebben.x2jme.template.XTMaterial;
import be.stanhebben.x2jme.template.XTMesh;
import be.stanhebben.x2jme.template.XTMeshMaterialList;
import be.stanhebben.x2jme.template.XTMeshNormals;
import be.stanhebben.x2jme.template.XTMeshTextureCoords;
import be.stanhebben.x2jme.template.XTTextureFilename;
import be.stanhebben.x2jme.text.XTextToken;
import be.stanhebben.x2jme.text.XTextTokenStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the abstract superclass of all template definitions. Templates
 * are hardcoded, custom and unsupported template instance are not parsed (but
 * skipped). Template definitions are completely skipped as well.
 *
 * @author Stan Hebben
 */
public abstract class XTemplate {
    private static final HashMap<String, XTemplate> templates;

    static {
        /* Set up template definitions */
        templates = new HashMap<String, XTemplate>();
        templates.put("frame", XTFrame.getInstance());
        templates.put("frametransformmatrix", XTFrameTransformMatrix.getInstance());
        templates.put("material", XTMaterial.getInstance());
        templates.put("mesh", XTMesh.getInstance());
        templates.put("meshmateriallist", XTMeshMaterialList.getInstance());
        templates.put("meshnormals", XTMeshNormals.getInstance());
        templates.put("meshtexturecoords", XTMeshTextureCoords.getInstance());
        templates.put("texturefilename", XTTextureFilename.getInstance());
    }

    /**
     * Skips a template definition. If the next tokens do not define a template,
     * nothing happens.
     *
     * @param tokens token stream
     * @return true if a template has been skipped
     */
    public static boolean skip(XBinaryTokenStream tokens) {
        if (tokens.optional(XBinaryToken.TEMPLATE)) {
            tokens.required(XBinaryToken.NAME);
            tokens.required(XBinaryToken.OBRACE);
            
            XBinaryToken token = tokens.next();
            while (token.getType() != XBinaryToken.CBRACE) token = tokens.next();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Skips a template definition. If the next tokens do not define a template,
     * nothing happens.
     *
     * @param tokens token stream
     * @return true if a template has been skipped
     */
    public static boolean skip(XTextTokenStream tokens) {
        if (tokens.optionalName("template")) {
            tokens.required(XTextToken.ID);
            tokens.required(XTextToken.OBRACE);

            XTextToken token = tokens.next();
            while (token.getType() != XTextToken.CBRACE) token = tokens.next();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Reads a data item. If the data item could be read properly, that data
     * item is returned. If the data item could be read but uses an unknown
     * template, null is returned.
     *
     * @param ns namespace
     * @param tokens token stream
     * @return data item
     */
    public static XData read(XNameSpace ns, XBinaryTokenStream tokens) {
        String templateName = tokens.optionalName();
        if (templateName == null) {
            tokens.required(XBinaryToken.OBRACE);
            String ref = tokens.requiredName();
            XData result = ns.get(ref);
            tokens.required(XBinaryToken.CBRACE);
            return result;
        }
        
        XTemplate template = templates.get(templateName.toLowerCase());
        if (template == null) {
            // unknown template: skip
            int level = 0;
            while (true) {
                XBinaryToken token = tokens.next();
                if (token.getType() == XBinaryToken.OBRACE) level++;
                if (token.getType() == XBinaryToken.CBRACE) {
                    level--;
                    if (level == 0) break;
                }
            }
            return null;
        } else {
            XData result = template.acceptData(ns, tokens);
            if (result.getName() != null) ns.add(result);
            return result;
        }
    }

    /**
     * Reads a data item. If the data item could be read properly, that data
     * item is returned. If the data item could be read but uses an unknown
     * template, null is returned.
     *
     * @param ns namespace
     * @param tokens token stream
     * @return data item
     */
    public static XData read(XNameSpace ns, XTextTokenStream tokens) {
        String templateName = tokens.requiredName();

        XTemplate template = templates.get(templateName.toLowerCase());
        if (template == null) {
            // unknown template: skip
            int level = 0;
            while (true) {
                XTextToken token = tokens.next();
                if (token.getType() == XTextToken.OBRACE) level++;
                if (token.getType() == XTextToken.CBRACE) {
                    level--;
                    if (level == 0) break;
                }
            }
            return null;
        } else {
            XData result = template.acceptData(ns, tokens);
            if (result.getName() != null) ns.add(result);
            return result;
        }
    }

    /**
     * Reads the children of this item. All fixed data of this item is already
     * processed. Also reads the closing brace of this item. Returns null if
     * there are no children at all.
     * 
     * @param ns namespace
     * @param tokens token stream
     * @return children array
     */
    public static XData[] readChildren(XNameSpace ns, XBinaryTokenStream tokens) {
        if (tokens.optional(XBinaryToken.CBRACE)) return null;

        ArrayList<XData> result = new ArrayList<XData>();
        while (!tokens.optional(XBinaryToken.CBRACE)) {
            XData data = read(ns, tokens);
            if (data != null) {
                result.add(data);
            }
        }
        return result.toArray(new XData[result.size()]);
    }

    /**
     * Reads the children of this item. All fixed data of this item is already
     * processed. Also reads the closing brace of this item. Returns null if
     * there are no children at all.
     *
     * @param ns namespace
     * @param tokens token stream
     * @return children array
     */
    public static XData[] readChildren(XNameSpace ns, XTextTokenStream tokens) {
        if (tokens.optional(XTextToken.CBRACE)) return null;

        ArrayList<XData> result = new ArrayList<XData>();
        while (!tokens.optional(XTextToken.CBRACE)) {
            XData data = read(ns, tokens);
            if (data != null) {
                result.add(data);
            }
        }
        return result.toArray(new XData[result.size()]);
    }

    private String name;

    /**
     * Constructs a new template with the specified name.
     *
     * @param name template name
     */
    public XTemplate(String name) {
        this.name = name;
    }

    /**
     * Returns the template name.
     *
     * @return template name
     */
    public String getName() {
        return name;
    }

    /**
     * Instantiates this template. Only the token that specified the template
     * name has been read, all other tokens are still unread.
     *
     * @param ns namespace
     * @param tokens token stream
     * @return template instance
     */
    public abstract XData acceptData(XNameSpace ns, XBinaryTokenStream tokens);

    /**
     * Instantiates this template. Only the token that specified the template
     * name has been read, all other tokens are still unread.
     *
     * @param ns namespace
     * @param tokens token stream
     * @return template instance
     */
    public abstract XData acceptData(XNameSpace ns, XTextTokenStream tokens);
}
