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

/**
 * This exception is thrown when a resource could not be found.
 *
 * @author Stan Hebben
 */
public class ResourceException extends RuntimeException {
    private String resource;
    private String file;

    /**
     * Constructs a new ResourceException which indicates that the specified
     * resource, used by the specified file, could not be found.
     *
     * @param file file which referenced the resource
     * @param resource resource name
     */
    public ResourceException(String file, String resource) {
        this.resource = resource;
        this.file = file;
    }

    /**
     * Returns the file that refers to the resource which could not be found.
     *
     * @return referencing file
     */
    public String getFile() {
        return file;
    }

    /**
     * Returns the resource which could not be found.
     *
     * @return the resource which could not be found
     */
    public String getResource() {
        return resource;
    }
}
