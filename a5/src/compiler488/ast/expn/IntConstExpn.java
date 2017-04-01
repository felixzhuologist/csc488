package compiler488.ast.expn;

import com.sun.org.apache.bcel.internal.classfile.Code;
import compiler488.ast.type.IntegerType;
import compiler488.codegen.CodeGenErrorException;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

/**
 * Represents a literal integer constant.
 */
public class IntConstExpn extends ConstExpn
    {
    private Integer value;	// The value of this literal.

	public IntConstExpn(Integer lineNumber, Integer value) {
		super(lineNumber);
		this.value = value;
	}

		/** Returns a string representing the value of the literal. */
    @Override
	public String toString () { return value.toString (); }

	@Override
	public void doSemantics() {
		this.resultType = new IntegerType(lineNumber);
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, value.shortValue());

		} catch (MemoryAddressException e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
}
