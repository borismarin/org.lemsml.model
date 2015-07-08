package org.lemsml.model.extended.interfaces;

import java.util.Set;

import javax.measure.Quantity;

import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Symbol;

public interface IScope {
	public String getScopeName();

	public IScope getEnclosingScope();

	public Symbol resolve(String name) throws LEMSCompilerException;

	Symbol define(Symbol sym) throws LEMSCompilerException;

	public Set<String> getDefinedSymbols();

	Quantity<?> evaluate(String symbol) throws LEMSCompilerException;

}
