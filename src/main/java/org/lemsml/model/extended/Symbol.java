package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.Set;

import org.lemsml.model.extended.interfaces.INamedValueDefinition;

import expr_parser.utils.ExpressionParser;
import expr_parser.visitors.AntlrExpressionParser;

public class Symbol {

	private String name;
	private LemsNode type;
	private String valueDefinition;
	private Scope scope;
	private AntlrExpressionParser parser; //TODO: insane coupling

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
				//.listSymbolsInExpression(getValueDefinition());
				.listSymbolsInExpression(getParser());
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

	@Override
	public String toString() {
		return MessageFormat.format("({0}) {1}: {2}",
								this.getType().getClass().getSimpleName(),
								this.getName(),
								this.getValueDefinition());
	}

	public AntlrExpressionParser getParser() {
		return parser;
	}

	public void setParser(AntlrExpressionParser parser) {
		this.parser = parser;
	}

}
