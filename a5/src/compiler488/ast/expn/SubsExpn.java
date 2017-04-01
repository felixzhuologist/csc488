package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.ast.type.*;
import compiler488.semantics.Util;
import compiler488.semantics.SemanticErrorException;

/**
 * References to an array element variable
 * 
 * Treat array subscript operation as a special form of unary expression.
 * operand must be an integer expression
 */
public class SubsExpn extends UnaryExpn implements Readable {

	private String variable; // name of the array variable

	public SubsExpn(Integer lineNumber, String variable, Expn operand) {
		super(lineNumber);
		this.variable = variable;
		this.operand = operand;
	}


	/** Returns a string that represents the array subscript. */
	@Override
	public String toString() {
		return (variable + "[" + operand + "]");
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		operand.doSemantics();
		if (!(operand.getResultType() instanceof IntegerType)) {
			throw new SemanticErrorException("Indexing into array with non integer type");
		}

    SymbolTableEntry entry = Main.symbolTable.getEntry(variable);
    if (entry == null || !(entry instanceof ArraySymbol)) {
      throw new SemanticErrorException("Reference to undeclared array variable " + variable);
    }

    ArraySymbol arrEntry = (ArraySymbol) entry;
    this.resultType = Util.getTypeWithLineNumber(arrEntry.getType(), lineNumber);
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

}
