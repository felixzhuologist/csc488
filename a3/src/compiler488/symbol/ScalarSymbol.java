package compiler488.symbol;

import compiler488.ast.type.Type;

public class ScalarSymbol extends VariableSymbol {
    public ScalarSymbol(String name) {
        super(name);
    }

    public ScalarSymbol(String name, Type type) {
        super(name, type);
    }
}
