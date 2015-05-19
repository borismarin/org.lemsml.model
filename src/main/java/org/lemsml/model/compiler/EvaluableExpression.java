package org.lemsml.model.compiler;

import java.util.Map;
import java.util.Map.Entry;

import javax.measure.Unit;

import expr_parser.utils.ExpressionParser;

public class EvaluableExpression implements IDimensionalEvaluable {

	private String expression;
	private Map<String, Double> context;
	private Unit<?> unit;

	public EvaluableExpression(String expression, Unit<?> unit) {
		this.expression = expression;
		this.unit = unit;
	}

	public void setContext(Map<String, ISymbol<?>> context) {
		for (Entry<String, ISymbol<?>> entry : context.entrySet()) {
			String name = entry.getKey();
			ISymbol<?> symb = entry.getValue();
			// TODO: check units
			this.context.put(name, symb.getValue());
		}
	}

	@Override
	public Double getValue() {

		return ExpressionParser
				.evaluateInContext(this.expression, this.context);
	}

	@Override
	public Unit<?> getUnit() {
		return this.unit;
	}


}
