package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Printable;
import compiler488.ast.type.IntegerType;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.Expn;
import compiler488.semantics.SemanticErrorException;

import java.util.ListIterator;

/**
 * The command to write data on the output device.
 */
public class WriteStmt extends Stmt {
	private ASTList<Printable> outputs; // The objects to be printed.

	public WriteStmt (Integer lineNumber) {
		super(lineNumber);
		outputs = new ASTList<Printable> ();
	}

	public WriteStmt(Integer lineNumber, ASTList<Printable> outputs) {
		super(lineNumber);
		this.outputs = outputs;
	}

	/** Returns a description of the <b>write</b> statement. */
	@Override
	public String toString() {
		return "write " + outputs;
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		this.outputs.doSemantics();

		// Check that each output is text, newline, or an int expression
		ListIterator<Printable> outputIter = outputs.getIter();
		while (outputIter.hasNext()) {
			Printable output = outputIter.next();
			if (!(output instanceof SkipConstExpn || output instanceof TextConstExpn || 
						((Expn) output).getResultType() instanceof IntegerType)) {
				throw new SemanticErrorException("Write output must be text, newline, or integer");
			}
		}
	}
	
	public ASTList<Printable> getOutputs() {
		return outputs;
	}

	public void setOutputs(ASTList<Printable> outputs) {
		this.outputs = outputs;
	}
}
