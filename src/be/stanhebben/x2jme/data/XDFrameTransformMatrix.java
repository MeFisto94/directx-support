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
import be.stanhebben.x2jme.XUtil;
import be.stanhebben.x2jme.template.XTFrameTransformMatrix;
import com.jme3.math.Transform;

/**
 * Contains frame transformation data. Instantiates the FrameTransformMatrix
 * template.
 *
 * @author Stan Hebben
 */
public class XDFrameTransformMatrix extends XData {
    private float[] value;

    /**
     * Constructs a new frame transformation matrix.
     *
     * @param file containing file
     * @param name item name
     * @param value 4x4 transformation matrix
     */
    public XDFrameTransformMatrix(XFile file, String name, float[] value) {
        super(file, name, XTFrameTransformMatrix.getInstance());

        this.value = value;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////

    /**
     * Converts this data item to a JME Transform. Only works with
     * Scale-Rotate-Translate transformations, thus all transformations in
     * directx files are assumed to be SRT transformations.
     *
     * @return the converted transformation
     */
    @Override
    public Transform toTransform() {
        return XUtil.matrixToTransform(value);
    }
}
