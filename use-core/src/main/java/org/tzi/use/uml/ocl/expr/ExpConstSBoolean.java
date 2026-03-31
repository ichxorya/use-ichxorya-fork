package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.uml.ocl.type.TypeFactory;
import org.tzi.use.uml.ocl.value.SBooleanValue;
import org.tzi.use.uml.ocl.value.UndefinedValue;
import org.tzi.use.uml.ocl.value.Value;

public class ExpConstSBoolean extends Expression {

    private Expression beliefExpression;
    private Expression disbeliefExpression;
    private Expression uncertaintyExpression;
    private Expression agentExpression;

    public ExpConstSBoolean(Expression beliefExpression, Expression disbeliefExpression, Expression uncertaintyExpression, Expression agentExpression) throws ExpInvalidException {
        super(TypeFactory.mkSBoolean());

        if (!beliefExpression.type().isKindOfReal(Type.VoidHandling.EXCLUDE_VOID) && !beliefExpression.type().isTypeOfVoidType())
            throw new ExpInvalidException("Belief must be a kind of Real");

        if (!disbeliefExpression.type().isKindOfReal(Type.VoidHandling.EXCLUDE_VOID) && !disbeliefExpression.type().isTypeOfVoidType())
            throw new ExpInvalidException("Disbelief must be a kind of Real");

        if (!uncertaintyExpression.type().isKindOfReal(Type.VoidHandling.EXCLUDE_VOID) && !uncertaintyExpression.type().isTypeOfVoidType())
            throw new ExpInvalidException("Uncertainty must be a kind of Real");

        if (!agentExpression.type().isKindOfReal(Type.VoidHandling.EXCLUDE_VOID) && !agentExpression.type().isTypeOfVoidType())
            throw new ExpInvalidException("Agent base rate must be a kind of Real");

        this.beliefExpression = beliefExpression;
        this.disbeliefExpression = disbeliefExpression;
        this.uncertaintyExpression = uncertaintyExpression;
        this.agentExpression = agentExpression;
    }

    @Override
    public Value eval(EvalContext ctx) {
        Value belief, disbelief, uncertainty, agent;
        Value result = null;

        ctx.enter(this);
        belief = beliefExpression.eval(ctx);
        disbelief = disbeliefExpression.eval(ctx);
        uncertainty = uncertaintyExpression.eval(ctx);
        agent = agentExpression.eval(ctx);

        if (belief.isUndefined() || disbelief.isUndefined() || uncertainty.isUndefined() || agent.isUndefined()) {
            result = UndefinedValue.instance;
        } else {
            try {
                result = new SBooleanValue(
                        Double.parseDouble(belief.toString()),
                        Double.parseDouble(disbelief.toString()),
                        Double.parseDouble(uncertainty.toString()),
                        Double.parseDouble(agent.toString())
                );
            }
            catch (Exception ex) {
                result = UndefinedValue.instance;
            }
        }
        
        ctx.exit(this, result);

        return result;
    }

    @Override
    protected boolean childExpressionRequiresPreState() {
        return false;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append("SBoolean(")
                .append(beliefExpression.toString())
                .append(",")
                .append(disbeliefExpression.toString())
                .append(",")
                .append(uncertaintyExpression.toString())
                .append(",")
                .append(agentExpression.toString())
                .append(")");
        return sb;
    }

    @Override
    public void processWithVisitor(ExpressionVisitor visitor) {
        visitor.visitConstSBoolean(this);
    }
}
