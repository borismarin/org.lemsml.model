package org.lemsml.model.extended;

import java.util.Set;

import javax.measure.Quantity;

import org.lemsml.model.exceptions.LEMSCompilerException;

import expr_parser.utils.UndefinedSymbolException;

public interface IScope {
	public String getScopeName();

	public IScope getEnclosingScope();

	public Symbol resolve(String name);

	Symbol define(Symbol sym) throws LEMSCompilerException, UndefinedSymbolException;

	public Set<String> getDefinedSymbols();

	Quantity<?> evaluate(String symbol) throws UndefinedSymbolException;

}
