package compiler488.ast.stmt;


import compiler488.ast.ASTList;
import compiler488.ast.decl.Declaration;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {

    public Program(Integer lineNumber, ASTList<Declaration> declarations, ASTList<Stmt> statements) {
        super(lineNumber);
        this.declarations = declarations;
        this.statements = statements;
    }

}
