package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
import compiler488.symbol.*;
import compiler488.compiler.Main;

/**
 * Represents calling a procedure.
 */
public class ProcedureCallStmt extends Stmt {
	private String name; // The name of the procedure being called.

	private ASTList<Expn> arguments; // The arguments passed to the procedure.

	public ProcedureCallStmt(Integer lineNumber, String name) {
		super(lineNumber);
		this.name = name;
	}

	public ProcedureCallStmt(Integer lineNumber, String name, ASTList<Expn> arguments) {
		super(lineNumber);
		this.name = name;
		this.arguments = arguments;
	}

	/** Returns a string describing the procedure call. */
	@Override
	public String toString() {
		if (arguments!=null)
			return "Procedure call: " + name + " (" + arguments + ")";
		else
			return "Procedure call: " + name + " ";
	}

	@Override
	public void doSemantics() throws Exception {
		SymbolTableEntry entry = Main.symbolTable.getEntry(name);
		if (entry == null || !(entry instanceof ProcedureSymbol)) {
			throw new Exception("Calling undeclared procedure " + name);
		}

		if (arguments != null) {
			arguments.doSemantics();
		}

		int numGivenArgs = (arguments == null) ? 0 : arguments.size();
		int numExpectedArgs = ((ProcedureSymbol) entry).getNumParams();
		if (numGivenArgs != numExpectedArgs) {
			throw new Exception("Procedure " + name + " was called with " +
													numGivenArgs + " args but expected " + numExpectedArgs);
		}
	}

	public ASTList<Expn> getArguments() {
		return arguments;
	}

	public void setArguments(ASTList<Expn> args) {
		this.arguments = args;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
