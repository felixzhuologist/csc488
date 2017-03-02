package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Readable;
import compiler488.ast.type.IntegerType;
import compiler488.ast.expn.Expn;
import java.util.ListIterator;

/**
 * The command to read data into one or more variables.
 */
public class ReadStmt extends Stmt {
	
	private ASTList<Readable> inputs; // A list of locations to put the values read.

	public ReadStmt (Integer lineNumber) {

		super(lineNumber);
		inputs = new ASTList<Readable> ();
	}

	public ReadStmt(Integer lineNumber, ASTList<Readable> inputs) {
		super(lineNumber);
		this.inputs = inputs;
	}

	/** Returns a string describing the <b>read</b> statement. */
	@Override
	public String toString() {
		return "read " + inputs;
	}

	@Override
	public void doSemantics() throws Exception {
		inputs.doSemantics();

		ListIterator<Readable> inputIter = inputs.getIter();
		while (inputIter.hasNext()) {
			Readable input = inputIter.next();
			if (!(((Expn) input).getResultType() instanceof IntegerType)) {
				throw new Exception("Can't read input into non integer variable");
			}
		}
	}

	public ASTList<Readable> getInputs() {
		return inputs;
	}

	public void setInputs(ASTList<Readable> inputs) {
		this.inputs = inputs;
	}
}
