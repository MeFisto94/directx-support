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
import be.stanhebben.x2jme.template.XTFrame;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Contains Frame Data. Instantiates Frame templates.
 *
 * @author Stan Hebben
 */
public class XDFrame extends XData {
    private XData[] children;

    private Node spatial;

    /**
     * Creates a new Frame with the specified children.
     *
     * @param file containing file
     * @param name frame name
     * @param children frame children
     */
    public XDFrame(XFile file, String name, XData[] children) {
        super(file, name, XTFrame.getInstance());

        this.children = children;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////

    /**
     * Converts this Frame to a Spatial object. The returned Spatial is a node
     * which contains all children spatials. If there is a FrameTransformMatrix
     * node, then it will be converted to a JME Transform and applied to this
     * node.
     *
     * @param am AssetManager used to load textures and materials
     * @return a Node instance
     */
    @Override
    public Spatial toSpatial(AssetManager am) {
        if (spatial == null) {
            spatial = new Node();

            for (XData data : children) {
                String tname = data.getTemplate().getName();
                if (data.toSpatial(am) != null) {
                    spatial.attachChild(data.toSpatial(am));
                } else if (data.toTransform() != null) {
                    spatial.setLocalTransform(data.toTransform());
                } else {
                    System.out.println("Warning: unsupported child node in Frame: " + tname);
                }
            }
        }

        return spatial;
    }
}
