package be.stanhebben.xjmp;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.gde.core.assets.AssetData;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.jme3.gde.core.assets.SpatialAssetDataObject;
import com.jme3.scene.Spatial;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 * Converts a DirectX mesh file to a binary jME file.
 * 
 * @author Stan Hebben
 */
public class XToJme implements ActionListener {
    private final DataObject context;

    public XToJme(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (context != null) {
            final ProjectAssetManager manager = context.getLookup().lookup(ProjectAssetManager.class);
            if (manager == null) {
                throw new RuntimeException("No project asset manager!");
            }

            try {
                FileObject file = context.getPrimaryFile();
                Spatial model = ((SpatialAssetDataObject) context).loadAsset();

                //export model
                String outputPath = file.getParent().getPath() + File.separator + file.getName() + ".j3o";
                BinaryExporter exp = BinaryExporter.getInstance();

                File outFile = new File(outputPath);
                exp.save(model, outFile);
                //store original asset path interface properties
                DataObject targetModel = DataObject.find(FileUtil.toFileObject(outFile));
                AssetData properties = targetModel.getLookup().lookup(AssetData.class);
                if (properties != null) {
                    properties.loadProperties();
                    properties.setProperty("ORIGINAL_PATH", manager.getRelativeAssetPath(file.getPath()));
                    properties.saveProperties();
                }
                //update the tree
                context.getPrimaryFile().getParent().refresh();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
