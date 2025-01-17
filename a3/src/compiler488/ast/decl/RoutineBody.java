package compiler488.ast.decl;

import java.io.PrintStream;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.stmt.Scope;
import compiler488.semantics.SemanticErrorException;

/**
 * Represents the parameters and instructions associated with a
 * function or procedure.
 */
public class RoutineBody extends Indentable {
	private ASTList<ScalarDecl> parameters; // The formal parameters of the routine.

	private Scope body; // Execute this scope when routine is called.

	public RoutineBody(Integer lineNumber, ASTList<ScalarDecl> parameters, Scope body) {
		super(lineNumber);
		this.parameters = parameters;
		this.body = body;
	}

	/* Constructor for routines with no parameters. */
	public RoutineBody(Integer lineNumber, Scope body) {
		super(lineNumber);
		this.parameters = new ASTList<ScalarDecl>();
		this.body = body;
	}
	/**
	 * Print a description of the formal parameters and the scope for this
	 * routine.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		if (parameters != null)
			out.println("(" + parameters + ")");
		else
			out.println(" ");
		body.printOn(out, depth);
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		this.parameters.doSemantics();
		this.body.doSemantics();
	}

	public Scope getBody() {
		return body;
	}

	public void setBody(Scope body) {
		this.body = body;
	}

	public ASTList<ScalarDecl> getParameters() {
		return parameters;
	}

	public void setParameters(ASTList<ScalarDecl> parameters) {
		this.parameters = parameters;
	}
}
