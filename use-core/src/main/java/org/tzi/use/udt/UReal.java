/*
 * Uncertainty Datatypes Library - Ported from atenearesearchgroup/uncertainty
 * Original: uDataTypes/Libraries/Java/src/uDataTypes/UReal.java
 *
 * Implements UReal (Uncertain Real) following the GUM standard for
 * measurement uncertainty propagation.
 * UReal(x, u) represents a value x with standard uncertainty u.
 *
 * @author Antonio Vallecillo (original), adapted for USE 7.5.0
 */
package org.tzi.use.udt;

class Result {
    double lt;
    double eq;
    double gt;

    Result() {
        this.lt = 0.0;
        this.eq = 1.0;
        this.gt = 0.0;
    }

    Result(double l, double e, double g) {
        this.lt = l;
        this.eq = e;
        this.gt = g;
    }

    Result check(boolean swap) {
        if (!swap) return this;
        return new Result(this.gt, this.eq, this.lt);
    }
}

public class UReal implements Cloneable, Comparable<UReal> {

    protected double x = 0.0;
    protected double u = 0.0;

    // Constructors
    public UReal() {
        this.x = 0.0;
        this.u = 0.0;
    }

    public UReal(double x) {
        this.x = x;
        this.u = 0.0;
    }

    public UReal(double x, double u) {
        this.x = x;
        this.u = Math.abs(u);
    }

    public UReal(String x) {
        this.x = Double.parseDouble(x);
        this.u = 0.0;
    }

    public UReal(String x, String u) {
        this.x = Double.parseDouble(x);
        this.u = Math.abs(Double.parseDouble(u));
    }

    // Getters and setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getU() { return u; }
    public void setU(double u) { this.u = Math.abs(u); }

    // ===== Type Operations (GUM uncertainty propagation) =====

    public UReal add(UReal r) {
        UReal result = new UReal();
        result.setX(this.getX() + r.getX());
        result.setU(Math.sqrt((this.getU() * this.getU()) + (r.getU() * r.getU())));
        return result;
    }

    public UReal minus(UReal r) {
        UReal result = new UReal();
        result.setX(this.getX() - r.getX());
        if (r == this) result.setU(0.0);
        else result.setU(Math.sqrt((this.getU() * this.getU()) + (r.getU() * r.getU())));
        return result;
    }

    public UReal mult(UReal r) {
        UReal result = new UReal();
        result.setX(this.getX() * r.getX());
        double a = r.getX() * r.getX() * this.getU() * this.getU();
        double b = this.getX() * this.getX() * r.getU() * r.getU();
        result.setU(Math.sqrt(a + b));
        return result;
    }

    public UReal divideBy(UReal r) {
        UReal result = new UReal();
        if (r == this) {
            result.setX(1.0);
            result.setU(0.0);
            return result;
        }
        if (r.getU() == 0.0) {
            result.setX(this.getX() / r.getX());
            result.setU(this.getU() / r.getX());
            return result;
        }
        if (this.getU() == 0.0) {
            result.setX(this.getX() / r.getX());
            result.setU(r.getU() / (r.getX() * r.getX()));
            return result;
        }
        double a = this.getX() / r.getX();
        result.setX(a);
        double c = ((this.getU() * this.getU()) / Math.abs(r.getX()));
        double d = (this.getX() * this.getX() * r.getU() * r.getU()) / (r.getX() * r.getX() * r.getX() * r.getX());
        result.setU(Math.sqrt(c + d));
        return result;
    }

    public UReal abs() {
        return new UReal(Math.abs(this.getX()), this.getU());
    }

    public UReal neg() {
        return new UReal(-this.getX(), this.getU());
    }

    public UReal power(float s) {
        UReal result = new UReal();
        double a = Math.pow(this.getX(), s);
        result.setX(a);
        double c = s * this.getU() * (Math.pow(this.getX(), s - 1));
        result.setU(c);
        return result;
    }

    public UReal sqrt() {
        UReal result = new UReal();
        if (this.getX() == 0.0 && this.getU() == 0.0) {
            result.setX(0.0);
            result.setU(0.0);
        } else {
            double a = Math.sqrt(this.getX());
            double c = (this.getU()) / (2 * Math.sqrt(this.getX()));
            result.setX(a);
            result.setU(c);
        }
        return result;
    }

