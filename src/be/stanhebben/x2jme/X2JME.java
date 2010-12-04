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
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.material.plugins.J3MLoader;
import com.jme3.system.JmeSystem;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.texture.plugins.DDSLoader;
import com.jme3.texture.plugins.TGALoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DirectX to J3O file converter. Can be used as command - line application.
 * 
 * @author Stan Hebben
 */
public class X2JME {
    private static AssetManager am;
    private static Arguments arguments;

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        arguments = new Arguments(args);
        if (!arguments.valid || arguments.help) {
            System.out.println("X2JME Beta 1 - DirectX .x to .j3o converter - by Stan Hebben");
            System.out.println("Usage:");
            System.out.println("  java -jar X2JME.jar <parameters>");
            System.out.println("Parameters:");
            System.out.println("  -help  show help");
            System.out.println("  -adir  set assets directory (default: assets)");
            System.out.println("  -file  set input file (default: entire assets directory)");
            System.out.println("  -dtex  set default texture (default: disabled)");
            System.out.println("Notes:");
            System.out.println("  XJME can be used to convert individual files or entire        ");
            System.out.println("  folders, depending if the source file is a file or folder. In ");
            System.out.println("  the case of a folder being supplied as source, all .x files   ");
            System.out.println("  in the folder and its subfolders will be converted. The file  ");
            System.out.println("  argument points to a folder or file within the assets         ");
            System.out.println("  directory.");
            System.out.println("");
            System.out.println("  If a texture is not found, XJME will try to find one. If there");
            System.out.println("  is a texture with the same name (but different extension) then");
            System.out.println("  that texture will be used as default. If there is no such     ");
            System.out.println("  texture, a global default texture will be used, if available. ");
            System.out.println("  The global default texture can be set with the -dtex option.  ");
            System.out.println("  If not global default texture is set, and no texture could be ");
            System.out.println("  found, an exception will be thrown.                           ");
            System.out.println("");
            System.out.println("  If you encounter a bug, please don't run away! This application");
            System.out.println("  is in beta. Please report any bugs at x2jme.sourceforge.net .  ");
            System.out.println("  Make sure you describe the issue and steps for reproduction,   ");
            System.out.println("  and attach any files that are relevant.                        ");
            System.out.println("");
            System.out.println("  This version does not support all .x file features. If you want");
            System.out.println("  new features added, ask me and I'll take a look at it.         ");

            return;
        }

        Logger.getAnonymousLogger().setLevel(Level.OFF);

        File assetDir = new File(arguments.assetDirectory);

        /* We need an assetManager to load textures and jme materials */
        am = JmeSystem.newAssetManager();
        am.registerLocator("", ClasspathLocator.class);
        am.registerLocator(arguments.assetDirectory, FileLocator.class);
        am.registerLoader(J3MLoader.class, "j3md");
        am.registerLoader(AWTLoader.class, "bmp");
        am.registerLoader(AWTLoader.class, "png");
        am.registerLoader(DDSLoader.class, "dds");
        am.registerLoader(TGALoader.class, "tga");
        
        File input;
        if (arguments.file == null) {
            input = assetDir;
        } else {
            input = new File(assetDir, arguments.file);
        }
        
        if (!input.exists()) {
            System.out.println("File not found: " + input);
        } else if (input.isFile()) {
            String adir;
            if (arguments.file == null) {
                adir = null;
            } else if (arguments.file.lastIndexOf('/') < 0) {
                adir = arguments.file;
            } else {
                adir = arguments.file.substring(0, arguments.file.lastIndexOf('/'));
            }
            convert(adir, input);
        } else if (input.isDirectory()) {
            convertDir(arguments.file, input);
        }
    }

    ////////////////////
    // Private methods
    ////////////////////

    private X2JME() {}
    
    /**
     * Converts an entire directory, and its subdirectories.
     *
     * @param adir the current directory (inside the assets directory)
     * @param dir the directory file
     */
    private static void convertDir(String adir, File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                convertDir(adir == null ? f.getName() : adir + "/" + f.getName(), f);
            } else if (f.isFile() && f.getName().toLowerCase().endsWith(".x")) {
                convert(adir, f);
            }
        }
    }

    /**
     * Converts an individual file.
     *
     * @param adir current directory (inside the asset directory)
     * @param input input file
     */
    private static void convert(String adir, final File input) {
        try {
            String currentFile = adir == null ? input.getName() : adir + "/" + input.getName();
            System.out.println("Converting " + currentFile);

            /* Read the x file, then save it to j3o using the BinaryExporter. */
            AssetKey key = new AssetKey(currentFile);
            AssetInfo info = new AssetInfo(am, key) {
                @Override
                public InputStream openStream() {
                    try {
                        return new FileInputStream(input);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(X2JME.class.getName()).log(Level.SEVERE, "Could not open file", ex);
                        return null;
                    }
                }
            };

            XFile file = new XFile(info, arguments.defaultTexture);
            Savable sv = file.toSavable();
            BinaryExporter exporter = BinaryExporter.getInstance();
            
            String outputName = input.getName().toLowerCase().endsWith(".x") ?
                input.getName().substring(0, input.getName().length() - 2) + ".j3o"
                : input.getName() + ".j3o";
            File output = new File(input.getParent(), outputName);
            exporter.save(sv, output);
        } catch (ResourceException ex) {
            System.out.println("Resource not found for " + ex.getFile() + ": " + ex.getResource());
        } catch (IOException ex) {
            Logger.getLogger(X2JME.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(X2JME.class.getName()).log(Level.SEVERE, "Conversion failed for " + input, ex);
        }
    }

    //////////////////////////
    // Private inner classes
    //////////////////////////

    /**
     * This class is used to process the command - line arguments.
     */
    private static class Arguments {
        private boolean valid = true;
        private boolean help = false;
        private String file = null;
        private String assetDirectory = "assets";
        private String defaultTexture = null;

        /**
         * Create an arguments set from the specified command - line arguments.
         *
         * @param arguments the arguments to process
         */
        public Arguments(String[] arguments) {
            int i = 0;
            while (i < arguments.length) {
                if (arguments[i].equals("-file")) {
                    // input file or directory
                    if (i + 1 == arguments.length) {
                        System.out.println("Missing value for -file");
                        valid = false;
                    } else {
                        file = arguments[i + 1];
                        i += 2;
                    }
                } else if (arguments[i].equals("-adir")) {
                    // asset directory
                    if (i + 1 == arguments.length) {
                        System.out.println("Missing value for -adir");
                        valid = false;
                    } else {
                        assetDirectory = arguments[i + 1];
                        i += 2;
                    }
                } else if (arguments[i].equals("-dtex")) {
                    // default texture
                    if (i + 1 == arguments.length) {
                        System.out.println("Missing value for -dtex");
                    } else {
                        defaultTexture = arguments[i + 1];
                        i += 2;
                    }
                } else if (arguments[i].equals("-help") || arguments[i].equals("-h") || arguments[i].equals("-?")) {
                    // display help
                    help = true;
                    i++;
                } else {
                    // unrecognized argument
                    System.out.println("Unknown argument: " + arguments[i]);
                    valid = false;
                    return;
                }
            }
        }
    }
}
