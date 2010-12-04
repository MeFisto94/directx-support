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
import be.stanhebben.x2jme.X2JME;
import be.stanhebben.x2jme.XFile;
import be.stanhebben.x2jme.template.XTMaterial;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture.WrapAxis;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;

/**
 * Contains material data. Material data contains parameters for a Phong shader.
 *
 * @author Stan Hebben
 */
public class XDMaterial extends XData {
    private ColorRGBA color;
    private float power;
    private ColorRGBA specular;
    private ColorRGBA emissive;
    private XData[] children;

    private Material material;

    /**
     * Constructs a new material. If the children contain a TextureFilename
     * object, then the material will be textured.
     *
     * @param file containing file
     * @param name material name
     * @param color material color (ambient and diffuse)
     * @param power specular power
     * @param specular specular color
     * @param emissive emissive color
     * @param children child nodes
     */
    public XDMaterial(
            XFile file,
            String name,
            ColorRGBA color,
            float power,
            ColorRGBA specular,
            ColorRGBA emissive,
            XData[] children) {
        super(file, name, XTMaterial.getInstance());

        this.color = color;
        this.power = power;
        this.specular = specular;
        this.emissive = emissive;
        this.children = children;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////

    /**
     * Converts this material to a JME Material definition. Uses the Lighting
     * shader in JME (Common/MatDefs/Light/Lighting.j3md) as shader.
     *
     * @return the converted material definition
     */
    @Override
    public Material toMaterial(AssetManager am) {
        if (material == null) {
            MaterialDef matDef = (MaterialDef) am.loadAsset("Common/MatDefs/Light/Lighting.j3md");
            material = new Material(matDef);
            material.setBoolean("m_VertexLighting", false);
            material.setBoolean("m_LowQuality", false);
            material.setBoolean("m_HighQuality", true);
            material.setBoolean("m_UseAlpha", true);
            material.setBoolean("m_UseMaterialColors", true);
            material.setColor("m_Ambient", color);
            material.setColor("m_Diffuse", color);
            material.setColor("m_Specular", specular);
            material.setFloat("m_Shininess", power);

            for (XData child : children) {
                if (child.toTexture() != null) {
                    String located = getFile().locateTexture(child.toTexture());
                    Texture2D texture = (Texture2D) am.loadTexture(located);
                    if (texture == null) {
                        System.out.println("Texture not found: " + child.toTexture());
                    } else {
                        texture.setWrap(WrapAxis.S, WrapMode.Repeat);
                        texture.setWrap(WrapAxis.T, WrapMode.Repeat);
                        texture.setName(child.toTexture());
                        System.out.println("Loaded texture: " + located + " (" + child.toTexture() + ")");
                        //texture.setName(child.toTexture());
                        material.setTexture("m_DiffuseMap", texture);
                    }
                } else {
                    System.out.println("Warning: unsupported child in Material: " + child.getTemplate().getName());
                }
            }
        }
        
        return material;
    }
}
