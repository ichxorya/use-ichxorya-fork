package org.tzi.use.uml.ocl.type;

import java.util.HashSet;
import java.util.Set;

/**
 * The USE type for UInteger (uncertain integer with standard uncertainty).
 * Conforms to OclAny. isKindOfNumber = true.
 */
public class UIntegerType extends UncertainType {

    protected UIntegerType() {
        super("UInteger");
    }

    @Override
    public boolean conformsTo(Type t) {
        return equals(t) || t.isTypeOfOclAny();
    }

    @Override
    public boolean isTypeOfUInteger() {
        return true;
    }

    @Override
    public boolean isKindOfUInteger(VoidHandling h) {
        return true;
    }

    @Override
    public boolean isKindOfNumber(VoidHandling h) {
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
