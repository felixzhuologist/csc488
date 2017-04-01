package compiler488.symbol;

import compiler488.ast.type.Type;

import java.util.ArrayList;

public class ProcedureSymbol extends RoutineSymbol {
    public ProcedureSymbol(String name, ArrayList<Type> paramTypes) {
        super(name, paramTypes);
    }
}
