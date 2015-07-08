package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.interfaces.IScope;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.UndefinedSymbolException;

public class Scope implements IScope {

	private Map<String, Symbol> symbolTable = new HashMap<String, Symbol>();
	private String name;
	private IScope parent;

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
		for (String dep : ExpressionParser.listSymbolsInExpression(sym.getValueDefinition())) {
			getDependencies().addNode(dep);
			getDependencies().addEdge(sym.getName(), dep);
		}
	}

	@Override
	public Symbol resolve(String name) throws LEMSCompilerException {
		Symbol symb = this.symbolTable.get(name);
		if (null != symb)
			return symb;
		if (null != getParent()) {
			return getParent().resolve(name);
		}
		throw new LEMSCompilerException("Undefined symbol: " +  name , LEMSCompilerError.UndefinedSymbol);
	}

	@Override
	public Set<String> getDefinedSymbols() {
		return this.symbolTable.keySet();
	}

	private Map<String, Quantity<?>> evalDependencies(Symbol symbol, Map<String, Quantity<?>> localContext) throws LEMSCompilerException,
			UndefinedSymbolException {

		for (String dep : getDependencies().edgesFrom(symbol.getName())) {
			Symbol resolved = resolve(dep);
			if(!localContext.containsKey(dep) && !(dep.equals(symbol.getName())))
				localContext.putAll(evalDependencies(resolved, localContext));
		}
		localContext.put(symbol.getName(), ExpressionParser.evaluateQuantitiesContext(
				symbol.getValueDefinition(), localContext, getUnitContext()));

		return localContext;

	}

	public Quantity<?> evaluate(String symbol) throws LEMSCompilerException {
		return evaluate(symbol, new HashMap<String, Quantity<?>>());
	}

	public Quantity<?> evaluate(String symbol,
			Map<String, Quantity<?>> indepVars) throws LEMSCompilerException {
		try {
			HashMap<String, Quantity<?>> context = new HashMap<String, Quantity<?>>();
			context.putAll(indepVars);
			Map<String, Quantity<?>> evaluated = evalDependencies(resolve(symbol), context);
			return evaluated.get(symbol);
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

}
