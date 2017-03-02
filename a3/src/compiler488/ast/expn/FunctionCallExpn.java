package compiler488.ast.expn;

import compiler488.ast.ASTList;
import compiler488.symbol.*;
import compiler488.compiler.Main;

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

		if (arguments != null) {
			arguments.doSemantics();
		}

		int numGivenArgs = (arguments == null) ? 0 : arguments.size();
		int numExpectedArgs = ((FunctionSymbol) entry).getNumParams();
		System.out.println(numExpectedArgs);
		if (numGivenArgs != numExpectedArgs) {
			throw new Exception("Function " + ident + " was called with " +
													numGivenArgs + " args but expected " + numExpectedArgs);
		}
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
