package org.tzi.use.uml.ocl.type;

import java.util.HashSet;
import java.util.Set;

/**
 * The USE type for UBoolean (uncertain boolean with confidence level).
 * Conforms to OclAny.
 */
public class UBooleanType extends UncertainBooleanType {

    protected UBooleanType() {
        super("UBoolean");
    }

    @Override
    public boolean conformsTo(Type t) {
        return equals(t) || t.isTypeOfOclAny();
    }

    @Override
    public boolean isTypeOfUBoolean() {
        return true;
    }

    @Override
    public boolean isKindOfUBoolean(VoidHandling h) {
        return true;
    }

    @Override
    public Set<? extends Type> allSupertypes() {
        Set<Type> supertypes = new HashSet<>();
        supertypes.add(this);
        supertypes.add(TypeFactory.mkOclAny());
        return supertypes;
    }
}
