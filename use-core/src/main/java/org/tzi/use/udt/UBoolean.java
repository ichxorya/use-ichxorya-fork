/*
 * Uncertainty Datatypes Library - Ported from atenearesearchgroup/uncertainty
 * Original: uDataTypes/Libraries/Java/src/uDataTypes/UBoolean.java
 *
 * Implements UBoolean (Uncertain Boolean) - a boolean value with a confidence level.
 * UBoolean(b, c) represents a boolean b with confidence c ∈ [0,1].
 * Internally always kept in canonical form: (true, c) where c = P(true).
 *
 * @author Antonio Vallecillo (original), adapted for USE 7.5.0
 */
package org.tzi.use.udt;

public class UBoolean implements Cloneable, Comparable<UBoolean> {

    protected boolean b = true;
    protected double c = 0.0;

    /**
     * Canonical form: (b=true, c=confidence_that_true).
     * If b=false, we flip to (true, 1-c).
     */
    private void setNormalForm() {
        if (!b) { b = true; c = 1 - c; }
    }

    // Constructors
    public UBoolean() {
        this.b = true;
        this.c = 0.0;
    }

    public UBoolean(boolean b) {
        this.b = b;
        this.c = 1.0;
        setNormalForm();
    }

    public UBoolean(double c) {
        if ((c < 0.0) || (c > 1.0)) throw new IllegalArgumentException("Invalid parameters");
        this.b = true;
        this.c = c;
        setNormalForm();
    }

    public UBoolean(boolean b, double c) {
        if ((c < 0.0) || (c > 1.0)) throw new IllegalArgumentException("Invalid parameters");
        this.b = b;
        this.c = c;
        setNormalForm();
    }

    public UBoolean(String b) {
        this.b = Boolean.parseBoolean(b);
        this.c = 1.0;
        setNormalForm();
    }

    public UBoolean(String b, String c) {
        this.b = Boolean.parseBoolean(b);
        this.c = Double.parseDouble(c);
        if ((this.c < 0.0) || (this.c > 1.0)) throw new IllegalArgumentException("Invalid parameters");
        setNormalForm();
    }

    // Getters
    public boolean getB() {
        setNormalForm();
        return b;
    }

    public double getC() {
        setNormalForm();
        return c;
    }

    // ===== Type Operations =====

    public UBoolean not() {
        return new UBoolean(!this.getB(), this.getC());
    }

    public UBoolean and(UBoolean b) {
        if (this == b) return new UBoolean(this.b & b.b, this.c);
        return new UBoolean(this.b & b.b, this.c * b.c);
    }

    public UBoolean or(UBoolean b) {
        if (this == b) return new UBoolean(this.b | b.b, this.c);
        return new UBoolean(this.b | b.b, this.c + b.c - (this.c * b.c));
    }

    public UBoolean implies(UBoolean b) {
        if (this == b) return new UBoolean((!this.b) | b.b, this.c);
        return new UBoolean((!this.b) | b.b, (1 - this.c) + b.c - ((1 - this.c) * b.c));
    }

    public UBoolean equivalent(UBoolean b) {
        return this.xor(b).not();
    }

    public UBoolean xor(UBoolean b) {
        return new UBoolean(true, java.lang.Math.abs(this.getC() - b.getC()));
    }

    public UBoolean uEquals(UBoolean b) {
        return this.equivalent(b);
    }

    // ===== Comparison operations =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UBoolean uBoolean = (UBoolean) o;
        if (getB() != uBoolean.getB()) return false;
        return Math.abs(uBoolean.getC() - getC()) < 0.001D;
    }

    public boolean distinct(UBoolean b) {
        return !this.equals(b);
    }

    public boolean equalsC(UBoolean b, double confidence) {
        return java.lang.Math.abs(this.getC() - b.getC()) <= (1 - confidence);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (getB() ? 1 : 0);
        temp = Double.doubleToLongBits(getC());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // ===== Conversions =====

    public String toString() {
        boolean val = this.getB();
        double conf = this.getC();
        if (conf < 0.5) {
            val = !val;
            conf = 1 - conf;
        }
        return String.format("UBoolean(%b, %5.3f)", val, conf);
    }

    public boolean toBoolean() {
        return (c >= 0.5);
    }

    @Override
    public int compareTo(UBoolean other) {
        double x = (this.getC() - other.getC());
        if (Math.abs(x) < 0.001D) return 0;
        if (x < 0) return -1;
        return 1;
    }

    @Override
    public UBoolean clone() {
        return new UBoolean(this.getB(), this.getC());
    }
}
