package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.compiler.Main;
import compiler488.codegen.Utils;
import compiler488.codegen.CodeGenErrorException;
import compiler488.semantics.SemanticErrorException;

/**
 * Place holder for all binary expression where both operands could be either
 * integer or boolean expressions. e.g. = and not = comparisons
 */
public class EqualsExpn extends BinaryExpn {

    public EqualsExpn(Integer lineNumber, Expn left, Expn right, String opSymbol) {
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.opSymbol = opSymbol;
    }

    @Override
    public void doSemantics() throws SemanticErrorException {
        this.left.doSemantics();
        this.right.doSemantics();

        if (!(left.getResultType() instanceof BooleanType && right.getResultType() instanceof BooleanType) && 
            !(left.getResultType() instanceof IntegerType && right.getResultType() instanceof IntegerType)) {
            throw new SemanticErrorException(opSymbol + " expects two of the same type but got a " +
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
            switch (opSymbol) {
                case "=":
                    Machine.writeMemory(Main.codeGenAddr++, Machine.EQ);
                    break;
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
