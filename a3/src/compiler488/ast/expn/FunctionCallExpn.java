package compiler488.ast.expn;

import compiler488.ast.ASTList;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.ast.type.*;
import compiler488.semantics.Util;
import compiler488.semantics.SemanticErrorException;

import java.util.ArrayList;

/**
 * Represents a function call with or without arguments.
 */
public class FunctionCallExpn extends Expn {
	private String ident; // The name of the function.

	private ASTList<Expn> arguments; // The arguments passed to the function.

	public FunctionCallExpn(Integer lineNumber, String ident, ASTList<Expn> arguments) {
		super(lineNumber);
		this.ident = ident;
		this.arguments = arguments;
	}

	/** Returns a string describing the function call. */
	@Override
	public String toString() {
		if (arguments!=null) {
			return ident + " (" + arguments + ")";
		}
		else
			return ident + " " ;
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		// verify that function has been defined
		SymbolTableEntry entry = Main.symbolTable.getEntry(ident);
		if (entry == null || !(entry instanceof FunctionSymbol)) {
			throw new SemanticErrorException("Calling undeclared function " + ident);
		}

		// Assume that there is at least one arg (since the no arg case is in IdentExpn)
		FunctionSymbol funcEntry = (FunctionSymbol) entry;
		ArrayList<Expn> args = arguments.getList();
		ArrayList<Type> expectedArgTypes = funcEntry.getParamTypes();

		if (args.size() != expectedArgTypes.size()) {
				throw new SemanticErrorException("Function " + ident + " was called with " +
													args.size() + " args but expected " + expectedArgTypes.size());		
		}

		arguments.doSemantics();
		for (int i = 0; i < args.size(); i++) {
			Type got = args.get(i).getResultType();
			Type expected = expectedArgTypes.get(i);
			if (!got.getClass().equals(expected.getClass())) {
				throw new SemanticErrorException("Expected " + expected.getClass().getName() + " for param number " + (i + 1) + " of function " +
														ident + " but got " + got.getClass().getName() + " instead");
			}
		}

		// Set the type of this expn to be the return type of the function
		this.resultType = Util.getTypeWithLineNumber(funcEntry.getReturnType(), lineNumber);
	}

	public ASTList<Expn> getArguments() {
		return arguments;
	}

	public void setArguments(ASTList<Expn> args) {
		this.arguments = args;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

}
