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
import be.stanhebben.x2jme.template.XTMeshTextureCoords;

/**
 * Contains texture coordinate data. Instantiates the MeshTextureCoords template.
 *
 * @author Stan Hebben
 */
public class XDMeshTextureCoords extends XData {
    private float[] coords;

    /**
     * Constructs a new MeshTextureCoords instance.
     *
     * @param file containing file
     * @param coords texture coordinates
     */
    public XDMeshTextureCoords(XFile file, float[] coords) {
        super(file, null, XTMeshTextureCoords.getInstance());

        this.coords = coords;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////

    @Override
    public float[] toTexCoords() {
        return coords;
    }
}
