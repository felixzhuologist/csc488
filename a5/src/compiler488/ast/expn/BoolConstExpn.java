package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.codegen.CodeGenErrorException;

/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn
    {
    private boolean  value ;	/* value of the constant */

	public BoolConstExpn(Integer lineNumber, boolean value) {
		super(lineNumber);
		this.value = value;
	}

		/** Returns the value of the boolean constant */
    @Override
	public String toString () { 
	return ( value ? "(true)" : "(false)" );
    }

  @Override
  public void doSemantics() {
    this.resultType = new BooleanType(lineNumber);
  }

  @Override
  public void doCodeGen() throws CodeGenErrorException {
    try {
      short boolVal = value ? Machine.MACHINE_TRUE : Machine.MACHINE_FALSE;
      Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);    
      Machine.writeMemory(Main.codeGenAddr++, boolVal);
    } catch (MemoryAddressException e) {
      throw new CodeGenErrorException(e.getMessage());
    }
  }

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
}
