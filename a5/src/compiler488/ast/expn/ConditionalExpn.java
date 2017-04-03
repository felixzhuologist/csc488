package compiler488.ast.expn;

import compiler488.ast.type.*;
import compiler488.codegen.CodeGenErrorException;
import compiler488.codegen.Utils;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.Util;
import compiler488.semantics.SemanticErrorException;

/** Represents a conditional expression (i.e., x>0?3:4). */
public class ConditionalExpn extends Expn {
	private Expn condition; // Evaluate this to decide which value to yield.

	private Expn trueValue; // The value is this when the condition is true.

	private Expn falseValue; // Otherwise, the value is this.

	public ConditionalExpn(Integer lineNumber, Expn condition, Expn trueValue, Expn falseValue) {
		super(lineNumber);
		this.condition = condition;
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}

	/** Returns a string that describes the conditional expression. */
	@Override
	public String toString() {
		return "(" + condition + " ? " + trueValue + " : " + falseValue + ")";
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		// Children do semantics first to set their resultTypes
		condition.doSemantics();
		trueValue.doSemantics();
		falseValue.doSemantics();

		if (!(condition.getResultType() instanceof BooleanType)) {
			throw new SemanticErrorException("Condition in conditional does not evaluate to boolean");
		}

		if (!(trueValue.getResultType().getClass()).equals(falseValue.getResultType().getClass())) {
			throw new SemanticErrorException("Statement types in conditional do not match");
		}

		this.resultType = Util.getTypeWithLineNumber(trueValue.getResultType(), lineNumber);
	}

	public Expn getCondition() {
		return condition;
	}

	public void setCondition(Expn condition) {
		this.condition = condition;
	}

	public Expn getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(Expn falseValue) {
		this.falseValue = falseValue;
	}

	public Expn getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(Expn trueValue) {
		this.trueValue = trueValue;
	}



	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			Utils.generateIfCode(condition, trueValue, falseValue);
		} catch (MemoryAddressException e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
}
