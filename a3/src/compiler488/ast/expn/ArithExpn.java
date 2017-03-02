package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {


    public ArithExpn(Integer lineNumber, Expn left, Expn right, String opSymbol) {
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.opSymbol = opSymbol;
    }

    @Override
    public void doSemantics() throws Exception {
        left.doSemantics();
        right.doSemantics();

        if (!(left.getResultType() instanceof IntegerType && right.getResultType() instanceof IntegerType)) {
            throw new Exception(opSymbol + " expects two integers but got a " +
                                left.getResultType().getClass().getName() + " and " +
                                right.getResultType().getClass().getName() + " instead");
        }
        this.resultType = new IntegerType(lineNumber);
    }
}
