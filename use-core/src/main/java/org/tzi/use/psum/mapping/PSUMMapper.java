package org.tzi.use.psum.mapping;

import org.tzi.use.psum.model.Belief;
import org.tzi.use.psum.model.PSUMElement;
import org.tzi.use.psum.model.Uncertainty;
import org.tzi.use.psum.model.enums.UncertaintyKind;
import org.tzi.use.psum.model.enums.UncertaintyNature;
import org.tzi.use.psum.model.enums.UncertaintyPerspective;
import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.uml.ocl.type.TypeFactory;

/**
 * Maps PSUM Domain Elements to underlying OCL Types (UReal, UBoolean, SBoolean, UInteger)
 * according to the predefined semantic mapping rules.
 */
public class PSUMMapper {

    /**
     * Maps a given PSUM Metadata block (Uncertainty or Belief) into
     * the mathematically executable USE Type.
     * 
     * @param element PSUM Uncertainty or Belief
     * @return Underlying OCL Type
     */
    public Type mapToType(PSUMElement element) {
        if (element instanceof Uncertainty) {
            return mapUncertainty((Uncertainty) element);
        } else if (element instanceof Belief) {
            return mapBelief((Belief) element);
        }
        return null; // CRISP
    }

    private Type mapUncertainty(Uncertainty u) {
        if (u.getKind() == UncertaintyKind.MEASUREMENT && u.getNature() == UncertaintyNature.ALEATORY) {
            return TypeFactory.mkUReal();
        }
        
        if (u.getKind() == UncertaintyKind.ENVIRONMENT && u.getNature() == UncertaintyNature.ALEATORY) {
            return TypeFactory.mkUReal();
        }
        
        if (u.getKind() == UncertaintyKind.OCCURRENCE) {
            return TypeFactory.mkUBoolean();
        }
        
        // Default fallback for general objective likelihood
        return TypeFactory.mkUBoolean();
    }

    private Type mapBelief(Belief b) {
        if (b.getKind() == UncertaintyKind.CONTENT && b.getPerspective() == UncertaintyPerspective.SUBJECTIVE) {
            return TypeFactory.mkSBoolean(); // Subjective Logic
        }
        
        if (b.getKind() == UncertaintyKind.CONTENT && b.getPerspective() == UncertaintyPerspective.OBJECTIVE) {
            return TypeFactory.mkUBoolean(); // Objective probability
        }
        
        return TypeFactory.mkSBoolean();
    }
}
