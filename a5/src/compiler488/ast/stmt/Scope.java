package compiler488.ast.stmt;

import java.io.PrintStream;
import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.codegen.CodeGenErrorException;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;
import compiler488.runtime.Machine;

/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt {
	protected ASTList<Declaration> declarations; // The declarations at the top.

	protected ASTList<Stmt> statements; // The statements to execute.
	
	private int lexicalLevel = -1;

	public Scope(Integer lineNumber, ASTList<Stmt> statements) {
		super(lineNumber);
		this.declarations = new ASTList<Declaration>();
		this.statements = statements;
	}

	public Scope(Integer lineNumber) {
		super(lineNumber);
		this.declarations = new ASTList<Declaration>();
		this.statements = new ASTList<Stmt>();
	}

	public Scope(Integer lineNumber, ASTList<Declaration> declarations, ASTList<Stmt> statements) {
		super(lineNumber);
		this.declarations = declarations;
		this.statements = statements;
	}

	/**
	 * Print a description of the <b>scope</b> construct.
	 * 
	 * @param out
	 *			Where to print the description.
	 * @param depth
	 *			How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "Scope");
		Indentable.printIndentOnLn(out, depth, "declarations");

		declarations.printOnSeperateLines(out, depth + 1);

		Indentable.printIndentOnLn(out, depth, "statements");

		statements.printOnSeperateLines(out, depth + 1);

		Indentable.printIndentOnLn(out, depth, "End Scope");
	}

	public ASTList<Declaration> getDeclarations() {
		return declarations;
	}

	public ASTList<Stmt> getStatements() {
		return statements;
	}

	public void setDeclarations(ASTList<Declaration> declarations) {
		this.declarations = declarations;
	}

	public void setStatements(ASTList<Stmt> statements) {
		this.statements = statements;
	}
	
	public Integer getAllocationSize() {
	    
	    Integer totalAllocation = 0;
        ListIterator<Declaration> decIter = declarations.getIter();
        while (decIter.hasNext()) {
            Declaration nextDeclaration = decIter.next();
            totalAllocation += nextDeclaration.getAllocationSize();
        }
        
        return totalAllocation;
	}
	
	@Override
	public void doSemantics() throws SemanticErrorException {
		Main.symbolTable.openScope();
		this.lexicalLevel = Main.symbolTable.currentDepth();
		this.declarations.doSemantics();
		this.statements.doSemantics();
		Main.symbolTable.closeScope();
	}

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		try {
			doCodeGen(false, 0);
		}
		catch (Exception e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
	
	public void doCodeGen(boolean doesReturn, int numParams) throws CodeGenErrorException {
		try {
			if (this.lexicalLevel > 0) {
				short prevDisp = Main.codeGenAddr;
				Machine.writeMemory(Main.codeGenAddr++, Machine.ADDR);
				Machine.writeMemory(Main.codeGenAddr++, (short)(this.lexicalLevel - 1));
				Machine.writeMemory(Main.codeGenAddr++, (short)0);
				Machine.writeMemory(Main.codeGenAddr++, Machine.PUSHMT);
				Machine.writeMemory(Main.codeGenAddr++, Machine.SETD);
				Machine.writeMemory(Main.codeGenAddr++, (short)this.lexicalLevel);
			}
			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, (short)0);

			// get total size to be allocated
			Integer totalAllocation = this.getAllocationSize();

			Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
			Machine.writeMemory(Main.codeGenAddr++, totalAllocation.shortValue());

			Machine.writeMemory(Main.codeGenAddr++, Machine.DUPN);
		}
		catch (Exception e) {
			throw new CodeGenErrorException(e.getMessage());
		}

		declarations.doCodeGen();
		statements.doCodeGen();

		try {
			if (this.lexicalLevel > 0) {
			
				if (doesReturn)
				{
				    Integer returnAddress = -numParams-2;
				
					// Set Return Value
					Machine.writeMemory(Main.codeGenAddr++, Machine.ADDR);
					Machine.writeMemory(Main.codeGenAddr++, (short)(this.lexicalLevel));
					Machine.writeMemory(Main.codeGenAddr++, returnAddress.shortValue());
					Machine.writeMemory(Main.codeGenAddr++, Machine.SWAP);
					Machine.writeMemory(Main.codeGenAddr++, Machine.STORE);
				}

				Machine.writeMemory(Main.codeGenAddr++, Machine.PUSHMT);
				Machine.writeMemory(Main.codeGenAddr++, Machine.ADDR);
				Machine.writeMemory(Main.codeGenAddr++, (short)this.lexicalLevel);
				Machine.writeMemory(Main.codeGenAddr++, (short)0);
				Machine.writeMemory(Main.codeGenAddr++, Machine.SUB);
				Machine.writeMemory(Main.codeGenAddr++, Machine.POPN);
				Machine.writeMemory(Main.codeGenAddr++, Machine.SETD);
				Machine.writeMemory(Main.codeGenAddr++, (short)(this.lexicalLevel - 1));
			}
		}
		catch (Exception e) {
			throw new CodeGenErrorException(e.getMessage());
		}
	}
}
