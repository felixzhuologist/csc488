package compiler488.symbol;

import compiler488.ast.type.Type;

import java.util.ArrayList;

public class FunctionSymbol extends RoutineSymbol {
    private Type returnType;

    public FunctionSymbol(String name, ArrayList<Type> paramTypes, Type returnType) {
        super(name, paramTypes);
        this.returnType = returnType;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
}
