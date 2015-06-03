package org.lemsml.model.extended;

import java.util.Map;

import org.lemsml.model.NamedDimensionalValuedType;

import expr_parser.utils.ExpressionParser;

public class SymbolicExpression<T> extends Symbol<T> {

	private Map<String, Double> context;

	public SymbolicExpression(String name, T instance) {
		super(name, instance);
	}

	public Double evaluate() {
		return ExpressionParser.evaluateInContext(
				((NamedDimensionalValuedType) this.getType()).getValue(),
				this.context);
	}

	public Map<String, Double> getContext() {
		return context;
	}

	public void setContext(Map<String, Double> context) {
		this.context = context;
	}


}
