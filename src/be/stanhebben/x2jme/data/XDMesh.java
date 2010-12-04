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
import be.stanhebben.x2jme.template.XTMesh;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;

/**
 * Contains Mesh data. Instantiates the Mesh template.
 *
 * @author Stan Hebben
 */
public class XDMesh extends XData {
    private float[] position;
    private int[] index;
    private XData[] children;

    private Mesh mesh;
    private Geometry geometry;

    /**
     * Constructs a new Mesh instance.
     *
     * @param file containing file
     * @param name mesh name
     * @param position position data
     * @param index index data
     * @param children children data
     */
    public XDMesh(XFile file, String name, float[] position, int[] index, XData[] children) {
        super(file, name, XTMesh.getInstance());

        this.position = position;
        this.index = index;
        this.children = children;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////

    /**
     * Converts this Mesh to a Geometry instance.
     *
     * @param am AssetManager used to load textures and materials
     * @return this Mesh converted to a Geometry
     */
    @Override
    public Spatial toSpatial(AssetManager am) {
        if (mesh == null) {
            mesh = new Mesh();

            /* Adapt to the new coordinate system */
            float[] xpos = new float[index.length * 3];
            for (int i = 0; i < index.length; i++) {
                xpos[3 * i + 0] = position[index[i] * 3 + 0];
                xpos[3 * i + 1] = position[index[i] * 3 + 1];
                xpos[3 * i + 2] = -position[index[i] * 3 + 2];
            }

            mesh.setBuffer(Type.Position, 3, xpos);

            /* Constructs a 1:1 index buffer */
            int[] ib = new int[index.length];
            for (int i = 0; i < index.length; i++) {
                ib[i] = i;
            }
            mesh.setBuffer(Type.Index, 3, ib);

            /* Reads normals and texture coordinates and applies a material, if possible */
            float[] normals = null;
            float[] texcoords = null;

            geometry = new Geometry(getName(), mesh);
            for (XData data : children) {
                if (data.toMaterial(am) != null) {
                    // TODO: apply the material selectively if necessary
                    Material mat = data.toMaterial(am);
                    geometry.setMaterial(mat);
                }
                if (data.toNormals() != null) {
                    normals = data.toNormals();
                    mesh.setBuffer(Type.Normal, 3, normals);
                }
                if (data.toTexCoords() != null) {
                    texcoords = new float[index.length * 2];
                    float[] dcoords = data.toTexCoords();
                    for (int i = 0; i < index.length; i++) {
                        texcoords[2 * i + 0] = dcoords[2 * index[i] + 0];
                        texcoords[2 * i + 1] = -dcoords[2 * index[i] + 1];
                    }
                    mesh.setBuffer(Type.TexCoord, 2, texcoords);
                }
            }
        }

        return geometry;
    }
}
