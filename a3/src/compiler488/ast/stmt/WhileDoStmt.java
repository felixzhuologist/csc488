package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.BooleanType;


/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {

	public WhileDoStmt(Integer lineNumber, Stmt body, Expn expn) {
		super(lineNumber);
		this.body = body;
		this.expn = expn;
	}

	/**
	 * Print a description of the <b>while-do</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "while " + expn + " do");
		body.printOn(out, depth + 1);
		Indentable.printIndentOnLn(out, depth, "End while-do");
	}
	
    @Override
    public void doSemantics() throws Exception {
        expn.doSemantics();
        if (!(expn.getResultType() instanceof BooleanType)) {
            throw new Exception("Unexpected type: " + expn.getClass().getSimpleName());
        }
    }
}
