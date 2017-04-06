package compiler488.ast.stmt;

import compiler488.ast.AST;
import compiler488.ast.expn.*;
import compiler488.ast.type.BooleanType;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;
import compiler488.codegen.Utils;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.codegen.CodeGenErrorException;

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
    public void doSemantics() throws SemanticErrorException {
	    if (expn != null) {
			    this.expn.doSemantics(); 
	    		if (!(expn.getResultType() instanceof BooleanType)) {
			        throw new SemanticErrorException("Expected exit condition that evaluates to boolean");
			    }
	    }

	    if (level < 0) {
	    	throw new SemanticErrorException("Invalid break depth");
	    }
	    if (level > Main.currNumLoops) {
	    	throw new SemanticErrorException("Attempt to break out of " + level + " loops when " +
	    											"current depth is only " + Main.currNumLoops);
	    }
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			if (expn != null) {
				// generate if where the if block is just exit statement codegen 
				// (without a condition)
	      Utils.generateIfCode(expn, (AST) (new ExitStmt(lineNumber, level)), null);
			}
			else {
				// generate branching code
				Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
				// save address to be patched later
				Integer labelAfterLoop = new Integer(Main.codeGenAddr);
				Machine.writeMemory(Main.codeGenAddr++, Machine.UNDEFINED);
				Machine.writeMemory(Main.codeGenAddr++, Machine.BR);

				// save information so that loops know which address to patch
				Integer levelOfLoopToJumpOutOf = new Integer(Main.currNumLoops - level);
				Integer previousLabel = Main.loopLvlToPatchAddr.get(levelOfLoopToJumpOutOf);
				if (previousLabel == null) { // want to exit at the first exit statement
					Main.loopLvlToPatchAddr.put(levelOfLoopToJumpOutOf, labelAfterLoop);
				}
			}
		} catch (MemoryAddressException e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
}
