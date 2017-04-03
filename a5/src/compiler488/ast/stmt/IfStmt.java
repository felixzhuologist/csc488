package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.BooleanType;
import compiler488.codegen.CodeGenErrorException;
import compiler488.codegen.Utils;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.SemanticErrorException;
import compiler488.ast.type.BooleanType;

/**
 * Represents an if-then or an if-then-else construct.
 */
public class IfStmt extends Stmt {
	// The condition that determines which branch to execute.
	private Expn condition;

	// Represents the statement to execute when the condition is true.
	private Stmt whenTrue;

	// Represents the statement to execute when the condition is false.
	private Stmt whenFalse = null;


	public IfStmt(Integer lineNumber, Expn condition, Stmt whenTrue) {
		super(lineNumber);
		this.condition = condition;
		this.whenTrue = whenTrue;
	}

	public IfStmt(Integer lineNumber, Expn condition, Stmt whenTrue, Stmt whenFalse) {
		super(lineNumber);
		this.condition = condition;
		this.whenTrue = whenTrue;
		this.whenFalse = whenFalse;
	}

	/**
	 * Print a description of the <b>if-then-else</b> construct. If the
	 * <b>else</b> part is empty, just print an <b>if-then</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "if " + condition + " then ");
		whenTrue.printOn(out, 	depth + 1);
		if (whenFalse != null) {
			Indentable.printIndentOnLn(out, depth, "else");
			whenFalse.printOn(out, depth + 1);
		}
		Indentable.printIndentOnLn(out, depth, "End if");
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		condition.doSemantics();
		
		if (!(condition.getResultType() instanceof BooleanType)) {
			throw new SemanticErrorException("If condition must evaluate to boolean");
		}

		whenTrue.doSemantics();
		if (whenFalse != null) {
			whenFalse.doSemantics();
		}
	}

	public Expn getCondition() {
		return condition;
	}

	public void setCondition(Expn condition) {
		this.condition = condition;
	}

	public Stmt getWhenFalse() {
		return whenFalse;
	}

	public void setWhenFalse(Stmt whenFalse) {
		this.whenFalse = whenFalse;
	}

	public Stmt getWhenTrue() {
		return whenTrue;
	}

	public void setWhenTrue(Stmt whenTrue) {
		this.whenTrue = whenTrue;
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			Utils.generateIfCode(condition, whenTrue, whenFalse);
//
//			// if (condition)
//			condition.doCodeGen();
//			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
//
//			// save the address of the instruction so we can patch it later
//			short labelFalseInstrAddr = Main.codeGenAddr;
//			// push address to branch to if false, is undefined right now, will be patched later
//			Machine.writeMemory(Main.codeGenAddr++, Machine.UNDEFINED);
//			Machine.writeMemory(Main.codeGenAddr++, Machine.BF);
//
//			// if true then:
//			whenTrue.doCodeGen();
//			// need to push the address to branch to, so that it skips the else part
//			//	PUSH label_after
//			//	BR
//			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
//
//			if (whenFalse != null) {
//				// save the address of the instruction so we can patch it later
//				short labelAfterInstrAddr = Main.codeGenAddr;
//				Machine.writeMemory(Main.codeGenAddr++, Machine.UNDEFINED);
//				Machine.writeMemory(Main.codeGenAddr++, Machine.BR);
//
//				// the current address that we have right now is the address to branch to if the condition is false, so
//				// we can patch this now
//				Utils.patch(labelFalseInstrAddr, Main.codeGenAddr);
//
//				// now the else part
//				whenFalse.doCodeGen();
//
//				// now we can patch the address of "after the if block"
//				Utils.patch(labelAfterInstrAddr, Main.codeGenAddr);
//			} else {
//				// just patch the false address so it goes to after the if block if the condition is false
//				Utils.patch(labelFalseInstrAddr, Main.codeGenAddr);
//			}

		} catch (MemoryAddressException e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
}
