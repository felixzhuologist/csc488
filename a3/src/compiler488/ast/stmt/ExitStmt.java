package compiler488.ast.stmt;

import compiler488.ast.expn.*;

/**
 * Represents the command to exit from a loop.
 */

public class ExitStmt extends Stmt {

	// condition for 'exit when'
  private Expn expn = null;
	private Integer level = -1;
	private boolean isInsideLoop = false;

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

	// Mark this node as inside a loop
	public void markInsideLoop() {
		this.isInsideLoop = true;
	}
	
	@Override
    public void doSemantics() throws Exception {
	    if (expn != null && !(expn instanceof BoolExpn)) {
	        throw new Exception();
	    }
	}

}
