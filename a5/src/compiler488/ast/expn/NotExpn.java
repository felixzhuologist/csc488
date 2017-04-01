package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;
import compiler488.semantics.SemanticErrorException;

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
    public void doSemantics() throws SemanticErrorException {
      operand.doSemantics();
      if (!(operand.getResultType() instanceof BooleanType)) {
        throw new SemanticErrorException("Negating non boolean expression");
      }
      resultType = new BooleanType(lineNumber);
    }
}
