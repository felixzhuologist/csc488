package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;
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
}