    public UReal sin() {
        return new UReal(Math.sin(this.getX()), this.getU() * Math.cos(this.getX()));
    }

    public UReal cos() {
        return new UReal(Math.cos(this.getX()), this.getU() * Math.sin(this.getX()));
    }

    public UReal tan() {
        return this.sin().divideBy(this.cos());
    }

    public UReal atan() {
        return new UReal(Math.atan(this.getX()), this.getU() / (1 + this.getX() * this.getX()));
    }

    public UReal acos() {
        UReal result = new UReal();
        result.setX(Math.acos(this.getX()));
        if (Math.abs(this.getX()) != 1.0)
            result.setU(this.getU() / Math.sqrt((1 - this.getX() * this.getX())));
        else result.setU(this.getU());
        return result;
    }

    public UReal asin() {
        UReal result = new UReal();
        result.setX(Math.asin(this.getX()));
        if (Math.abs(this.getX()) != 1.0)
            result.setU(this.getU() / Math.sqrt((1 - this.getX() * this.getX())));
        else result.setU(this.getU());
        return result;
    }

    public UReal inverse() {
        return new UReal(1.0, 0.0).divideBy(this);
    }

    public UReal floor() {
        return new UReal(Math.floor(this.getX()), this.getU());
    }

    public UReal round() {
        return new UReal(Math.round(this.getX()), this.getU());
    }

    public UReal min(UReal r) {
        if (r.lt(this).toBoolean()) return new UReal(r.getX(), r.getU());
        return new UReal(this.getX(), this.getU());
    }

    public UReal max(UReal r) {
        if (r.gt(this).toBoolean()) return new UReal(r.getX(), r.getU());
        return new UReal(this.getX(), this.getU());
    }

    // ===== Comparison operations =====

    public boolean equals(UReal number) {
        if (this == number) return true;
        double s1 = this.getU();
        double s2 = number.getU();
        if ((s1 == 0) || (s2 == 0)) return this.getX() == number.getX();
        double r = (s1 * s1) / (s2 * s2);
        double S = Math.sqrt(-2.0 + 3 * r + 3 * r * r - 2 * r * r * r + 2 * Math.pow(1 - r + r * r, 1.5)) / (Math.sqrt(r) * (1 + Math.sqrt(r)));
        if (Double.isNaN(S)) return (this.getX() == number.getX());
        double separation = S * (s1 + s2);
        return Math.abs(number.getX() - this.getX()) <= separation;
    }

    public boolean distinct(UReal r) {
        return !(this.equals(r));
    }

    // ===== Gaussian helper functions =====

    private static double CNDF(double x) {
        int neg = (x < 0d) ? 1 : 0;
        if (neg == 1) x *= -1d;
        double k = (1d / (1d + 0.2316419 * x));
        double y = ((((1.330274429 * k - 1.821255978) * k + 1.781477937) *
                k - 0.356563782) * k + 0.319381530) * k;
        y = 1.0 - 0.398942280401 * Math.exp(-0.5 * x * x) * y;
        return (1d - neg) * y + neg * (1d - y);
    }

