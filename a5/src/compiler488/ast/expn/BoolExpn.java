package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;
import compiler488.codegen.CodeGenErrorException;
import compiler488.codegen.Utils;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.SemanticErrorException;

/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {

    public BoolExpn(Integer lineNumber, Expn left, Expn right, String opSymbol) {
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.opSymbol = opSymbol;
    }

    @Override
    public void doSemantics() throws SemanticErrorException {
        left.doSemantics();
        right.doSemantics();

        if (!(left.getResultType() instanceof BooleanType && right.getResultType() instanceof BooleanType)) {
            throw new SemanticErrorException(opSymbol + " expects two booleans but got a " +
                                left.getResultType().getClass().getName() + " and " +
                                right.getResultType().getClass().getName() + " instead");
        }
        this.resultType = new BooleanType(lineNumber);
    }

    @Override
    public void doCodeGen() throws CodeGenErrorException {
        try {
            switch (opSymbol) {
                case "or":
                    left.doCodeGen();
                    right.doCodeGen();
                    Machine.writeMemory(Main.codeGenAddr++, Machine.OR);
                    break;
                case "and":
                    // De Morgan's law: not ((not left) or (not right))

                    // not left
                    left.doCodeGen();
                    Utils.generateNotCode();
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.MACHINE_FALSE);
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.EQ);

                    // not right
                    right.doCodeGen();
                    Utils.generateNotCode();
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.MACHINE_FALSE);
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.EQ);

                    // not ((not left) or (not right))
                    Machine.writeMemory(Main.codeGenAddr++, Machine.OR);
                    Utils.generateNotCode();
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.MACHINE_FALSE);
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
//                    Machine.writeMemory(Main.codeGenAddr++, Machine.EQ);

                    break;
                default:
                    throw new CodeGenErrorException("Unknown operation " + opSymbol);
            }

        } catch (MemoryAddressException e) {
            throw new CodeGenErrorException(e.getMessage());
        }
    }
}
