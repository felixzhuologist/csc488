package compiler488.ast.expn;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {

    public NotExpn(Expn operand) {
        this.operand = operand;
        this.opSymbol = "not";
    }

}
