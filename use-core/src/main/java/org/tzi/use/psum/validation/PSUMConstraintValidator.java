package org.tzi.use.psum.validation;

import org.tzi.use.psum.model.Belief;
import org.tzi.use.psum.model.PSUMElement;
import org.tzi.use.psum.model.PSUMModel;
import org.tzi.use.psum.model.Uncertainty;
import org.tzi.use.psum.model.enums.ReducibilityLevel;
import org.tzi.use.psum.model.enums.UncertaintyNature;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates PSUM specific constraints.
 */
public class PSUMConstraintValidator {

    public static class ValidationIssue {
        private String elementId;
        private String message;

        public ValidationIssue(String elementId, String message) {
            this.elementId = elementId;
            this.message = message;
        }

        @Override
        public String toString() {
            return "[" + elementId + "] " + message;
        }
    }

    public List<ValidationIssue> validate(PSUMModel model) {
        List<ValidationIssue> issues = new ArrayList<>();

        for (PSUMElement element : model.getElements()) {
            if (element instanceof Uncertainty) {
                validateUncertainty((Uncertainty) element, issues);
            } else if (element instanceof Belief) {
                validateBelief((Belief) element, issues);
            }
        }

        return issues;
    }

    private void validateUncertainty(Uncertainty u, List<ValidationIssue> issues) {
        if (u.getNature() == UncertaintyNature.ALEATORY && u.getReducibility() != ReducibilityLevel.IRREDUCIBLE) {
            issues.add(new ValidationIssue(u.getId(), "Aleatory uncertainty must be Irreducible (PSUM §11.1)."));
        }
        if (u.getNature() == UncertaintyNature.EPISTEMIC && u.getReducibility() == ReducibilityLevel.IRREDUCIBLE) {
            issues.add(new ValidationIssue(u.getId(), "Epistemic uncertainty must be Reducible (Fully or Partially) (PSUM §11.1)."));
        }
    }

    private void validateBelief(Belief b, List<ValidationIssue> issues) {
        if (b.getNature() == UncertaintyNature.ALEATORY && b.getReducibility() != ReducibilityLevel.IRREDUCIBLE) {
            issues.add(new ValidationIssue(b.getId(), "Aleatory belief must be Irreducible (PSUM §11.1)."));
        }
        if (b.getNature() == UncertaintyNature.EPISTEMIC && b.getReducibility() == ReducibilityLevel.IRREDUCIBLE) {
            issues.add(new ValidationIssue(b.getId(), "Epistemic belief must be Reducible (Fully or Partially) (PSUM §11.1)."));
        }
        if (b.getAgent() == null) {
            issues.add(new ValidationIssue(b.getId(), "Belief must have a declared BeliefAgent source."));
        }
    }
}
