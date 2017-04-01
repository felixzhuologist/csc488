package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Printable;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.Expn;
import compiler488.codegen.CodeGenErrorException;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.SemanticErrorException;

import java.lang.reflect.Method;
import java.util.ListIterator;

/**
 * The command to write data on the output device.
 */
public class WriteStmt extends Stmt {
	private ASTList<Printable> outputs; // The objects to be printed.

	public WriteStmt (Integer lineNumber) {
		super(lineNumber);
		outputs = new ASTList<Printable> ();
	}

	public WriteStmt(Integer lineNumber, ASTList<Printable> outputs) {
		super(lineNumber);
		this.outputs = outputs;
	}

	/** Returns a description of the <b>write</b> statement. */
	@Override
	public String toString() {
		return "write " + outputs;
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		this.outputs.doSemantics();

		// Check that each output is text, newline, or an int expression
		ListIterator<Printable> outputIter = outputs.getIter();
		while (outputIter.hasNext()) {
			Printable output = outputIter.next();
			if (!(output instanceof SkipConstExpn || output instanceof TextConstExpn || 
						((Expn) output).getResultType() instanceof IntegerType)) {
				throw new SemanticErrorException("Write output must be text, newline, or integer");
			}
		}
	}
	
	public ASTList<Printable> getOutputs() {
		return outputs;
	}

	public void setOutputs(ASTList<Printable> outputs) {
		this.outputs = outputs;
	}

	public void doCodeGen() throws CodeGenErrorException {
		ListIterator<Printable> outputIter = outputs.getIter();
		while (outputIter.hasNext()) {
			try {
				Printable output = outputIter.next();
				if (output instanceof TextConstExpn) {
					// print each character individually
					for (char c: ((TextConstExpn) output).getValue().toCharArray()) {
						Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
						Machine.writeMemory(Main.codeGenAddr++, (short) c);
						Machine.writeMemory(Main.codeGenAddr++, Machine.PRINTC);
					}
				} else if (output instanceof SkipConstExpn) {
					((SkipConstExpn) output).doCodeGen();
					Machine.writeMemory(Main.codeGenAddr++, Machine.PRINTC);
				} else {
					Method method = output.getClass().getMethod("doCodeGen");
					method.invoke(output);
					// check the return type of output -> either int or bool
//					if (((Expn) output).getResultType() instanceof IntegerType) {
//						// doing code gen on the expression will push its result onto the stack
////						((Expn) output).doCodeGen();

						Machine.writeMemory(Main.codeGenAddr++, Machine.PRINTI);
//					} else if (((Expn) output).getResultType() instanceof BooleanType) {
//
//						Machine.writeMemory(Main.codeGenAddr++, Machine.PRINTI);
//
//					}
				}
			} catch (Exception e) {
				throw new CodeGenErrorException(e.getMessage());
			}
		}
		// outputs.doCodeGen();
	}
}
