package org.tzi.use.psum.model;

/**
 * PSUM v1.0 §9.1
 * Base element for all PSUM concepts providing common identification.
 */
public abstract class PSUMElement {
    private String id;
    private String description;

    public PSUMElement(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
