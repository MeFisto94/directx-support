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

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.IOException;

/**
 * AssetLoader which can be register to an AssetManager, so that DirectX files
 * can be loaded.
 * 
 * @author Stan Hebben
 */
public class XImporter implements AssetLoader {
    public XImporter() {}

    /**
     * Loads a DirectX model.
     *
     * @param assetInfo asset info to reference the model
     * @return the loaded model
     * @throws IOException if the file could not be read properly
     */
    public Object load(AssetInfo assetInfo) throws IOException {
        XFile xfile = new XFile(assetInfo);
        return xfile.toNode();
    }
}
