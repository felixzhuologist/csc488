package compiler488.ast.expn;

import compiler488.ast.type.*;
import compiler488.semantics.SemanticErrorException;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.compiler.Main;
import compiler488.codegen.Utils;
import compiler488.codegen.CodeGenErrorException;

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

  @Override
  public void doCodeGen() throws CodeGenErrorException {
    try {
        left.doCodeGen();
        right.doCodeGen();
        short opcode;
        switch (opSymbol) {
            case "<": 
                Machine.writeMemory(Main.codeGenAddr++, Machine.LT);
                break;
            case "<=": 
                Machine.writeMemory(Main.codeGenAddr++, Machine.SWAP);
                Machine.writeMemory(Main.codeGenAddr++, Machine.LT);
                Utils.generateNotCode();
                break;
            case ">":
                Machine.writeMemory(Main.codeGenAddr++, Machine.SWAP);
                Machine.writeMemory(Main.codeGenAddr++, Machine.LT);
                break;
            case ">=":
                Machine.writeMemory(Main.codeGenAddr++, Machine.LT);
                Utils.generateNotCode();
                break;
            case "=":
                Machine.writeMemory(Main.codeGenAddr++, Machine.EQ);
            case "!=":
                Machine.writeMemory(Main.codeGenAddr++, Machine.EQ);
                Utils.generateNotCode();
            default:
                throw new CodeGenErrorException("Unknown operation " + opSymbol);
        }
    } catch (MemoryAddressException e) {
      throw new CodeGenErrorException(e.getMessage());
    }
  }
}
