package compiler488.symbol;

import compiler488.ast.type.Type;

public class VariableSymbol extends SymbolTableEntry {
    protected Type type;

    public VariableSymbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
