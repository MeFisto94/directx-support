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

import be.stanhebben.x2jme.binary.XBinaryTokenStream;
import be.stanhebben.x2jme.text.XTextTokenStream;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Transform;
import com.jme3.scene.Spatial;

/**
 * Abstract superclass of all data items (meshes, materials, frame, ...)
 *
 * Each data item represents an instance of a template.
 *
 * @author Stan Hebben
 */
public abstract class XData {
    /**
     * Reads a data item from the specified token stream. Returns null if the
     * next tokens do not represent a data item.
     *
     * @param ns namespace
     * @param tokens token stream
     * @return the parsed data item, or null
     */
    public static XData accept(XNameSpace ns, XBinaryTokenStream tokens) {
        return XTemplate.read(ns, tokens);
    }

    /**
     * Reads a data item from the specified token stream. Returns null if the
     * next tokens do not represent a data item.
     *
     * @param ns namespace
     * @param tokens token stream
     * @return the parsed data item, or null
     */
    public static XData accept(XNameSpace ns, XTextTokenStream tokens) {
        return XTemplate.read(ns, tokens);
    }

    private XFile file;
    private String name;
    private XTemplate template;

    /**
     * Creates a new data item with the specified name, which represents an
     * instance of the specified template.
     *
     * @param file containing file
     * @param name data item name, or null if no name is specified
     * @param template data template
     */
    public XData(XFile file, String name, XTemplate template) {
        this.file = file;
        this.name = name;
        this.template = template;
    }

    public XFile getFile() {
        return file;
    }

    /**
     * Returns the name of this data item. Can be null if no name has been set.
     *
     * @return data item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the template of this data item.
     *
     * @return data item template
     */
    public XTemplate getTemplate() {
        return template;
    }

    /**
     * Converts this data item to a spatial. Returns null if this data item
     * cannot be converted to a spatial.
     *
     * @param am AssetManager used to load textures and materials
     * @return data item converted to spatial, or null
     */
    public Spatial toSpatial(AssetManager am) {
        return null;
    }

    /**
     * Converts this data item to a material. Returns null if this data item
     * cannot be converted to a material.
     *
     * @param am AssetManager used to load materials and textures
     * @return data item converted to a material, or null
     */
    public Material toMaterial(AssetManager am) {
        return null;
    }

    /**
     * Returns an array of the faces this material applies to. If the current
     * data is not a material, or the material applies to all faces, this method
     * returns null.
     *
     * @return the faces this material applies to, or null
     */
    public int[] getApplicableFaces() {
        return null;
    }

    /**
     * Converts this data item to a transform. Returns null if this data item
     * cannot be converted to a transform.
     *
     * @return data item converted to a transform, or null
     */
    public Transform toTransform() {
        return null;
    }

    /**
     * Converts this data item to a texture name. Returns null if this data
     * item does not represent a texture name.
     *
     * @return data item converted to a texture name, or null
     */
    public String toTexture() {
        return null;
    }

    /**
     * Converts this data item to a list of normals. Returns null if this data
     * item does not represent normal data.
     *
     * @return data item converted to a list of normals, or null
     */
    public float[] toNormals() {
        return null;
    }

    /**
     * Converts this data item to texture coordinates. Returns null if this data
     * item does not represent texture coordinates.
     *
     * @return data item converted to texture coordinates, or null
     */
    public float[] toTexCoords() {
        return null;
    }
}
