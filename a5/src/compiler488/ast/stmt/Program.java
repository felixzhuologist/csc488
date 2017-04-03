package compiler488.ast.stmt;


import compiler488.ast.ASTList;
import compiler488.ast.decl.Declaration;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.codegen.CodeGenErrorException;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {

    public Program(Integer lineNumber, ASTList<Declaration> declarations, ASTList<Stmt> statements) {
        super(lineNumber);
        this.declarations = declarations;
        this.statements = statements;
    }
    
    @Override
    public void doCodeGen() throws CodeGenErrorException {
        try {
            Machine.writeMemory(Main.codeGenAddr++, Machine.PUSHMT);
            Machine.writeMemory(Main.codeGenAddr++, Machine.SETD);
            Machine.writeMemory(Main.codeGenAddr++, (short)0);
        }
        catch (Exception e) {
            throw new CodeGenErrorException(e.getMessage());
        }
        super.doCodeGen();
    }
}
