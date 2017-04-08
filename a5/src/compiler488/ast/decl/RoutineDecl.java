package compiler488.ast.decl;

import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

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

		this.lexicalLevel = routineSymbol.getDepth();
		this.index = routineSymbol.getIndex();

		Main.symbolTable.addEntry(routineSymbol);
		Main.routineStack.push(routineSymbol);
		Main.symbolTable.openScope();

		this.routineBody.doSemantics();

		Main.symbolTable.closeScope();
		Main.routineStack.pop();
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {  
	 
		try {
			// Let the body handle scope and reserving space for var, and parameters
			this.routineBody.doCodeGen();
// 			
// 			// Save space for return value 
// 			if(this.type != null)
// 			{
// 				Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
// 				Machine.writeMemory(Main.codeGenAddr++, (float)0);
// 			}
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
