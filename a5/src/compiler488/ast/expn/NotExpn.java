package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;
import compiler488.codegen.CodeGenErrorException;
import compiler488.codegen.Utils;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.SemanticErrorException;
import compiler488.semantics.Util;

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

    @Override
    public void doCodeGen() throws CodeGenErrorException {
        try {
            operand.doCodeGen();
            Utils.generateNotCode();

        } catch (MemoryAddressException e) {
            throw new CodeGenErrorException(e.getMessage());
        }
    }

}
