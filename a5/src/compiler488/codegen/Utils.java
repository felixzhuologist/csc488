package compiler488.codegen;

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
}
