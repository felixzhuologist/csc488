package compiler488.ast.decl;

import compiler488.ast.type.Type;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

/**
 * Represents the declaration of a parameter.
 */

public class ScalarDecl extends Declaration {

	public ScalarDecl(Integer lineNumber, Type type, String name) {
		super(lineNumber);
		this.type = type;
		this.name = name;
	}
	/**
	 * Returns a string describing the name and type of the object being
	 * declared.
	 */
	@Override
	public String toString() {
		return  name + " : " + type ;
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		SymbolTableEntry newSymbol = new ScalarSymbol(this.name, this.type);
		Main.symbolTable.addEntry(newSymbol);			
	}
}
