package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {

    public UnaryMinusExpn(Integer lineNumber, Expn operand) {
        super(lineNumber);
        this.operand = operand;
        this.opSymbol = "-";
    }

    @Override
    public void doSemantics() throws Exception {
      this.operand.doSemantics();
      if (!(operand.getResultType() instanceof IntegerType)) {
        throw new Exception("Unary minussing a non integer expression");
      }
      this.resultType = new IntegerType(lineNumber);
    }
}
