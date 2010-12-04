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
import be.stanhebben.x2jme.template.XTTextureFilename;

/**
 * Contains a texture filename. Instantiates the TextureFilename template.
 *
 * @author Stan Hebben
 */
public class XDTextureFilename extends XData {
    private String filename;

    /**
     * Constructs a new texture filename instance.
     *
     * @param file containing file
     * @param filename texture file name
     */
    public XDTextureFilename(XFile file, String filename) {
        super(file, null, XTTextureFilename.getInstance());

        this.filename = filename;
    }

    /////////////////////////
    // XData implementation
    /////////////////////////

    @Override
    public String toTexture() {
        return filename;
    }
}
