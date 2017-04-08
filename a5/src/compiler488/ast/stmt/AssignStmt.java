package compiler488.ast.stmt;

import compiler488.ast.expn.Expn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.semantics.SemanticErrorException;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.codegen.CodeGenErrorException;

/**
 * Holds the assignment of an expression to a variable.
 */
public class AssignStmt extends Stmt {
	/*
	 * lval is the location being assigned to, and rval is the value being
	 * assigned.
	 */
	private Expn lval, rval;

	public AssignStmt(Integer lineNumber, Expn lval, Expn rval) {
		super(lineNumber);
		this.lval = lval;
		this.rval = rval;
	}

	/** Returns a string that describes the assignment statement. */
	@Override
	public String toString() {
		return "Assignment: " + lval + " := " + rval;
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		lval.doSemantics();
		rval.doSemantics();

		if (!(lval.getResultType().getClass().equals(rval.getResultType().getClass()))) {
			throw new SemanticErrorException("lval and rval types do not match");
		}
	}

	public Expn getLval() {
		return lval;
	}

	public void setLval(Expn lval) {
		this.lval = lval;
	}

	public Expn getRval() {
		return rval;
	}

	public void setRval(Expn rval) {
		this.rval = rval;
	}
	
	@Override
	public void doCodeGen() throws CodeGenErrorException {
	    try {
	    	if (lval instanceof IdentExpn || lval instanceof SubsExpn) {
				lval.doCodeGenLHS();
			} else {
				lval.doCodeGen();
			}

	        rval.doCodeGen();

	        Machine.writeMemory(Main.codeGenAddr++, Machine.STORE);
	    }
	    catch (Exception e) {
	        throw new CodeGenErrorException(e.getMessage());
	    }
	}
}
