package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.ast.type.*;
import compiler488.semantics.Util;
import compiler488.semantics.SemanticErrorException;

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
  public void doSemantics() throws SemanticErrorException {
    SymbolTableEntry entry = Main.symbolTable.getEntry(ident);
    if (entry == null) {
      throw new SemanticErrorException("Reference to undeclared variable " + ident);
    }

    if (entry instanceof ScalarSymbol) {
      ScalarSymbol identEntry = (ScalarSymbol) entry;
      this.resultType = Util.getTypeWithLineNumber(identEntry.getType(), lineNumber);
    }
    else if (entry instanceof FunctionSymbol) {
      FunctionSymbol identEntry = (FunctionSymbol) entry;

      if (identEntry.getNumParams() != 0) {
        throw new SemanticErrorException("Function " + ident + " was called with no " +
                          "args but expected " + identEntry.getParamTypes().size());         }

      this.resultType = identEntry.getReturnType();
    }
    else {
      throw new SemanticErrorException("variable is not a scalar symbol or function symbol " + ident);
    }
  }

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}
}
