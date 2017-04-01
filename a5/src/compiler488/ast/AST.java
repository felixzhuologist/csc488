package compiler488.ast;

import compiler488.codegen.CodeGenErrorException;
import compiler488.semantics.SemanticErrorException;

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
	
	public final static String version = "Winter 2017";
	
	public void doSemantics() throws SemanticErrorException {
        // Do nothing by default.
	}

	public void doCodeGen() throws CodeGenErrorException {
        // Do nothing by default.
	}
}
