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

/**
 * Represents a loop in which the exit condition is evaluated after each pass.
 */
public class RepeatUntilStmt extends LoopingStmt {

	public RepeatUntilStmt(Integer lineNumber, Stmt body, Expn expn) {
		super(lineNumber);
		this.body = body;
		this.expn = expn;
	}

	/**
	 * Print a description of the <b>repeat-until</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "repeat");
		body.printOn(out, depth + 1);
		Indentable.printIndentOnLn(out, depth, " until "  + expn );

	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			// save the current address as the address of the loop label
			short loopLabelAddr = Main.codeGenAddr;

			// first the body
			Main.currNumLoops++;
			body.doCodeGen();
			Main.currNumLoops--;

			// repeat {...} until (expn)
			expn.doCodeGen();

			// push the address of the loop label, so we branch back to it after each iteration if expn is false
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, loopLabelAddr);
			Machine.writeMemory(Main.codeGenAddr++, Machine.BF);

			// check if this loop needs to patch any addresses for exit statements
			Integer numLoops = new Integer(Main.currNumLoops);
			Integer exitLoopLabel = Main.loopLvlToPatchAddr.get(numLoops);
			if (exitLoopLabel != null) {
				Utils.patch(exitLoopLabel.shortValue(), Main.codeGenAddr);
				Main.loopLvlToPatchAddr.remove(numLoops);
			}
		} catch (MemoryAddressException e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
}
