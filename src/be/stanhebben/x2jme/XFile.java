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

import be.stanhebben.x2jme.binary.XBinaryTokenStream;
import be.stanhebben.x2jme.text.XTextTokenStream;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.export.Savable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Used to read .x files.
 * 
 * @author Stan Hebben
 */
public class XFile {
    private AssetInfo info;
    private XData[] data;
    private String defaultTexture1;
    private String defaultTexture2;

    /**
     * Opens and parsers an X file. Uses the specified texture if no other
     * texture could be found.
     *
     * @param info asset info
     * @param defaultTexture default texture
     * @throws IOException if the file could not be read properly
     */
    public XFile(AssetInfo info, String defaultTexture) throws IOException {
        this(info);
        defaultTexture2 = defaultTexture;
    }

    /**
     * Opens and parses an X file.
     *
     * @param info asset info
     * @throws IOException if the file could not be read properly
     */
    public XFile(AssetInfo info) throws IOException {
        this.info = info;

        String name = info.getKey().getName();
        defaultTexture1 = name.toLowerCase().endsWith(".x") ? name.substring(0, name.length() - 2) : name;

        InputStream in = new BufferedInputStream(info.openStream());
        /* Process the header */
        DataInputStream din = new DataInputStream(in);
        byte[] header = new byte[16];
        din.readFully(header);

        if (header[0] != 'x' || header[1] != 'o' || header[2] != 'f' || header[3] != ' ') {
            throw new IllegalArgumentException("Input file is not an x file");
        }
        if (header[4] != '0' || header[5] != '3' || header[6] != '0' || (header[7] != '2' && header[7] != '3')) {
            throw new IllegalArgumentException("Unsupported x file version");
        }
        boolean binary;
        if (header[8] == 't' && header[9] == 'x' && header[10] == 't' && header[11] == ' ') {
            binary = false;
        } else if (header[8] == 'b' && header[9] == 'i' && header[10] == 'n' && header[11] == ' ') {
            binary = true;
        } else {
            throw new XParserException("Unknown x file format");
        }
        boolean float64;
        if (header[12] == '0' && header[13] == '0' && header[14] == '6' && header[15] == '4') {
            float64 = true;
        } else if (header[12] == '0' && header[13] == '0' && header[14] == '3' && header[15] == '2') {
            float64 = false;
        } else {
            throw new XParserException("Invalid float size");
        }

        if (binary) {
            /* Parse the data in binary mode */
            XBinaryTokenStream tokens = new XBinaryTokenStream(in, float64);

            XNameSpace ns = new XNameSpace(this);
            List<XData> dataList = new LinkedList<XData>();
            do {
                if (XTemplate.skip(tokens)) continue; // skip templates
                XData xdata = XData.accept(ns, tokens); // try to read the current data as item
                if (xdata != null) {
                    dataList.add(xdata);
                    continue;
                }
            } while (tokens.hasMore());
            data = dataList.toArray(new XData[dataList.size()]);
        } else {
            /* Parse the data in text mode */
            XTextTokenStream tokens = new XTextTokenStream(in);

            XNameSpace ns = new XNameSpace(this);
            List<XData> dataList = new LinkedList<XData>();
            do {
                if (!tokens.hasMore()) break;
                if (XTemplate.skip(tokens)) continue; // skip templates
                XData xdata = XData.accept(ns, tokens); // try to read the current data as item
                if (xdata != null) {
                    dataList.add(xdata);
                    continue;
                }
            } while (true);
            data = dataList.toArray(new XData[dataList.size()]);
        }
    }

    /**
     * Converts the .x file to a savable.
     * 
     * @return this file as savable
     */
    public Savable toSavable() {
        return toNode();
    }

    /**
     * Converts the .x file to a Node.
     *
     * @return this file as Node
     */
    public Node toNode() {
        Node output = new Node();
        for (XData d : data) {
            Spatial value = d.toSpatial(info.getManager());
            if (value != null) output.attachChild(value);
        }
        return output;
    }

    /**
     * Locates the specified texture. It searches for the file in the same directory
     * as the currently processed .x file, then the asset root, and then it
     * looks for default textures.
     *
     * @param name texture name
     * @return located texture
     */
    public String locateTexture(String name) {
        String ext = name.substring(name.lastIndexOf('.') + 1);

        String folder = info.getKey().getFolder();
        if (folder.length() > 0 && info.getManager().locateAsset(new AssetKey(folder + name)) != null) {
            return folder + name;
        } else if (info.getManager().locateAsset(new AssetKey(name)) != null) {
            return name;
        } else if (info.getManager().locateAsset(new AssetKey(defaultTexture1 + "." + ext)) != null) {
            return defaultTexture1 + "." + ext;
        } else {
            if (defaultTexture2 == null) throw new ResourceException(info.getKey().getName(), name);
            return defaultTexture2;
        }
    }
}
