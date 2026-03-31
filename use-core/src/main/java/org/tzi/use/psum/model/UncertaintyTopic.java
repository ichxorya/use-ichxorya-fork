package org.tzi.use.psum.model;

/**
 * PSUM v1.0 §10.5
 * Relates an uncertainty/belief to its true domain subject (e.g. an attribute in a USE model).
 */
public class UncertaintyTopic extends PSUMElement {
    private String targetModelElement; // The qualified name of the element in USE

    public UncertaintyTopic(String id) {
        super(id);
    }

    public String getTargetModelElement() {
        return targetModelElement;
    }

    public void setTargetModelElement(String targetModelElement) {
        this.targetModelElement = targetModelElement;
    }
}
