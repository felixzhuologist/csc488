package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.SemanticErrorException;
import compiler488.codegen.CodeGenErrorException;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {


    public ArithExpn(Integer lineNumber, Expn left, Expn right, String opSymbol) {
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
        this.resultType = new IntegerType(lineNumber);
    }

    @Override
    public void doCodeGen() throws CodeGenErrorException{
        left.doCodeGen();
        right.doCodeGen();
        short opcode;
        switch (opSymbol) {
            case "+": 
                opcode = Machine.ADD;
                break;
            case "-": 
                opcode = Machine.SUB;
                break;
            case "*":
                opcode = Machine.MUL;
                break;
            case "/":
                opcode = Machine.DIV;
                break;
            default:
                throw new CodeGenErrorException("Unknown operation " + opSymbol);
        }
        try {
            Machine.writeMemory(Main.codeGenAddr++, opcode);            
        } catch (MemoryAddressException e) {
            throw new CodeGenErrorException(e.getMessage());
        }
    }
}
