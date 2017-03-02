package compiler488.ast.expn;

import compiler488.ast.ASTList;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.ast.type.*;
import compiler488.semantics.Util;

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
	public void doSemantics() throws Exception {
		SymbolTableEntry entry = Main.symbolTable.getEntry(ident);
		if (entry == null || !(entry instanceof FunctionSymbol)) {
			throw new Exception("Calling undeclared function " + ident);
		}

		// verify args
		if (arguments == null) {
			return;
		}
		FunctionSymbol funcEntry = (FunctionSymbol) entry;
		ArrayList<Expn> args = arguments.getList();
		ArrayList<Type> expectedArgTypes = funcEntry.getParamTypes();

		if (args.size() != expectedArgTypes.size()) {
				throw new Exception("Function " + ident + " was called with " +
													args.size() + " args but expected " + expectedArgTypes.size());		
		}

		arguments.doSemantics();
		for (int i = 0; i < args.size(); i++) {
			Type got = args.get(i).getResultType();
			Type expected = expectedArgTypes.get(i);
			if (!got.getClass().equals(expected.getClass())) {
				throw new Exception("Expected " + expected.getClass().getName() + " for param number " + (i + 1) + " of function " +
														ident + " but got " + got.getClass().getName() + " instead");
			}
		}

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
