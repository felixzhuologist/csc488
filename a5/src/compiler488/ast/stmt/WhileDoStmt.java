package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.codegen.CodeGenErrorException;
import compiler488.codegen.Utils;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;


/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {

	public WhileDoStmt(Integer lineNumber, Stmt body, Expn expn) {
		super(lineNumber);
		this.body = body;
		this.expn = expn;
	}

	/**
	 * Print a description of the <b>while-do</b> construct.
	 *
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "while " + expn + " do");
		body.printOn(out, depth + 1);
		Indentable.printIndentOnLn(out, depth, "End while-do");
	}


	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			// save the current address as the address of the loop label
			short loopLabelAddr = Main.codeGenAddr;

			// while (expn)
			expn.doCodeGen();
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);

			// save the address of the instruction so we can patch its value later
			short labelAfterInstrAddr = Main.codeGenAddr;
			// push address to branch to if false, is undefined right now, will be patched later
			Machine.writeMemory(Main.codeGenAddr++, Machine.UNDEFINED);
			Machine.writeMemory(Main.codeGenAddr++, Machine.BF);

			// now the body
			Main.currNumLoops++;
			body.doCodeGen();
			Main.currNumLoops--;

			// push the address of the loop label, so we branch back to it after each iteration
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, loopLabelAddr);
			Machine.writeMemory(Main.codeGenAddr++, Machine.BR);

			// now we can patch the address of "after the while block"
			Utils.patch(labelAfterInstrAddr, Main.codeGenAddr);

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
