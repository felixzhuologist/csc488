package compiler488.symbol;

import compiler488.ast.type.Type;
import compiler488.semantics.SemanticErrorException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> PUT YOUR NAMES HERE </B>
 */

public class SymbolTable {

	/** String used by Main to print symbol table
	 *  version information.
	 */

	public final static String version = "Winter 2017" ;

	private HashMap<String, SymbolTableEntry> symbolTable;
	private ArrayList<ArrayList<SymbolTableEntry>> scopeDisplay;

	private Integer depth;

	/** Symbol Table  constructor
	 *  Create and initialize a symbol table
	 */
	public SymbolTable () {
		symbolTable = new HashMap<String, SymbolTableEntry>();
		scopeDisplay = new ArrayList<ArrayList<SymbolTableEntry>>();
		depth = -1;
	}

	/**  Initialize - called once by semantic analysis
	 *                at the start of  compilation
	 *                May be unnecessary if constructor
	 *                does all required initialization
	 */
	public void Initialize() {}

	/**  Finalize - called once by Semantics at the end of compilation
	 *              May be unnecessary
	 */
	public void Finalize() {}


	public void openScope() {
		scopeDisplay.add(new ArrayList<SymbolTableEntry>());
		depth++;
	}

	/**
	 * Delete every symbol in this scope, and replace them in the symbol table
	 * with the respective previously-declared symbols (with the same names) from outer scopes
     */
	public void closeScope() {
		for (SymbolTableEntry symbol : scopeDisplay.get(depth)) {
			// get the previously-declared symbol with this name, if it exists
			SymbolTableEntry prevSym = symbol.getPrev();

			// remove this symbol from the symbol table
			symbolTable.remove(symbol.getName());

			// if there was a previously-declared symbol with the same name from an outer scope,
			// add it to the symbol table
			if (prevSym != null) {
				symbolTable.put(prevSym.getName(), prevSym);
			}
		}

		// delete all symbols stored at this scope, and decrement `depth` value
		scopeDisplay.remove(scopeDisplay.size() - 1);
		depth--;
	}

	/**
	 * Return the SymbolTableEntry with name `name`
     */
	public SymbolTableEntry getEntry(String name) {
		return symbolTable.get(name);
	}

	/**
	 * Add a new symbol entry into the symbol table and scopeDisplay
     */
	public Boolean addEntry(SymbolTableEntry newSymbol) throws SemanticErrorException {
		SymbolTableEntry oldSymbol = getEntry(newSymbol.getName());

		// if the symbol has already been declared at this depth, then we return false
		if (oldSymbol != null && oldSymbol.getDepth() == depth) {
			throw new SemanticErrorException(newSymbol.name + " has already been declared in this scope");
		}

		// set level to be all the symbols that are already declared in this scope
		newSymbol.setLevel(scopeDisplay.get(depth));

		newSymbol.setDepth(depth);

		// add this symbol to the list of symbols declared in this scope
		scopeDisplay.get(depth).add(newSymbol);

		// if this is a new symbol, add it to the map of all symbols.
		// if a symbol for this name already exists (either in this scope or in outer scopes),
		// remove the previous declaration, and add this new declaration
		if (oldSymbol == null) {
			symbolTable.put(newSymbol.getName(), newSymbol);
		} else {
			symbolTable.remove(oldSymbol.getName());
			symbolTable.put(newSymbol.getName(), newSymbol);
		}

		// save previous declaration (so it can be restored when this scope is exited)
		newSymbol.setPrev(oldSymbol);

		return true;
	}
}
