package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;
import compiler488.codegen.CodeGenErrorException;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.SemanticErrorException;

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
    public void doSemantics() throws SemanticErrorException {
      this.operand.doSemantics();
      if (!(operand.getResultType() instanceof IntegerType)) {
        throw new SemanticErrorException("Unary minussing a non integer expression");
      }
      this.resultType = new IntegerType(lineNumber);
    }


    @Override
    public void doCodeGen() throws CodeGenErrorException {
        try {
            operand.doCodeGen();
            Machine.writeMemory(Main.codeGenAddr++, Machine.NEG);

        } catch (MemoryAddressException e) {
            throw new CodeGenErrorException(e.getMessage());
        }
    }

}
