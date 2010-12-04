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
import be.stanhebben.x2jme.template.XTMeshNormals;

/**
 * Contains mesh normal data. Instantiates the MeshNormals template.
 *
 * @author Stan Hebben
 */
public class XDMeshNormals extends XData {
    private float[] nx;
    private float[] ny;
    private float[] nz;
    private int[] indices;

    private float[] out;

    /**
     * Creates new normal data.
     *
     * @param file containing file
     * @param nx normal x coordinates
     * @param ny normal y coordinates
     * @param nz normal z coordinates
     * @param indices normal indices
     */
    public XDMeshNormals(XFile file, float[] nx, float[] ny, float[] nz, int[] indices) {
        super(file, null, XTMeshNormals.getInstance());

        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.indices = indices;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////

    @Override
    public float[] toNormals() {
        if (out == null) {
            /* Adapts the normal data to the new coordinate system */
            float[] result = new float[indices.length * 3];
            for (int i = 0; i < indices.length; i++) {
                result[i * 3 + 0] = nx[indices[i]];
                result[i * 3 + 1] = ny[indices[i]];
                result[i * 3 + 2] = -nz[indices[i]];
            }
            out = result;
        }
        return out;
    }
}
