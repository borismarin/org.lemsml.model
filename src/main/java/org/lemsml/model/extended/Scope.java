package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.lemsml.model.exceptions.LEMSCompilerException;

import com.google.common.collect.ImmutableMap;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.UndefinedSymbolException;

public class Scope implements IScope {

	private Map<String, Symbol> symbolTable = new HashMap<String, Symbol>();
	private String name;
	private IScope parent;

	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Quantity<?>> context = new HashMap<String, Quantity<?>>();
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();

	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	public Scope(String string) {
		this.setScopeName(string);
	}

	@Override
	public Symbol define(Symbol sym) throws LEMSCompilerException,
			UndefinedSymbolException {
		getExpressions().put(sym.getName(), sym.getValueDefinition());
		buildDependencies(sym);
		sym.setInScope(this);

		return this.symbolTable.put(sym.getName(), sym);
	}

	private void buildDependencies(Symbol sym) throws LEMSCompilerException,
			UndefinedSymbolException {

		getDependencies().addNode(sym.getName());
		for (String dep : ExpressionParser.listSymbolsInExpression(sym
				.getValueDefinition())) {
			getDependencies().addNode(dep);
			getDependencies().addEdge(sym.getName(), dep);
		}
	}

	@Override
	public Symbol resolve(String name) {
		Symbol symb = this.symbolTable.get(name);
		if (null != symb)
			return symb;
		if (null != getParent()) {
			return getParent().resolve(name);
		}
		return null;
	}

	@Override
	public Set<String> getDefinedSymbols() {
		return this.symbolTable.keySet();
	}

	public void evalDependencies(String symbol) throws UndefinedSymbolException {

		Symbol resolved = resolve(symbol);

		Set<String> edgesFrom = getDependencies().edgesFrom(symbol);
		for (String dep : edgesFrom) { // why aren't we using the toposorted graph?
			if(!dep.equals(symbol)) //ugly guard for state vars
				evalDependencies(dep);
		}
		if (null == context.get(symbol)) { // TODO: DANGER! must clear context on setters
			try {
				Quantity<?> val = ExpressionParser.evaluateQuantitiesContext(resolved.getValueDefinition(), context, getUnitContext());
				context.put(symbol, val);
			} catch (UndefinedSymbolException e) {
				// OK, those are symbolic expressions
				getContext().putAll(context);
			}
		}
		return;

	}

	public Quantity<?> evaluate(String symbol) throws UndefinedSymbolException {
		evalDependencies(symbol);
		return getContext().get(symbol);
	}

	public Quantity<?> evaluate(String symbol, Map<String, Quantity<?>> indepVars)
			throws UndefinedSymbolException {
		evalDependencies(symbol);
		ImmutableMap<String, Quantity<?>> context = new ImmutableMap.Builder<String, Quantity<?>>()
				.putAll(this.getContext()).putAll(indepVars).build();
		return ExpressionParser.evaluateQuantitiesContext(getExpressions().get(symbol), context, getUnitContext());
	}


	public Set<String> getIndependentVariables(String symbol) {
		Set<String> syms = ExpressionParser
				.listSymbolsInExpression(getExpressions().get(symbol));
		Set<String> inContext = new HashSet<String>(getContext().keySet());
		syms.removeAll(inContext);
		return syms;
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

	public Map<String, Quantity<?>> getContext() {
		return context;
	}

	public void setContext(Map<String, Quantity<?>> context) {
		this.context = context;
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
