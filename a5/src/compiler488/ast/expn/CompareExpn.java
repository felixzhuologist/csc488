package compiler488.ast.expn;

import compiler488.ast.type.*;
import compiler488.semantics.SemanticErrorException;

/**
 * Place holder for all ordered comparisions expression where both operands must
 * be integer expressions. e.g. < , > etc. comparisons
 */
public class CompareExpn extends BinaryExpn {

    public CompareExpn(Integer lineNumber, Expn left, Expn right, String opSymbol) {
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.opSymbol = opSymbol;
    }

    @Override
    public void doSemantics() throws SemanticErrorException {
        left.doSemantics();
        right.doSemantics();

        if (!(left.getResultType() instanceof IntegerType && right.getResultType() instanceof IntegerType)) {
            throw new SemanticErrorException(opSymbol + " expects two integers but got a " +
                                left.getResultType().getClass().getName() + " and " +
                                right.getResultType().getClass().getName() + " instead");
        }

        this.resultType = new BooleanType(lineNumber);
    }
}
