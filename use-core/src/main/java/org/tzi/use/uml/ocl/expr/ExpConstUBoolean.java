package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.ocl.type.TypeFactory;
import org.tzi.use.uml.ocl.value.UBooleanValue;
import org.tzi.use.uml.ocl.value.UndefinedValue;
import org.tzi.use.uml.ocl.value.Value;

public class ExpConstUBoolean extends Expression {
    private Expression eValue;
    private Expression eProbability;

    public ExpConstUBoolean(Expression eValue, Expression eProbability)
    throws ExpInvalidException
    {
        super(TypeFactory.mkUBoolean());

        if (!eValue.type().isTypeOfBoolean() && !eValue.type().isTypeOfVoidType())
            throw new ExpInvalidException("Value must be Boolean");

        if (!(eProbability.type().isTypeOfInteger() || eProbability.type().isTypeOfReal() || eProbability.type().isTypeOfVoidType()))
            throw new ExpInvalidException("Probability must be an Integer or Real");

        this.eValue = eValue;
        this.eProbability = eProbability;
    }

    public String value() {
        return eValue.toString();
    }

    public String probability() {
        return eProbability.toString();
    }

    @Override
    public Value eval(EvalContext ctx) {
        Value res = null;
        Value value, probability;

        ctx.enter(this);
        value = eValue.eval(ctx);
        probability = eProbability.eval(ctx);

        if (value.isUndefined() || probability.isUndefined())
            res = UndefinedValue.instance;
        else try {
            boolean b = Boolean.parseBoolean(value.toString());
            double c = Double.parseDouble(probability.toString());
            res = new UBooleanValue(b, c);
        }
        catch (RuntimeException ex) {
            res = UndefinedValue.instance;
        }

        ctx.exit(this, res);

        return res;
    }

    @Override
    protected boolean childExpressionRequiresPreState() {
        return false;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append("UBoolean(")
                .append(eValue.toString())
                .append(",")
                .append(eProbability.toString())
                .append(")");
        return sb;
    }

    @Override
    public void processWithVisitor(ExpressionVisitor visitor) {
        visitor.visitConstUBoolean(this);
    }
}
