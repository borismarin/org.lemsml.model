package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.measure.Unit;

import org.apache.commons.lang3.tuple.Pair;
import org.lemsml.model.exceptions.LEMSCompilerError;
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
	public Symbol define(Symbol sym) throws LEMSCompilerException,
			UndefinedSymbolException {
		getExpressions().put(sym.getName(), sym.getValueDefinition());
		buildDependencies(sym);
		sym.setInScope(this);

		return this.symbolTable.put(sym.getName(), sym);
	}

	public void defineLiteral(NamedDimensionalType type, Pair<Double, Unit<?>> valueUnit) throws LEMSCompilerException {

		String name = type.getName();
		Unit<?> defDim = type.getUOMDimension();

		Double val = valueUnit.getLeft();
		Unit<?> unit = valueUnit.getRight();

		//TODO: ugly wildcard
		if(!valueUnit.getRight().isCompatible(defDim) && !defDim.getSymbol().equals("*")){
			String err = MessageFormat.format(
					"Unit mismatch for [({0}) {1}] defined in [{2}]:"
							+ " Expecting  [{3}], but"
							+ " dimension of [{4}] is [{5}].",
							type.getClass().getSimpleName(),
							name,
							this.getScopeName(),
							defDim.getDimension().toString(),
							unit,
							unit.getDimension());
			throw new LEMSCompilerException(err, LEMSCompilerError.DimensionalAnalysis);
		}
		else{
			this.getContext().put(name, val);
			this.getUnitContext().put(name, unit);
		}
	}

	private void buildDependencies(Symbol sym) throws LEMSCompilerException,
			UndefinedSymbolException {

		if (sym.getType() instanceof ILiteralDefinition) //no need to parse consts, pars, state vars
			return;

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
		LemsNode type = resolved.getType();

		if (type instanceof ILiteralDefinition) {
			Scope symbScope = resolved.getScope();
			if(!symbScope.equals(this)){ //but need to copy their value from parent scopes
				context.put(symbol, symbScope.getContext().get(symbol));
				unitContext.put(symbol, symbScope.getUnitContext().get(symbol));
			}
			return;
		}

		Set<String> edgesFrom = getDependencies().edgesFrom(symbol);
		for (String dep : edgesFrom) { // why aren't we using the toposorted
									   // graph?
			evalDependencies(dep);
		}
		if (null == context.get(symbol)) { // TODO: DANGER! must clear context
											// on setters
			try {
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
		// ImmutableMap<String, Unit<?>> units = new
		// ImmutableMap.Builder<String, Unit<?>>()
		// .putAll(this.getUnitContext()).putAll(unitRegistry).build();
		return ExpressionParser.dimensionalAnalysis(getExpressions()
				.get(symbol), getUnitContext());
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

	public Map<String, Double> getContext() {
		return context;
	}

	public void setContext(Map<String, Double> context) {
		this.context = context;
	}

	public Map<String, Unit<?>> getUnitContext() {
		return unitContext;
	}

	public DirectedGraph<String> getDependencies() {
		return dependencies;
	}

}
