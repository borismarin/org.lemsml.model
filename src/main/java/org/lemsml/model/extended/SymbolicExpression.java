package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lemsml.model.NamedDimensionalValuedType;

import com.google.common.collect.ImmutableMap;

import expr_parser.utils.ExpressionParser;

public class SymbolicExpression<T> extends Symbol<T> {

	private Map<String, Double> context = new HashMap<String, Double>();

	public SymbolicExpression(String name, T instance) {
		super(name, instance);
	}

	public Double evaluate(Map<String, Double> indepVars) {
		ImmutableMap<String, Double> context = 
				new ImmutableMap.Builder<String, Double>()
					.putAll(this.getContext())
					.putAll(indepVars)
					.build();
		return ExpressionParser.evaluateInContext(((NamedDimensionalValuedType) this.getType()).getValue(), context);
	}

	public Map<String, Double> getContext() {
		return context;
	}

	public void setContext(Map<String, Double> context) {
		this.context = context;
	}
	
	public Set<String> getIndependentVariables(){
		Set<String> syms = ExpressionParser.listSymbolsInExpression(
				((NamedDimensionalValuedType) this.getType()).getValue());
		Set<String> inContext = new HashSet<String>(this.context.keySet());
		syms.removeAll(inContext);
		return syms;
	}


}
