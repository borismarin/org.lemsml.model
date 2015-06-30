package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.measure.Unit;

import org.lemsml.model.exceptions.LEMSCompilerException;

import tec.units.ri.quantity.NumberQuantity;

import com.google.common.collect.ImmutableMap;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.UndefinedSymbolException;

public class Scope implements IScope {

	private Map<String, Symbol> symbolTable = new HashMap<String, Symbol>();
	private String name;
	private IScope parent;

	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Double> context = new HashMap<String, Double>();
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();

	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	Scope() {
	}

	Scope(String name) {
		this.name = name;
	}

	@Override
	public String getScopeName() {
		return this.name;
	}

	@Override
	public IScope getEnclosingScope() {
		return this.parent;
	}

	@Override
	public Symbol define(Symbol sym) throws LEMSCompilerException,
			UndefinedSymbolException {
		getExpressions().put(sym.getName(), sym.getValueDefinition());
		buildDependencies(sym);

		return this.symbolTable.put(sym.getName(), sym);

	}

	private void buildDependencies(Symbol sym) throws LEMSCompilerException,
			UndefinedSymbolException {

		getDependencies().addNode(sym.getName());
		for (String dep : ExpressionParser.listSymbolsInExpression(sym
				.getValueDefinition())) {
			// ISymbol<?> resolved = this.resolve(defName);
			// TODO: where/how should errors be handled?
			// if (resolved == null) {
			// String err = MessageFormat
			// .format("Symbol[{0}] undefined in  expression [{1}], at [({2}) {3}]",
			// dep,
			// defValue,
			// sym.getType().getClass().getSimpleName(),
			// defName);
			// throw new LEMSCompilerException(err,
			// LEMSCompilerError.UndefinedSymbol);
			// }
			getDependencies().addNode(dep);
			getDependencies().addEdge(sym.getName(), dep);
			// getExpressions().put(dep, resolved.getValueDefinition());
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

	public IScope getParent() {
		return parent;
	}

	public void setParent(IScope parent) {
		this.parent = parent;
	}

	public Map<String, String> getExpressions() {
		return expressions;
	}

	public void setExpressions(Map<String, String> expressions) {
		this.expressions = expressions;
	}

	public Map<String, Double> getContext() {
		return context;
	}

	public void setContext(Map<String, Double> context) {
		this.context = context;
	}

	public Map<String, Unit<?>> getUnitContext() {
		return unitContext;
	}

	public void setUnitContext(Map<String, Unit<?>> unitContext) {
		this.unitContext = unitContext;
	}

	public DirectedGraph<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(DirectedGraph<String> dependencies) {
		this.dependencies = dependencies;
	}

	public void evalDependencies(String symbol) throws UndefinedSymbolException {

		Set<String> edgesFrom = getDependencies().edgesFrom(symbol);
		for (String dep : edgesFrom) {
			if(!dep.equals(symbol)) //ugly guard for state vars
				evalDependencies(dep);
		}
		if (null == context.get(symbol)) {
			try {
				Symbol resolved = resolve(symbol);
				Double val = ExpressionParser.evaluateInContext(
						resolved.getValueDefinition(), context);
				Unit<?> unit = ExpressionParser.dimensionalAnalysis(
						resolved.getValueDefinition(), unitContext);
				context.put(symbol, val);
				unitContext.put(symbol, unit);
			} catch (UndefinedSymbolException e) {
				// OK, those are symbolic expressions
				getContext().putAll(context);
				getUnitContext().putAll(unitContext);
			}
		}
		return;

	}

	public Double evaluate(String symbol) throws UndefinedSymbolException {
		evalDependencies(symbol);
		return getContext().get(symbol);
	}

	public Double evaluate(String symbol, Map<String, Double> indepVars)
			throws UndefinedSymbolException {
		evalDependencies(symbol);
		ImmutableMap<String, Double> context = new ImmutableMap.Builder<String, Double>()
				.putAll(this.getContext()).putAll(indepVars).build();
		return ExpressionParser.evaluateInContext(getExpressions().get(symbol),
				context);
	}

	public Unit<?> evaluateUnit(String symbol) throws UndefinedSymbolException {
		evalDependencies(symbol);
		return ExpressionParser.dimensionalAnalysis(getExpressions()
				.get(symbol), this.getUnitContext());
	}

	public void setScopeName(String name) {
		this.name = name;
	}

	public Set<String> getIndependentVariables(String symbol) {
		Set<String> syms = ExpressionParser
				.listSymbolsInExpression(getExpressions().get(symbol));
		Set<String> inContext = new HashSet<String>(getContext().keySet());
		syms.removeAll(inContext);
		return syms;
	}

	@Override
	public Double evaluateSI(String symbol) throws UndefinedSymbolException {
		Double val = evaluate(symbol);
		Unit<?> unit = evaluateUnit(symbol);
		return new Double(NumberQuantity.of(val, unit).toSI().getValue()
				.doubleValue());
	}
}
