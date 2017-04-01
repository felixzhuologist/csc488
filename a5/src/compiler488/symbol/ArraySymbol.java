package compiler488.symbol;


import compiler488.ast.type.Type;

public class ArraySymbol extends VariableSymbol {
    private Integer lb;     // lower bound
    private Integer ub;     // upper bound
    private Integer size;
    private Type type;

    public ArraySymbol(String name, Integer lb, Integer ub, Integer size) {
        super(name);
        this.lb = lb;
        this.ub = ub;
        this.size = size;
    }

    public ArraySymbol(String name, Type type, Integer lb, Integer ub, Integer size) {
        super(name, type);
        this.lb = lb;
        this.ub = ub;
        this.size = size;
        this.type = type;
    }

    public Integer getLb() {
        return lb;
    }

    public void setLb(Integer lb) {
        this.lb = lb;
    }

    public Integer getUb() {
        return ub;
    }

    public void setUb(Integer ub) {
        this.ub = ub;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
