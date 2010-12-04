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

import com.jme3.math.Quaternion;
import com.jme3.math.Transform;


/**
 * Contains utility methods.
 *
 * @author Stan Hebben
 */
public class XUtil {
    /**
     * Converts a linear transformation matrix to a jme transform.
     *
     * @param matrix4x4 transformation matrix
     * @return converted transformation
     */
    public static Transform matrixToTransform(float[] matrix4x4) {
        // matrix4x4 = Scale*Rotate*Translate
        double _11 = matrix4x4[0];
        double _12 = matrix4x4[1];
        double _13 = matrix4x4[2];
        double _21 = matrix4x4[4];
        double _22 = matrix4x4[5];
        double _23 = matrix4x4[6];
        double _31 = matrix4x4[8];
        double _32 = matrix4x4[9];
        double _33 = matrix4x4[10];
        double _41 = matrix4x4[12];
        double _42 = matrix4x4[13];
        double _43 = matrix4x4[14];

        /* Extract scaling factors */
        double sx = Math.sqrt(_11 * _11 + _21 * _21 + _31 * _31);
        double sy = Math.sqrt(_12 * _12 + _22 * _22 + _32 * _32);
        double sz = Math.sqrt(_13 * _13 + _23 * _23 + _33 * _33);

        /* Determine winding. Winding is determing by vector multiplying the X
         * and Y vector, and checking if it has the same direction as the Z vector.
         * If not, winding must be reversed, which is done by inverting the z axis.
         */
        double winding = (_21 *  _23 - _31 * _22) * _13
                + (_31 * _12 - _11 * _32) * _23
                + (_11 * _22 - _21 * _12) * _33;
        if (winding < 0) sz = -sz;

        /* Divide the matrix elements by the scale so that rotation can be calculated */
        _11 /= sx;
        _12 /= sy;
        _13 /= sz;
        _21 /= sx;
        _22 /= sy;
        _23 /= sz;
        _31 /= sx;
        _32 /= sy;
        _33 /= sz;

        /* Convert the rotation matrix to a quaternion */
        // TODO: test rotations rigorously, as this is the most complex part of decomposition
        /* Choose the largest diagonal element (necessary to guarantee stability) */
        double max = Math.max(Math.max(Math.abs(_11), Math.abs(_22)), Math.abs(_33));

        Quaternion rotation;
        if (max == Math.abs(_11)) {
            // u=x, v=y, w=z
            double r = 0;
            if (1.0 + _11 - _22 - _33 > 0) {
                r = Math.sqrt(1.0 + _11 - _22 - _33);
            }
            if (r < 0.0001) {
                rotation = new Quaternion();
            } else {
                double q0 = (_32 - _23) / (2.0 * r);
                double q1 = r * 0.5;
                double q2 = (_12 + _21) / (2.0 * r);
                double q3 = (_31 + _13) / (2.0 * r);
                rotation = new Quaternion((float)q0, (float)q1, (float)q2, (float)q3);
            }
        } else if (max == Math.abs(_22)) {
            // u=y, v=z, w=x
            double r = 0;
            if (1.0 + _22 - _11 - _33 > 0) {
                r = Math.sqrt(1.0 + _22 - _11 - _33);
            }
            if (r < 0.0001) {
                rotation = new Quaternion();
            } else {
                double q0 = (_13 - _31) / (2.0 * r);
                double q2 = r * 0.5;
                double q3 = (_23 + _32) / (2.0 * r);
                double q1 = (_12 + _21) / (2.0 * r);
                rotation = new Quaternion((float)q0, (float)q1, (float)q2, (float)q3);
            }
        } else {
            // u=z, v=x, w=y
            double r = 0;
            if (1.0 + _33 - _11 - _22 > 0) {
                r = Math.sqrt(1.0 + _33 - _11 - _22);
            } 
            if (r < 0.0001) {
                rotation = new Quaternion();
            } else {
                double q0 = (_21 - _12) / (2.0 * r);
                double q3 = r * 0.5;
                double q1 = (_31 + _13) / (2.0 * r);
                double q2 = (_23 + _32) / (2.0 * r);
                rotation = new Quaternion((float)q0, (float)q1, (float)q2, (float)q3);
            }
        }

        /* Extract the translation */
        double tx = _41;
        double ty = _42;
        double tz = _43;

        /* Assemble the extracted data into a transformation */
        Transform result = new Transform();
        result.setTranslation((float)tx, (float)ty, (float)tz);
        result.setRotation(rotation);
        result.setScale((float)sx, (float)sy, (float)sz);
        return result;
    }

    ////////////////////
    // Private methods
    ////////////////////

    private XUtil() {}
}
