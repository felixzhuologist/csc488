package compiler488.ast.expn;

import compiler488.ast.Printable;
import compiler488.codegen.CodeGenErrorException;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

/**
 * Represents the special literal constant associated with writing a new-line
 * character on the output device.
 */
public class SkipConstExpn extends ConstExpn implements Printable {
	public SkipConstExpn(Integer lineNumber) {
		super(lineNumber);
	}

	/** Returns the string <b>"skip"</b>. */
	@Override
	public String toString() {
		return " newline ";
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, (short)'\n');

		} catch (MemoryAddressException e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
}
