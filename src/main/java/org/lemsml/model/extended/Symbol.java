package org.lemsml.model.extended;

import java.util.Set;

import org.lemsml.model.extended.interfaces.INamedValueDefinition;

import expr_parser.utils.ExpressionParser;

public class Symbol {

	private String name;
	private LemsNode type;
	private String valueDefinition;
	private Scope scope;

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

	public void setType(LemsNode instance) {
		this.type = instance;
	}

	public Scope getScope() {
		return scope;
	}

	public void setInScope(Scope inScope) {
		this.scope = inScope;
	}

}
