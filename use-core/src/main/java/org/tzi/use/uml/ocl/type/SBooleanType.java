package org.tzi.use.uml.ocl.type;

import java.util.HashSet;
import java.util.Set;

/**
 * The USE type for SBoolean (subjective boolean / binomial opinion from Subjective Logic).
 * Conforms to UBooleanType and transitively to OclAny.
 */
public class SBooleanType extends UncertainBooleanType {

    protected SBooleanType() {
        super("SBoolean");
    }

    @Override
    public boolean conformsTo(Type t) {
        return equals(t) || t.isKindOfUBoolean(VoidHandling.EXCLUDE_VOID) || t.isTypeOfOclAny();
    }

    @Override
    public boolean isTypeOfSBoolean() {
        return true;
    }

    @Override
    public boolean isKindOfSBoolean(VoidHandling h) {
        return true;
    }

    @Override
    public boolean isKindOfUBoolean(VoidHandling h) {
        return true; // SBoolean is-a-kind-of UBoolean
    }

    @Override
    public Set<? extends Type> allSupertypes() {
        Set<Type> supertypes = new HashSet<>();
        supertypes.add(this);
        supertypes.add(TypeFactory.mkUBoolean());
        supertypes.add(TypeFactory.mkOclAny());
        return supertypes;
    }
}
