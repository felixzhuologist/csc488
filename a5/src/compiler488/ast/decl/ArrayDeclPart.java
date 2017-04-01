package compiler488.ast.decl;

import compiler488.ast.type.*;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

/**
 * Holds the declaration part of an array.
 */
public class ArrayDeclPart extends DeclarationPart {

	/* The lower and upper boundaries of the array. */
        private Integer lb, ub;


	/* The number of objects the array holds. */
	private Integer size;

	private Type type;

	public ArrayDeclPart(Integer lineNumber, String name, Integer lb, Integer ub) {
		super(lineNumber);
		this.name = name;
		this.lb = lb;
		this.ub = ub;
		this.size = ub - lb + 1;
	}

	public ArrayDeclPart(Integer lineNumber, String name, Integer ub) {
		super(lineNumber);
		this.name = name;
		this.lb = 1;
		this.ub = ub;
		this.size = ub - lb + 1;
	}

	/**
	 * Returns a string that describes the array.
	 */
	@Override
	public String toString() {
		return name + "[" + lb + ".." + ub + "]";
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		if (this.lb > this.ub) {
			throw new SemanticErrorException("Lower bound is greater than upper bound in array decl");
		}
		SymbolTableEntry newSymbol = new ArraySymbol(this.name, this.type, this.lb, this.ub, this.size);
		Main.symbolTable.addEntry(newSymbol);
	}

	public Integer getSize() {
		return size;
	}


	public Integer getLowerBoundary() {
		return lb;
	}

	public Integer getUpperBoundary() {
		return ub;
	}

        public void setLowerBoundary(Integer lb) {
		this.lb = lb;
	}

        public void setUpperBoundary(Integer ub) {
		this.ub = ub;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
