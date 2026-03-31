/*
 * USE - UML based specification environment
 * PSUM Extension: Abstract base for uncertainty types.
 */
package org.tzi.use.uml.ocl.type;

/**
 * Abstract base class for uncertain types (UReal, UInteger, UBoolean, SBoolean).
 * Bridges BasicType with the uncertainty-specific type hierarchy.
 * 
 * @author Víctor Manuel Ortiz Guardeño (original), adapted for USE 7.5.0
 */
public abstract class UncertainType extends BasicType {

    protected UncertainType(String t) {
        super(t);
    }
}
