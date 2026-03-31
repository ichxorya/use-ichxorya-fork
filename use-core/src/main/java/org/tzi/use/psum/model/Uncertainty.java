package org.tzi.use.psum.model;

import org.tzi.use.psum.model.enums.UncertaintyKind;
import org.tzi.use.psum.model.enums.UncertaintyNature;
import org.tzi.use.psum.model.enums.ReducibilityLevel;
import org.tzi.use.psum.model.enums.UncertaintyPerspective;

/**
 * PSUM v1.0 §11.1
 * Represents a direct Uncertainty annotation (for measurement/objective uncertainty),
 * without needing a BeliefAgent.
 */
public class Uncertainty extends PSUMElement {
    
    private UncertaintyTopic subject;
    private IndeterminacySource source;
    
    private UncertaintyKind kind;
    private UncertaintyNature nature;
    private ReducibilityLevel reducibility;
    private UncertaintyPerspective perspective;

    public Uncertainty(String id) {
        super(id);
    }

    public UncertaintyTopic getSubject() { return subject; }
    public void setSubject(UncertaintyTopic subject) { this.subject = subject; }

    public IndeterminacySource getSource() { return source; }
    public void setSource(IndeterminacySource source) { this.source = source; }

    public UncertaintyKind getKind() { return kind; }
    public void setKind(UncertaintyKind kind) { this.kind = kind; }

    public UncertaintyNature getNature() { return nature; }
    public void setNature(UncertaintyNature nature) { this.nature = nature; }

    public ReducibilityLevel getReducibility() { return reducibility; }
    public void setReducibility(ReducibilityLevel reducibility) { this.reducibility = reducibility; }

    public UncertaintyPerspective getPerspective() { return perspective; }
    public void setPerspective(UncertaintyPerspective perspective) { this.perspective = perspective; }
}
