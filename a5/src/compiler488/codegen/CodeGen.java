package compiler488.codegen;

import java.io.*;
import java.util.*;

import compiler488.ast.AST;
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

/**      CodeGenerator.java
 *<pre>
 *  Code Generation Conventions
 *
 *  To simplify the course project, this code generator is
 *  designed to compile directly to pseudo machine memory
 *  which is available as the private array memory[]
 *
 *  It is assumed that the code generator places instructions
 *  in memory in locations
 *
 *      memory[ 0 .. startMSP - 1 ]
 *
 *  The code generator may also place instructions and/or
 *  constants in high memory at locations (though this may
 *  not be necessary)
 *      memory[ startMLP .. Machine.memorySize - 1 ]
 *
 *  During program exection the memory area
 *      memory[ startMSP .. startMLP - 1 ]
 *  is used as a dynamic stack for storing activation records
 *  and temporaries used during expression evaluation.
 *  A hardware exception (stack overflow) occurs if the pointer
 *  for this stack reaches the memory limit register (mlp).
 *
 *  The code generator is responsible for setting the global
 *  variables:
 *      startPC         initial value for program counter
 *      startMSP        initial value for msp
 *      startMLP        initial value for mlp
 * </pre>
 * @author  Evan Klein, Felix Zhu, Atheed Thameem, Felix Mo, Jeffrey Deng
 */

public class CodeGen
{
    /** version string for Main's -V */
    public static final String version = "Winter 2017" ;

    /** initial value for memory stack pointer */
    private short startMSP;
    /** initial value for program counter */
    private short startPC;
    /** initial value for memory limit pointer */
    private short startMLP;

    /** flag for tracing code generation */
    private boolean traceCodeGen = Main.traceCodeGen ;

    /**
     *  Constructor to initialize code generation
     */
    public CodeGen()
    {

    }

    /** Additional initialization for Code Generation (if required) */
    void Initialize()
    {
        return;
    }

    /**
     *  Perform any requred cleanup at the end of code generation.
     *  Called once at the end of code generation.
     *  @throws MemoryAddressException
     */
    void Finalize()
            throws MemoryAddressException     // from Machine.writeMemory
    {
        Machine.setPC( (short) 0 ) ;		/* where code to be executed begins */
        Machine.setMSP((short)  (Main.codeGenAddr + 1) );   	/* where memory stack begins */
        Machine.setMLP((short) ( Machine.memorySize -1 ) );
					/* limit of stack */
        Machine.writeMemory((short)  Main.codeGenAddr , Machine.HALT );

        return;
    }

    //  ADDITIONAL FUNCTIONS TO IMPLEMENT CODE GENERATION GO HERE
    public void doIt(AST programAST) throws CodeGenErrorException {
        Initialize();
        programAST.doCodeGen();
        try {
            Finalize();
        } catch (MemoryAddressException e) {
            throw new CodeGenErrorException(e.getMessage());
        }
    }
}