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

import java.util.HashMap;

/**
 * A namespace contains data items.
 *
 * @author Stan Hebben
 */
public class XNameSpace {
    private XFile file;
    private HashMap<String, XData> data;

    /**
     * Creates a new namespace.
     *
     * @param file containing file
     */
    public XNameSpace(XFile file) {
        this.file = file;
        data = new HashMap<String, XData>();
    }

    /**
     * Returns the file of this namespace.
     *
     * @return namespace file
     */
    public XFile getFile() {
        return file;
    }

    /**
     * Retrieves a data item by name.
     *
     * @param name data item name
     * @return data item
     */
    public XData get(String name) {
        return data.get(name);
    }

    /**
     * Checks if this namespace contains the specified item.
     *
     * @param name item name
     * @return true if this namespace contains this item
     */
    public boolean has(String name) {
        return data.containsKey(name);
    }

    /**
     * Adds a value item. If the item has no name set, nothing will happen.
     *
     * @param item data item to add
     */
    public void add(XData item) {
        if (item.getName() != null) {
            data.put(item.getName(), item);
        }
    }
}