    private static double pdf(double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    private static double pdf(double x, double mu, double sigma) {
        return pdf((x - mu) / sigma) / sigma;
    }

    private static double CNDF(double z, double mu, double sigma) {
        return CNDF((z - mu) / sigma);
    }

    // ===== Fuzzy comparison operations (return UBoolean) =====

    private Result calculate(UReal number) {
        Result r = new Result();
        double m1, m2, s1, s2;
        boolean swap = false;
        if (this.getX() <= number.getX()) {
            m1 = this.getX(); m2 = number.getX();
            s1 = this.getU(); s2 = number.getU();
        } else {
            m2 = this.getX(); m1 = number.getX();
            s2 = this.getU(); s1 = number.getU();
            swap = true;
        }

        if ((s1 == 0.0) && (s2 == 0.0)) {
            if (m1 == m2) { r.lt = 0.0; r.eq = 1.0; r.gt = 0.0; return r.check(swap); }
            if (m1 < m2) { r.lt = 1.0; r.eq = 0.0; r.gt = 0.0; return r.check(swap); }
            r.lt = 0.0; r.eq = 0.0; r.gt = 1.0; return r.check(swap);
        }
        if ((s1 == 0.0)) {
            r.lt = 1 - CNDF(m1, m2, s2); r.eq = 0.0; r.gt = CNDF(m1, m2, s2);
            return r.check(swap);
        }
        if ((s2 == 0.0)) {
            r.lt = CNDF(m2, m1, s1); r.eq = 0.0; r.gt = 1 - CNDF(m2, m1, s1);
            return r.check(swap);
        }
        if (s1 == s2) {
            double crossing = (m1 + m2) / 2;
            r.lt = CNDF(crossing, m1, s1) - CNDF(crossing, m2, s2);
            r.gt = 0.0;
            r.eq = 1.0 - (r.gt + r.lt);
            return r.check(swap);
        } else {
            double crossing1 = -(-m2 * s1 * s1 + m1 * s2 * s2 +
                    s1 * s2 * Math.sqrt((m1 - m2) * (m1 - m2) - 2.0 * (s1 * s1 - s2 * s2) * Math.log(s2 / s1)))
                    / (s1 * s1 - s2 * s2);
            double crossing2 = (m2 * s1 * s1 - m1 * s2 * s2 +
                    s1 * s2 * Math.sqrt(((m1 - m2) * (m1 - m2) - 2.0 * (s1 * s1 - s2 * s2) * Math.log(s2 / s1))))
                    / (s1 * s1 - s2 * s2);
            double c1 = Math.min(crossing1, crossing2);
            double c2 = Math.max(crossing1, crossing2);
            if (s1 < s2) {
                r.gt = CNDF(c1, m2, s2) - CNDF(c1, m1, s1);
                r.lt = 1.0 - CNDF(c2, m2, s2) - (1.0 - CNDF(c2, m1, s1));
                r.eq = CNDF(c1, m1, s1) + (1.0 - CNDF(c2, m1, s1)) + CNDF(c2, m2, s2) - CNDF(c1, m2, s2);
            } else {
                r.lt = CNDF(c1, m1, s1) - CNDF(c1, m2, s2);
                r.gt = 1.0 - CNDF(c2, m1, s1) - (1.0 - CNDF(c2, m2, s2));
                r.eq = CNDF(c1, m2, s2) + (1.0 - CNDF(c2, m2, s2)) + CNDF(c2, m1, s1) - CNDF(c1, m1, s1);
            }
            return r.check(swap);
        }
    }

    public UBoolean uEquals(UReal number) {
        Result r = this.calculate(number);
        return new UBoolean(true, r.eq);
    }

    public UBoolean uDistinct(UReal r) {
        return this.uEquals(r).not();
    }

    public UBoolean lt(UReal number) {
        Result r = this.calculate(number);
        return new UBoolean(true, r.lt);
    }

    public UBoolean le(UReal number) {
        Result r = this.calculate(number);
        return new UBoolean(true, r.lt + r.eq);
    }

    public UBoolean gt(UReal number) {
        Result r = this.calculate(number);
        return new UBoolean(true, r.gt);
    }

    public UBoolean ge(UReal number) {
        Result r = this.calculate(number);
        return new UBoolean(true, r.gt + r.eq);
    }

    // ===== Fuzzy comparisons with zero =====

    public UBoolean uEqualsZero(double u) { return this.uEquals(new UReal(0.0, u)); }
    public UBoolean uDistinctZero(double u) { return this.uDistinct(new UReal(0.0, u)); }
    public UBoolean ltZero() { return this.lt(new UReal()); }
    public UBoolean leZero() { return this.le(new UReal()); }
    public UBoolean gtZero() { return this.gt(new UReal()); }
    public UBoolean geZero() { return this.ge(new UReal()); }

    // ===== Comparable, conversions, misc =====

    @Override
    public int compareTo(UReal other) {
        if (this.equals(other)) return 0;
        if (this.lt(other).toBoolean()) return -1;
        return 1;
    }

    public String toString() {
        return String.format("UReal(%5.3f, %5.3f)", this.getX(), this.getU());
    }

    public int toInteger() { return (int) Math.floor(this.getX()); }
    public double toReal() { return this.getX(); }

    public UInteger toUInteger() {
        UInteger r = new UInteger();
        r.setX((int) Math.floor(this.getX()));
        r.setU(Math.sqrt((this.getU() * this.getU()) + (this.getX() - r.getX()) * (this.getX() - r.getX())));
        return r;
    }

    public int hashcode() { return Math.round((float) x); }

    @Override
    public UReal clone() {
        return new UReal(this.getX(), this.getU());
    }
}
