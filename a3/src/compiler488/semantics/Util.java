package compiler488.semantics;

import compiler488.ast.type.Type;
import compiler488.ast.expn.*;

public class Util {
    public static boolean expnEvaluatesToBool(Expn expn) {
      return (expn instanceof BoolExpn || expn instanceof BoolConstExpn ||
              expn instanceof CompareExpn || expn instanceof EqualsExpn ||
              expn instanceof NotExpn || expn instanceof FunctionCallExpn ||
              expn instanceof IdentExpn);
    }
}
