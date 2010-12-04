/*
 * X2JME By Stan Hebben - x2jme.sourceforge.net
 *
 * This file may be used and modified without restriction, as long a proper
 * credit is given to the original author.
 *
 * The software is provided as-is, and the author cannot be held responsible for
 * any damage, directly or indirectly, from the use of this software.
 */

package be.stanhebben.x2jme.data;

import be.stanhebben.x2jme.XData;
import be.stanhebben.x2jme.XFile;
import be.stanhebben.x2jme.template.XTMeshMaterialList;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

/**
 * Contains mesh material list data.
 * 
 * @author Stan Hebben
 */
public class XDMeshMaterialList extends XData {
    private int[] faceIndexes;
    private XData[] children;

    /**
     * Creates a new MeshMaterialList instance.
     * 
     * @param file containing file
     * @param name item name
     * @param faceIndexes applicable face indexes
     * @param children item children
     */
    public XDMeshMaterialList(XFile file, String name, int[] faceIndexes, XData[] children) {
        super(file, name, XTMeshMaterialList.getInstance());

        if (faceIndexes.length == 0) faceIndexes = null;
        this.faceIndexes = faceIndexes;
        this.children = children;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////
    
    /**
     * Converts this material list to a material. Takes the first Material child.
     *
     * @param am AssetManager used to load textures and materials
     * @return converted material
     */
    @Override
    public Material toMaterial(AssetManager am) {
        for (XData data : children) {
            if (data.toMaterial(am) != null) {
                return data.toMaterial(am);
            } else {
                System.out.println("Warning: unsupported child in MeshMaterialList: " + data.getTemplate().getName());
            }
        }
        return null;
    }

    /**
     * Returns an array with all faces this material list applies to.
     *
     * @return an array with all applicable faces
     */
    @Override
    public int[] getApplicableFaces() {
        return faceIndexes;
    }

    /**
     * Converts this material list to texture coordinates.
     *
     * Sometimes the texture coordinates are placed inside the material list,
     * this method implementation provides support for those cases.
     *
     * @return the texture coordinates
     */
    @Override
    public float[] toTexCoords() {
        for (XData data : children) {
            float[] result = data.toTexCoords();
            if (result != null) return result;
        }
        return null;
    }
}
