package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.lemsml.model.DerivedVariable;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.interfaces.IScope;
import org.lemsml.model.extended.interfaces.IScoped;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.UndefinedSymbolException;

public class Scope implements IScope {

	private Map<String, Symbol> symbolTable = new HashMap<String, Symbol>();
	private String name;
	private IScope parent;
	private Component belongsTo;

	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();

	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	public Scope(String string) {
		this.setScopeName(string);
	}

	@Override
	public Symbol define(Symbol sym) throws LEMSCompilerException {
		getExpressions().put(sym.getName(), sym.getValueDefinition());
		buildDependencies(sym);
		sym.setInScope(this);
		return this.symbolTable.put(sym.getName(), sym);
	}

	private void buildDependencies(Symbol sym) throws LEMSCompilerException {
		getDependencies().addNode(sym.getName());
		if (null != sym.getValueDefinition()) { // disregard select/reduce
			for (String dep : ExpressionParser.listSymbolsInExpression(sym
					.getValueDefinition())) {
				getDependencies().addNode(dep);
				getDependencies().addEdge(sym.getName(), dep);
			}
		}
		else{ // select/reduce
			String path = ((DerivedVariable) sym.getType()).getSelect().replace("/", ".");
			sym.setValueDefinition(path);
			getDependencies().addNode(path);
			getDependencies().addEdge(sym.getName(), path);
		}
	}

	@Override
	public Symbol resolve(String name) throws LEMSCompilerException {
		// Scoping rules: if it is not found, look for it in parent (recurse)
		//                paths allow direct access to any symbol in a scope
		Symbol symb = this.symbolTable.get(name);
		if(null != symb)
			return symb;
		if(name.indexOf('.') > 0){ // path notation breaks neat scoping...
			return resolvePath(name);
		}
		if (null != getParent()) {
			return getParent().resolve(name);
		}
		throw new LEMSCompilerException("Undefined symbol: " + name,
				LEMSCompilerError.UndefinedSymbol);
	}

	private Symbol resolvePath(String path) throws LEMSCompilerException {
		return this.getBelongsTo().followPath(path).getScope().resolve(path.split("\\.")[1]);
	}

	@Override
	public Set<String> getDefinedSymbols() {
		return this.symbolTable.keySet();
	}

	//TODO: this method could use some refactoring...
	private Map<String, Quantity<?>> evalDependencies(Symbol symbol,
										Map<String, Quantity<?>> localContext,
										Map<String, Quantity<?>> indepVars)
                        throws LEMSCompilerException, UndefinedSymbolException {

		localContext.putAll(indepVars);
		for (String dep : getDependencies().edgesFrom(symbol.getName())) {
			// don't calculate deps which are already calculated, nor circular deps (e.g. state vars)
			if (!localContext.containsKey(dep) && !(dep.equals(symbol.getName()))){
				// Need to calculate vars in upper scopes, which can't see this one
				Symbol resolved = resolve(dep);
				if (!resolved.getScope().equals(this)) {
					localContext.put(dep,
							resolved.getScope().evaluate(resolved.getName(), indepVars)); // don't carry local names!!
				} else {
					localContext.putAll(evalDependencies(resolved, localContext, indepVars));
				}
			}
		}
		localContext.put(symbol.getName(),
				ExpressionParser.evaluateQuantityInContext(symbol.getValueDefinition(),
						localContext, getUnitContext()));

		return localContext;

	}

	public Quantity<?> evaluate(String symbolName) throws LEMSCompilerException {
		return evaluate(symbolName, new HashMap<String, Quantity<?>>());
	}

	public Quantity<?> evaluate(String symbolName,
			Map<String, Quantity<?>> indepVars) throws LEMSCompilerException {
		try {
			HashMap<String, Quantity<?>> context = new HashMap<String, Quantity<?>>();
			Map<String, Quantity<?>> evaluated = evalDependencies(
					resolve(symbolName), context, indepVars);
			return evaluated.get(symbolName);
		} catch (UndefinedSymbolException e) {
			throw new LEMSCompilerException(e.getMessage(),
					LEMSCompilerError.MissingSymbolValue);
		}
	}

	@Override
	public String getScopeName() {
		return this.name;
	}

	@Override
	public IScope getEnclosingScope() {
		return this.parent;
	}

	public void setScopeName(String name) {
		this.name = name;
	}

	public IScope getParent() {
		return parent;
	}

	public void setParent(IScope parent) {
		this.parent = parent;
	}

	public Map<String, String> getExpressions() {
		return expressions;
	}

	public Map<String, Unit<?>> getUnitContext() {
		return unitContext;
	}

	public DirectedGraph<String> getDependencies() {
		return dependencies;
	}

	public void setUnitContext(Map<String, Unit<?>> unitContext) {
		this.unitContext = unitContext;
	}

	//TODO: consider moving Scope logic back into Component/Lems
	public Component getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(Component belongsTo) {
		this.belongsTo = belongsTo;
	}

}
