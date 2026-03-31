package org.tzi.use.psum.model;

/**
 * PSUM v1.0 §10.2
 * Represents an entity that holds a Belief or performs an assessment.
 */
public class BeliefAgent extends PSUMElement {
    private String type;

    public BeliefAgent(String id) {
        super(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
