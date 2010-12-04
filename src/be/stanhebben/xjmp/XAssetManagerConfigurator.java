package be.stanhebben.xjmp;

import com.jme3.asset.AssetManager;
import com.jme3.gde.core.assets.AssetManagerConfigurator;

/**
 *
 * @author Stan Hebben
 */
@org.openide.util.lookup.ServiceProvider(service = AssetManagerConfigurator.class)
public class XAssetManagerConfigurator implements AssetManagerConfigurator {
    @Override
    public void prepareManager(AssetManager manager) {
        manager.registerLoader(be.stanhebben.x2jme.XImporter.class, "x");
    }
}
