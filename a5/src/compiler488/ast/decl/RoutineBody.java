package compiler488.ast.decl;

import java.io.PrintStream;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.stmt.Scope;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;
import compiler488.codegen.CodeGenErrorException;
import compiler488.runtime.Machine;

/**
 * Represents the parameters and instructions associated with a
 * function or procedure.
 */
public class RoutineBody extends Indentable {
	private ASTList<ScalarDecl> parameters; // The formal parameters of the routine.

	private Scope body; // Execute this scope when routine is called.

	public RoutineBody(Integer lineNumber, ASTList<ScalarDecl> parameters, Scope body) {
		super(lineNumber);
		this.parameters = parameters;
		this.body = body;
	}

	/* Constructor for routines with no parameters. */
	public RoutineBody(Integer lineNumber, Scope body) {
		super(lineNumber);
		this.parameters = new ASTList<ScalarDecl>();
		this.body = body;
	}
	/**
	 * Print a description of the formal parameters and the scope for this
	 * routine.
	 * 
	 * @param out
	 *			Where to print the description.
	 * @param depth
	 *			How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		if (parameters != null)
			out.println("(" + parameters + ")");
		else
			out.println(" ");
		body.printOn(out, depth);
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		this.parameters.doSemantics();
		this.body.doSemantics();
	}
	
	public void doCodeGenWithReturn(boolean hasReturn) throws CodeGenErrorException {  
		try {
			// Save space for parameters
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, (short)0);

			// get total # of parameters to be allocated
			Integer totalAllocation = parameters.size();
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, totalAllocation.shortValue());

			Machine.writeMemory(Main.codeGenAddr++, Machine.DUPN);

			// Generate Scope
			if (hasReturn) {
				this.body.doCodeGen(hasReturn, totalAllocation.shortValue());
			}
			else 
			{
				this.body.doCodeGen(false,  totalAllocation.shortValue());
			}
		}
		catch (Exception e) {
			System.out.println("Thrown in RoutineBody");
			throw new CodeGenErrorException(e.getMessage());
		}
	}
	
	public Integer getAllocationSize() {
		int totalAllocation = this.parameters.size();
		totalAllocation += this.body.getAllocationSize();

		return totalAllocation;
	}
	
// 	public void doCodeGenWithReturnAddr(short address) throws CodeGenErrorException {  
// 
// 		try {
// 			// Save space for parameters
// 			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
// 			Machine.writeMemory(Main.codeGenAddr++, (short)0);
// 
// 			// get total # of parameters to be allocated
// 			Integer totalAllocation = parameters.size();
// 			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
// 			Machine.writeMemory(Main.codeGenAddr++, totalAllocation.shortValue());
// 
// 			Machine.writeMemory(Main.codeGenAddr++, Machine.DUPN);
// 			
// 			// Save space for return value
// 			if (address >= 0) { 
// 				Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
// 				Machine.writeMemory(Main.codeGenAddr++, (short)0);
// 			}
// 		}
// 		catch (Exception e) {
// 			System.out.println("Thrown in RoutineBody");
// 			throw new CodeGenErrorException(e.getMessage());
// 		}
// 	
// 		// Generate Scope
// 		if (address >= 0) {
// 			this.body.doCodeGen(true);
// 		}
// 		else 
// 		{
// 			this.body.doCodeGen(false);
// 		}
// 		
// 		// Set return value
// 		try {
// 			if (address >= 0) { 
// 				// Load return value address
// 				Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
// 				Machine.writeMemory(Main.codeGenAddr++, (short)address);
// 			
// 				// Swap Return address and return value from line 102, and store
// 				Machine.writeMemory(Main.codeGenAddr++, Machine.SWAP);
// 				Machine.writeMemory(Main.codeGenAddr++, Machine.STORE);
// 			}
// 		}
// 		catch (Exception e) {
// 			throw new CodeGenErrorException(e.getMessage());
// 		}
// 	}
	

	public Scope getBody() {
		return body;
	}

	public void setBody(Scope body) {
		this.body = body;
	}

	public ASTList<ScalarDecl> getParameters() {
		return parameters;
	}

	public void setParameters(ASTList<ScalarDecl> parameters) {
		this.parameters = parameters;
	}
}
