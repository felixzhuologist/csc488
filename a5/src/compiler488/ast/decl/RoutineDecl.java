package compiler488.ast.decl;

import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;
import compiler488.codegen.CodeGenErrorException;
import compiler488.runtime.Machine;

import java.util.ArrayList;
import java.util.ListIterator;
import java.io.PrintStream;

/**
 * Represents the declaration of a function or procedure.
 */
public class RoutineDecl extends Declaration {
	/*
	 * The formal parameters of the function/procedure and the
	 * statements to execute when the procedure is called.
	 */
	private RoutineBody routineBody;
	// used to calculate the memory address of the variable
	private int lexicalLevel;   // lexical level of symbol table entry
	private int index;		  // index of symbol table entry

	public RoutineDecl(Integer lineNumber, String name, RoutineBody routineBody) {
		super(lineNumber);
		this.name = name;
		this.routineBody = routineBody;
		this.index = 1;
		this.lexicalLevel = -1;
	}

	public RoutineDecl(Integer lineNumber, String name, RoutineBody routineBody, Type type) {
		super(lineNumber);
		this.name = name;
		this.routineBody = routineBody;
		this.type = type;
		this.index = 1;
		this.lexicalLevel = -1;
	}

	/**
	 * Returns a string indicating that this is a function with
	 * return type or a procedure, name, Type parameters, if any,
	 * are listed later by routineBody
	 */
	@Override
	public String toString() {
	  if(type==null)
		{
		  return " procedure " + name;
		}
	  else
		{
		  return " function " + name + " : " + type ;
		}
	}

	/**
	 * Prints a description of the function/procedure.
	 * 
	 * @param out
	 *			Where to print the description.
	 * @param depth
	 *			How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOn(out, depth, this + " ");
		routineBody.printOn(out, depth);
	}

	@Override
	public void doSemantics() throws SemanticErrorException {
		RoutineSymbol routineSymbol;

		// Add function to symbol table: must be done first to support recursion
		ListIterator<ScalarDecl> params = this.routineBody.getParameters().getIter();
		ArrayList<Type> paramTypes = this.getTypesFromParams(params);
		if (type == null) {
			routineSymbol = new ProcedureSymbol(this.name, paramTypes);
		} else {
			routineSymbol = new FunctionSymbol(this.name, paramTypes, this.type);
		}

		Main.symbolTable.addEntry(routineSymbol);
		Main.routineStack.push(routineSymbol);
		
		this.lexicalLevel = routineSymbol.getDepth();
		this.index = routineSymbol.getIndex();
		
		Main.symbolTable.openScope();

		this.routineBody.doSemantics();

		Main.symbolTable.closeScope();
		Main.routineStack.pop();
	}

    @Override
	public Integer getAllocationSize() {
		return 1 + this.routineBody.getAllocationSize();
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {  
	 
		try {
			// Activation record start
			// Return address is at the top of the stack from FunctionCallExpn

			if (this.type != null) 
			{
				short returnValueAddr = Main.codeGenAddr;
				returnValueAddr -= (short)2;
				// Let the body handle scope and reserving space for var, and parameters
				this.routineBody.doCodeGenWithReturnAddr(returnValueAddr);
			}
			else {
				// Let the body handle scope and reserving space for var, and parameters
				this.routineBody.doCodeGen();
			}

			if (this.lexicalLevel > 0) {
				Machine.writeMemory(Main.codeGenAddr++, Machine.PUSHMT);
				Machine.writeMemory(Main.codeGenAddr++, Machine.ADDR);
				Machine.writeMemory(Main.codeGenAddr++, (short)this.lexicalLevel);
				Machine.writeMemory(Main.codeGenAddr++, (short)0);
				Machine.writeMemory(Main.codeGenAddr++, Machine.SUB);
				Machine.writeMemory(Main.codeGenAddr++, Machine.POPN);
				//Machine.writeMemory(Main.codeGenAddr++, Machine.SETD);
				//Machine.writeMemory(Main.codeGenAddr++, (short)(this.lexicalLevel - 1));

				Machine.writeMemory(Main.codeGenAddr++, Machine.BR);
			}
		}
		catch (Exception e) {
			System.out.println("Thrown in RoutineDecl");
			throw new CodeGenErrorException(e.getMessage());
		}
	}

	public RoutineBody getRoutineBody() {
		return routineBody;
	}

	public void setRoutineBody(RoutineBody routineBody) {
		this.routineBody = routineBody;
	}

	private ArrayList<Type> getTypesFromParams(ListIterator<ScalarDecl> params) {
		ArrayList<Type> paramTypes = new ArrayList<Type>();
		while (params.hasNext()) {
			paramTypes.add(params.next().getType());
		}
		return paramTypes;
	}
}
