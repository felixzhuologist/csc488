package compiler488.ast.stmt;

import compiler488.ast.expn.*;
import compiler488.semantics.Util;
import compiler488.compiler.Main;

/**
 * Represents the common parts of loops.
 */
public abstract class LoopingStmt extends Stmt
{
    protected Stmt body ;	  // body of ther loop
    protected Expn expn;          // Loop condition

    public LoopingStmt(Integer lineNumber) {
    	super(lineNumber);
    }

    public Expn getExpn() {
		return expn;
	}

	public void setExpn(Expn expn) {
		this.expn = expn;
	}

	public Stmt getBody() {
		return body;
	}

	public void setBody(Stmt body) {
		this.body = body;
	}

  @Override
  public void doSemantics() throws Exception {
      // expn semantics:
      if (!Util.expnEvaluatesToBool(expn)) {
          // TODO: check for FunctionCallExpn && return type is boolean
          throw new Exception("Expected loop condition that evaluates to boolean but got " + 
                              expn.getClass().getName() + 
                              " instead");
      }
      expn.doSemantics();

      // body semantics:
      Main.currNumLoops++;
      this.body.doSemantics();
      Main.currNumLoops--;
  }

}
