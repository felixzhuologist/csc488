package compiler488.ast.stmt;

import compiler488.ast.expn.*;
import compiler488.semantics.Util;
import compiler488.compiler.Main;

/**
 * Represents the command to exit from a loop.
 */

public class ExitStmt extends Stmt {

	// condition for 'exit when'
  private Expn expn = null;
	private Integer level = 1; // exit from 1 loop by default

	public ExitStmt(Integer lineNumber, Integer level, Expn expn) { // exit integer when expression
		super(lineNumber);
		this.level = level;
		this.expn = expn;
	}

	public ExitStmt(Integer lineNumber, Expn expn) { // exit when expression
		super(lineNumber);
		this.expn = expn;
	}

	public ExitStmt(Integer lineNumber, Integer level) { // exit integer
		super(lineNumber);
		this.level = level;
	}

	public ExitStmt(Integer lineNumber) { // exit
		super(lineNumber);
	}

	/** Returns the string <b>"exit"</b> or <b>"exit when e"</b>" 
            or  <b>"exit"</b> level  or  <b>"exit"</b> level  when e 
	*/
	@Override
	public String toString() {
		  {
		    String stmt = "exit " ;
	 	    if( level >= 0 )
			stmt = stmt + level + " " ;
                    if( expn != null )
		        stmt = stmt + "when " + expn + " " ;
		    return stmt ;
		  }
	}

	public Expn getExpn() {
		return expn;
	}

	public void setExpn(Expn expn) {
		this.expn = expn;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
    public void doSemantics() throws Exception {
	    if (expn != null) {
	    		if (!Util.expnEvaluatesToBool(expn)) {
			        throw new Exception("Expected exit condition that evaluates to boolean but got " + 
		                              expn.getClass().getName() + 
		                              " instead");
			    }
			    this.expn.doSemantics(); 
	    }

	    if (level < 0) {
	    	throw new Exception("Invalid break depth");
	    }
	    if (level > Main.currNumLoops) {
	    	throw new Exception("Attempt to break out of " + level + " loops when " +
	    											"current depth is only " + Main.currNumLoops);
	    }
	}

}
