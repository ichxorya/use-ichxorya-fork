/*
 * USE - UML based specification environment
 * PSUM Extension: Abstract base for uncertain boolean types (UBoolean, SBoolean).
 */
package org.tzi.use.uml.ocl.type;

/**
 * Abstract base class for uncertain boolean types.
 * Both UBooleanType and SBooleanType extend this.
 */
public abstract class UncertainBooleanType extends UncertainType {

    protected UncertainBooleanType(String t) {
        super(t);
    }
}
