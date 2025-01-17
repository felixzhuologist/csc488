package compiler488.ast.decl;

import java.io.PrintStream;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import java.util.ListIterator;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

/**
 * Holds the declaration of multiple elements.
 */
public class MultiDeclarations extends Declaration {
	/* The elements being declared */
	private ASTList<DeclarationPart> elements;

	public MultiDeclarations (Integer lineNumber) {
		super(lineNumber);
		elements = new ASTList<DeclarationPart> ();
	}

	public MultiDeclarations(Integer lineNumber, ASTList<DeclarationPart> elements, Type type) {
		super(lineNumber);
		this.elements = elements;
		this.type = type;
	}

	/**
	 * Returns a string that describes the array.
	 */
	@Override
	public String toString() {
		return  elements + " : " + type ;
	}


	/**
	 * Print the multiple declarations of the same type.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		out.println(elements);
		Indentable.printIndentOn (out, depth, this + " ");
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		elements.doSemantics();

		// Set type for all of its elements
		ListIterator<DeclarationPart> elementsIter = elements.getIter();
		while (elementsIter.hasNext()) {
			String elementName = elementsIter.next().getName();
			VariableSymbol elementSymbolTableEntry =  (VariableSymbol) Main.symbolTable.getEntry(elementName);
			elementSymbolTableEntry.setType(this.type);
		}
	}

	public ASTList<DeclarationPart> getElements() {
		return elements;
	}

	public void setElements(ASTList<DeclarationPart> elements) {
		this.elements = elements;
	}
}
