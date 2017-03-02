package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.ast.type.*;
import compiler488.semantics.Util;

/**
 *  References to a scalar variable.
 */
public class IdentExpn extends Expn implements Readable
    {
    private String ident;  	// name of the identifier

	public IdentExpn(Integer lineNumber, String ident) {
		super(lineNumber);
		this.ident = ident;
	}

		/**
     * Returns the name of the variable or function.
     */
    @Override
	public String toString () { return ident; }

  @Override
  public void doSemantics() throws Exception {
    SymbolTableEntry entry = Main.symbolTable.getEntry(ident);
    if (entry == null || !(entry instanceof ScalarSymbol)) {
      throw new Exception("Reference to undeclared variable " + ident);
    }

    ScalarSymbol identEntry = (ScalarSymbol) entry;
    this.resultType = Util.getTypeWithLineNumber(identEntry.getType(), lineNumber);
  }

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}
}
