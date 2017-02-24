package compiler488.symbol;

import compiler488.ast.type.Type;

import java.util.ArrayList;

public class RoutineSymbol extends SymbolTableEntry {
    protected ArrayList<Type> paramTypes;

    public RoutineSymbol(String name, ArrayList<Type> paramTypes) {
        this.name = name;
        this.paramTypes = paramTypes;
    }

    public ArrayList<Type> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(ArrayList<Type> paramTypes) {
        this.paramTypes = paramTypes;
    }
}
