package compiler488.ast.expn;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {


    public ArithExpn(Expn left, Expn right, String opSymbol) {
        this.left = left;
        this.right = right;
        this.opSymbol = opSymbol;
    }

}
