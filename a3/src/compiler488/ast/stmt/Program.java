package compiler488.ast.stmt;


import compiler488.ast.ASTList;
import compiler488.ast.decl.Declaration;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {

    private ASTList<Declaration> declarations; // The declarations at the top.

    private ASTList<Stmt> statements; // The statements to execute.

//    public Program() {
//        declarations = new ASTList<Declaration>();
//        statements = new ASTList<Stmt>();
//    }

    public Program(ASTList<Declaration> declarations, ASTList<Stmt> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }


}
