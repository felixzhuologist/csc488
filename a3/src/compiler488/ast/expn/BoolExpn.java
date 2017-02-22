package compiler488.ast.expn;

/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {

    public BoolExpn(Expn left, Expn right, String opSymbol) {
        this.left = left;
        this.right = right;
        this.opSymbol = opSymbol;
    }

}
