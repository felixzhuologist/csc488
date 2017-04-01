package compiler488.ast.expn;

import compiler488.ast.AST;
import compiler488.ast.type.Type;
import compiler488.ast.Printable;

/**
 * A placeholder for all expressions.
 */
public class Expn extends AST implements Printable {
    /** The type that the Expn evaluates to. */
    protected Type resultType=null;

    public Expn(Integer lineNumber) {
        super(lineNumber);
    }

    public void setResultType(Type resultType) {
      this.resultType = resultType;
    }

    public Type getResultType() {
      return this.resultType;
    }
}
