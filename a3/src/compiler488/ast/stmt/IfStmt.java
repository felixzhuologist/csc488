package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.BooleanType;
import compiler488.semantics.SemanticErrorException;
import compiler488.ast.type.BooleanType;

/**
 * Represents an if-then or an if-then-else construct.
 */
public class IfStmt extends Stmt {
	// The condition that determines which branch to execute.
	private Expn condition;

	// Represents the statement to execute when the condition is true.
	private Stmt whenTrue;

	// Represents the statement to execute when the condition is false.
	private Stmt whenFalse = null;


	public IfStmt(Integer lineNumber, Expn condition, Stmt whenTrue) {
		super(lineNumber);
		this.condition = condition;
		this.whenTrue = whenTrue;
	}

	public IfStmt(Integer lineNumber, Expn condition, Stmt whenTrue, Stmt whenFalse) {
		super(lineNumber);
		this.condition = condition;
		this.whenTrue = whenTrue;
		this.whenFalse = whenFalse;
	}

	/**
	 * Print a description of the <b>if-then-else</b> construct. If the
	 * <b>else</b> part is empty, just print an <b>if-then</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "if " + condition + " then ");
		whenTrue.printOn(out, 	depth + 1);
		if (whenFalse != null) {
			Indentable.printIndentOnLn(out, depth, "else");
			whenFalse.printOn(out, depth + 1);
		}
		Indentable.printIndentOnLn(out, depth, "End if");
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		condition.doSemantics();
		
		if (!(condition.getResultType() instanceof BooleanType)) {
			throw new SemanticErrorException("If condition must evaluate to boolean");
		}

		whenTrue.doSemantics();
		if (whenFalse != null) {
			whenFalse.doSemantics();
		}
	}

	public Expn getCondition() {
		return condition;
	}

	public void setCondition(Expn condition) {
		this.condition = condition;
	}

	public Stmt getWhenFalse() {
		return whenFalse;
	}

	public void setWhenFalse(Stmt whenFalse) {
		this.whenFalse = whenFalse;
	}

	public Stmt getWhenTrue() {
		return whenTrue;
	}

	public void setWhenTrue(Stmt whenTrue) {
		this.whenTrue = whenTrue;
	}
}
