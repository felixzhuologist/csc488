package compiler488.ast.expn;

import com.sun.org.apache.bcel.internal.classfile.Code;
import compiler488.ast.Readable;
import compiler488.codegen.CodeGenErrorException;
import compiler488.runtime.Machine;
import compiler488.symbol.*;
import compiler488.compiler.Main;
import compiler488.ast.type.*;
import compiler488.semantics.Util;
import compiler488.semantics.SemanticErrorException;
import compiler488.codegen.CodeGenErrorException;
import compiler488.runtime.Machine;

/**
 * References to an array element variable
 * 
 * Treat array subscript operation as a special form of unary expression.
 * operand must be an integer expression
 */
public class SubsExpn extends UnaryExpn implements Readable {

    private String variable; // name of the array variable
    // used to calculate the memory address of the variable
    private int lexicalLevel;   // lexical level of symbol table entry
    private int index;          // index of symbol table entry
    private int lb;             // lowerbound of array

    public SubsExpn(Integer lineNumber, String variable, Expn operand) {
        super(lineNumber);
        this.variable = variable;
        this.operand = operand;
        this.lexicalLevel = -1;
        this.index = 1;
    }


    /** Returns a string that represents the array subscript. */
    @Override
    public String toString() {
        return (variable + "[" + operand + "]");
    }

    @Override
    public void doSemantics() throws SemanticErrorException {
        operand.doSemantics();
        if (!(operand.getResultType() instanceof IntegerType)) {
            throw new SemanticErrorException("Indexing into array with non integer type");
        }

        SymbolTableEntry entry = Main.symbolTable.getEntry(variable);
        if (entry == null || !(entry instanceof ArraySymbol)) {
          throw new SemanticErrorException("Reference to undeclared array variable " + variable);
        }

        this.lexicalLevel = entry.getDepth();
        this.index = entry.getIndex();

        ArraySymbol arrEntry = (ArraySymbol) entry;
        this.resultType = Util.getTypeWithLineNumber(arrEntry.getType(), lineNumber);
        
        this.lb = arrEntry.getLb();
    }
    
    @Override
    public void doCodeGen() throws CodeGenErrorException {  
     
        try {
            Machine.writeMemory(Main.codeGenAddr++, Machine.ADDR);
            Machine.writeMemory(Main.codeGenAddr++, (short)this.lexicalLevel);
            Machine.writeMemory(Main.codeGenAddr++, (short)this.index);
            
            // This will PUSH the operand's value
            this.operand.doCodeGen();
            
            if (this.lb != 1) {
                Machine.writeMemory(Main.codeGenAddr++, Machine.PUSH);
                Machine.writeMemory(Main.codeGenAddr++, (short)this.lb);
                Machine.writeMemory(Main.codeGenAddr++, Machine.SUB);
            }
            
            Machine.writeMemory(Main.codeGenAddr++, Machine.ADD);
        }
        catch (Exception e) {
            System.out.println("Thrown in SubsExpn");
            throw new CodeGenErrorException(e.getMessage());
        }
    }


    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }


}
