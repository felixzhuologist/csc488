package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {

    public NotExpn(Integer lineNumber, Expn operand) {
        super(lineNumber);
        this.operand = operand;
        this.opSymbol = "not";
    }

    @Override
    public void doSemantics() {
      this.resultType = new BooleanType(lineNumber);
    }
}
