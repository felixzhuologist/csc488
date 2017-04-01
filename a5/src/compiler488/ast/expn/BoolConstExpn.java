package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;

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

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
}
