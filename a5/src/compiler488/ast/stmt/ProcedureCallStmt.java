package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.*;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

import java.util.ArrayList;

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
	public void doSemantics() throws SemanticErrorException {
		// verify that the procedure has been defined
		SymbolTableEntry entry = Main.symbolTable.getEntry(name);
		if (entry == null || !(entry instanceof ProcedureSymbol)) {
			throw new SemanticErrorException("Calling undeclared procedure " + name);
		}

		if (arguments == null) {
			return;
		}

		// verify number of args
		ProcedureSymbol procEntry = (ProcedureSymbol) entry;
		ArrayList<Expn> args = arguments.getList();
		ArrayList<Type> expectedArgTypes = procEntry.getParamTypes();


		if (args.size() != expectedArgTypes.size()) {
				throw new SemanticErrorException("Procedure " + name + " was called with " +
													args.size() + " args but expected " + expectedArgTypes.size());		
		}

		// verify types of args
		arguments.doSemantics();
		for (int i = 0; i < args.size(); i++) {
			Type got = args.get(i).getResultType();
			Type expected = expectedArgTypes.get(i);
			if (!got.getClass().equals(expected.getClass())) {
				throw new SemanticErrorException("Expected " + expected.getClass().getName() + " for param number " + (i + 1) + " of procedure " +
														name + " but got " + got.getClass().getName() + " instead");
			}
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
