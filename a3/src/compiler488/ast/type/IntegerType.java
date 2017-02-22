package compiler488.ast.type;

/**
 * Used to declare objects that yield integers.
 */
public class IntegerType extends Type {
	public IntegerType() {
	}

	/** Returns the string <b>"Integer"</b>. */
	@Override
	public String toString() {
		return "integer";
	}
}
