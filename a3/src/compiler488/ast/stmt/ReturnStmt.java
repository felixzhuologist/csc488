package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.Type;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

/**
 * The command to return from a function or procedure.
 */
public class ReturnStmt extends Stmt {
	// The value to be returned by a function.
	private Expn value = null;

	public ReturnStmt(Integer lineNumber) {
		super(lineNumber);
	}

	public ReturnStmt(Integer lineNumber, Expn value) {
		super(lineNumber);
		this.value = value;
	}

	/**
	 * Print <b>return</b> or <b>return with </b> expression on a line, by itself.
	 * 
	 * @param out
	 *            Where to print.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOn(out, depth);
		if (value == null)
			out.println("return ");
		else
			out.println("return with " + value );
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		if (Main.routineStack.empty()) {
			throw new SemanticErrorException("Returning outside of function or procedure");
		}

		boolean isReturningValue = (value != null);
		boolean isInsideProcedure = (Main.routineStack.peek() instanceof ProcedureSymbol);

		if (isReturningValue && isInsideProcedure) {
			throw new SemanticErrorException("Returning a value inside of a procedure");
		} else if (!isReturningValue && !isInsideProcedure) {
			throw new SemanticErrorException("Not returning anything inside of a function");
		}

		if (isReturningValue && !isInsideProcedure) { // check that return type matches function decl
			value.doSemantics();
			Type expectedReturnType = ((FunctionSymbol) Main.routineStack.peek()).getReturnType();
			if (!expectedReturnType.getClass().equals(value.getResultType().getClass())) {
				throw new SemanticErrorException("Expected return value of type " + 
														expectedReturnType.getClass().getName() +
														" but got one of type " +
														value.getResultType().getClass().getName() +
														" instead");
			}
		}
	}

	public Expn getValue() {
		return value;
	}

	public void setValue(Expn value) {
		this.value = value;
	}
}
