package compiler488.ast.decl;

import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDeclPart extends DeclarationPart {

	public ScalarDeclPart(Integer lineNumber, String name) {
		super(lineNumber);
		this.name = name;
	}

	/**
	 * Returns a string describing the name of the object being
	 * declared.
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		SymbolTableEntry newSymbol = new ScalarSymbol(this.name);
		Main.symbolTable.addEntry(newSymbol);
	}
}
