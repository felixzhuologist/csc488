package compiler488.ast.stmt;

import compiler488.ast.expn.*;


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
      System.out.println("ok");
      if (!(expn instanceof BoolExpn || expn instanceof BoolConstExpn ||
            expn instanceof CompareExpn || expn instanceof EqualsExpn ||
            expn instanceof NotExpn)) {
          // TODO: check for FunctionCallExpn && return type is boolean
          throw new Exception();
      }
  }

}
