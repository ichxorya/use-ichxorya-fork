package org.tzi.use.psum.model;

import org.tzi.use.psum.model.enums.UncertaintyKind;
import org.tzi.use.psum.model.enums.UncertaintyNature;
import org.tzi.use.psum.model.enums.ReducibilityLevel;
import org.tzi.use.psum.model.enums.UncertaintyPerspective;

/**
 * PSUM v1.0 §10.1 & §11.1
 * Represents a Subjective/Objective belief about an uncertainty topic.
 */
public class Belief extends PSUMElement {
    
    // Belief properties
    private BeliefAgent agent;
    private UncertaintyTopic subject;
    
    // Core Uncertainty Characteristics inherited directly
    private UncertaintyKind kind;
    private UncertaintyNature nature;
    private ReducibilityLevel reducibility;
    private UncertaintyPerspective perspective;

    public Belief(String id) {
        super(id);
    }

    public BeliefAgent getAgent() { return agent; }
    public void setAgent(BeliefAgent agent) { this.agent = agent; }

    public UncertaintyTopic getSubject() { return subject; }
    public void setSubject(UncertaintyTopic subject) { this.subject = subject; }

    public UncertaintyKind getKind() { return kind; }
    public void setKind(UncertaintyKind kind) { this.kind = kind; }

    public UncertaintyNature getNature() { return nature; }
    public void setNature(UncertaintyNature nature) { this.nature = nature; }

    public ReducibilityLevel getReducibility() { return reducibility; }
    public void setReducibility(ReducibilityLevel reducibility) { this.reducibility = reducibility; }

    public UncertaintyPerspective getPerspective() { return perspective; }
    public void setPerspective(UncertaintyPerspective perspective) { this.perspective = perspective; }
}
