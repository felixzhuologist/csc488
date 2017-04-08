package compiler488.codegen;

import compiler488.ast.AST;
import compiler488.ast.expn.Expn;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

/**
 * Created by evanklein on 2017-04-01.
 */
public class Utils {

    /* Code to generate the NOT instruction */
    public static void generateNotCode() throws MemoryAddressException {
        Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
        Machine.writeMemory(Main.codeGenAddr++, Machine.MACHINE_FALSE);
        Machine.writeMemory(Main.codeGenAddr++, Machine.EQ);
    }

    /* Now that we have an address to go to, patch the goToAddr address inside
    *  the instruction located at instructionAddr in memory.
    *  Previously, the value at this instruction address was UNDEFINED.
    * */
    public static void patch(short instructionAddr, short goToAddr) throws MemoryAddressException {
        Machine.writeMemory(instructionAddr, goToAddr);
    }

    /* Generate code for if statements: this is used for actual if statements as well as
    * conditional expressions.
    */
    public static void generateIfCode(Expn condition, AST whenTrue, AST whenFalse)
            throws MemoryAddressException, CodeGenErrorException {
        // if (condition)
        condition.doCodeGen();
        Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);

        // save the address of the instruction so we can patch it later
        short labelFalseInstrAddr = Main.codeGenAddr;

        // push address to branch to if false, is undefined right now, will be patched later
        Machine.writeMemory(Main.codeGenAddr++, Machine.UNDEFINED);
        Machine.writeMemory(Main.codeGenAddr++, Machine.BF);

        // if true then:
        whenTrue.doCodeGen();
        
        if (whenFalse != null) {
            // need to push the address to branch to, so that it skips the else part
            //	PUSH label_after
            //	BR
            Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
            // save the address of the instruction so we can patch it later
            short labelAfterInstrAddr = Main.codeGenAddr;
            Machine.writeMemory(Main.codeGenAddr++, Machine.UNDEFINED);
            Machine.writeMemory(Main.codeGenAddr++, Machine.BR);

            // the current address that we have right now is the address to branch to if the condition is false, so
            // we can patch this now
            Utils.patch(labelFalseInstrAddr, Main.codeGenAddr);

            // now the else part
            whenFalse.doCodeGen();

            // now we can patch the address of "after the if block"
            Utils.patch(labelAfterInstrAddr, Main.codeGenAddr);
        } else {
            // just patch the false address so it goes to after the if block if the condition is false
            Utils.patch(labelFalseInstrAddr, Main.codeGenAddr);
        }

    }
}
