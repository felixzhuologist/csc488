package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;

/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {

    public BoolExpn(Integer lineNumber, Expn left, Expn right, String opSymbol) {
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.opSymbol = opSymbol;
    }

    @Override
    public void doSemantics() throws Exception {
        left.doSemantics();
        right.doSemantics();

        if (!(left.getResultType() instanceof BooleanType && right.getResultType() instanceof BooleanType)) {
            throw new Exception(opSymbol + " expects two booleans but got a " +
                                left.getResultType().getClass().getName() + " and " +
                                right.getResultType().getClass().getName() + " instead");
        }
        this.resultType = new BooleanType(lineNumber);
    }
}
