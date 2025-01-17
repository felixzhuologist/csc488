package compiler488.symbol;

import java.util.ArrayList;


public abstract class SymbolTableEntry {
    protected String name;
    protected SymbolTableEntry prev = null;     // if exists, previously-declared symbol of same name in an outer scope
    protected Integer depth;
    protected ArrayList<SymbolTableEntry> level;    // list of all other symbols declared in this scope

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolTableEntry getPrev() {
        return prev;
    }

    public void setPrev(SymbolTableEntry prev) {
        this.prev = prev;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public ArrayList<SymbolTableEntry> getLevel() {
        return level;
    }

    public void setLevel(ArrayList<SymbolTableEntry> level) {
        this.level = level;
    }
}
