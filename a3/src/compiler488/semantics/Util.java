package compiler488.semantics;

import compiler488.ast.type.*;
import compiler488.ast.expn.*;

public class Util {
    public static boolean expnEvaluatesToBool(Expn expn) {
      return (expn instanceof BoolExpn || expn instanceof BoolConstExpn ||
              expn instanceof CompareExpn || expn instanceof EqualsExpn ||
              expn instanceof NotExpn || expn instanceof FunctionCallExpn ||
              expn instanceof IdentExpn);
    }

    /* 
     * Return a Type object of the same type as input type, but with an
     * updated lineNumber
    */
    public static Type getTypeWithLineNumber(Type type, Integer lineNumber) {
      if (type instanceof BooleanType) {
        return new BooleanType(lineNumber);
      } else {
        return new IntegerType(lineNumber);
      }
    }
}
