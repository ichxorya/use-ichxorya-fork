package org.tzi.use.psum.model;

import org.tzi.use.psum.model.enums.IndeterminacyNature;

/**
 * PSUM v1.0 §10.6
 * The source of indeterminacy making the topic uncertain.
 */
public class IndeterminacySource extends PSUMElement {
    private IndeterminacyNature nature;

    public IndeterminacySource(String id) {
        super(id);
    }

    public IndeterminacyNature getNature() {
        return nature;
    }

    public void setNature(IndeterminacyNature nature) {
        this.nature = nature;
    }
}
