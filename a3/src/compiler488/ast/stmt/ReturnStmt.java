package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.Type;
import compiler488.symbol.*;
import compiler488.compiler.Main;

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
	public void doSemantics() throws Exception {
		if (Main.routineStack.empty()) {
			throw new Exception("Returning outside of function or procedure");
		}

		boolean returningValue = (value != null);
		boolean insideProcedure = (Main.routineStack.peek() instanceof ProcedureSymbol);

		if (returningValue && insideProcedure) {
			throw new Exception("Returning a value inside of a procedure");
		} else if (!returningValue && !insideProcedure) {
			throw new Exception("Not returning anything inside of a function");
		}

		value.doSemantics();
		if (returningValue && !insideProcedure) { // check that return type matches function decl
			Type expectedReturnType = ((FunctionSymbol) Main.routineStack.peek()).getReturnType();
			if (!expectedReturnType.getClass().equals(value.getResultType().getClass())) {
				throw new Exception("Expected return value of type " + 
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
