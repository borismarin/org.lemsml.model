package org.lemsml.model.extended;

import java.util.Set;

import javax.measure.Unit;

import org.lemsml.model.NamedDimensionalType;

import expr_parser.utils.ExpressionParser;

public class Symbol {

	private String name;
	private LemsNode type;
	private Unit<?> unit;
	private String valueDefinition;

	public Symbol(NamedDimensionalType type, String valueDef) {
		setName(type.getName());
		setType(type);
		setValueDefinition(valueDef);
	}

	public <T extends INamedValueDefinition> Symbol(T instance) {
		setName(instance.getName());
		setType((LemsNode) instance);
		setValueDefinition(instance.getValueDefinition());
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Unit<?> getUnit() {
		return this.unit;
	}

	public void setUnit(Unit<?> unit) {
		this.unit = unit;
	}

	public String getValueDefinition() {
		return this.valueDefinition;
	}

	public void setValueDefinition(String def) {
		this.valueDefinition = def;
	}

	public Set<String> getIndependentVariables() {
		Set<String> syms = ExpressionParser
				.listSymbolsInExpression(getValueDefinition());
		return syms;
	}

	public LemsNode getType() {
		return type;
	}

	public void setType(LemsNode type) {
		this.type = type;
	}

}
