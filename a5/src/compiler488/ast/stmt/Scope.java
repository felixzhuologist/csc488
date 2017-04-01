package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.decl.Declaration;
import compiler488.codegen.CodeGenErrorException;
import compiler488.compiler.Main;
import compiler488.semantics.SemanticErrorException;

/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt {
	protected ASTList<Declaration> declarations; // The declarations at the top.

	protected ASTList<Stmt> statements; // The statements to execute.

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
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
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
	
	@Override
    public void doSemantics() throws SemanticErrorException {
        Main.symbolTable.openScope();
        this.declarations.doSemantics();
        this.statements.doSemantics();
        Main.symbolTable.closeScope();
    }

	@Override
	public void doCodeGen() throws CodeGenErrorException {
		declarations.doCodeGen();
		statements.doCodeGen();
	}
}
