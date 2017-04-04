package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Readable;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.type.IntegerType;
import compiler488.ast.expn.Expn;
import compiler488.codegen.CodeGenErrorException;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticErrorException;

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
	public void doSemantics() throws SemanticErrorException {
		inputs.doSemantics();

		// Check that each input variable exists as an int in the symbol table
		ListIterator<Readable> inputIter = inputs.getIter();
		while (inputIter.hasNext()) {
			Readable input = inputIter.next();
			if (!(((Expn) input).getResultType() instanceof IntegerType)) {
				throw new SemanticErrorException("Can't read input into non integer variable");
			}
		}
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		ListIterator inIter = inputs.getIter();
		try {
			while (inIter.hasNext()) {
				Expn input = (Expn)(inIter.next());
				if (input instanceof IdentExpn) {
					input.doCodeGenLHS();
				} else if (input instanceof SubsExpn) {
					input.doCodeGen();
				} else {
					throw new CodeGenErrorException("Can't read input of such an expression");
				}
				Machine.writeMemory(Main.codeGenAddr++, Machine.READI);
				Machine.writeMemory(Main.codeGenAddr++, Machine.STORE);
			}
		} catch (Exception e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}

	public ASTList<Readable> getInputs() {
		return inputs;
	}

	public void setInputs(ASTList<Readable> inputs) {
		this.inputs = inputs;
	}
}
