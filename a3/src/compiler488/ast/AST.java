package compiler488.ast;

/**
 * This is a placeholder at the top of the Abstract Syntax Tree hierarchy. It is
 * a convenient place to add common behaviour.
 * @author  Dave Wortman, Marsha Chechik, Danny House
 */
public class AST {

	protected Integer lineNumber;

	public AST() {
	}

	public AST(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String printLineNumber() {
		return "Line: " + lineNumber.toString();
	}

	public void doSemantics() {

	}
	
	public final static String version = "Winter 2017";
	
	public void doSemantics() {
        // Do nothing by default.
	}
}
