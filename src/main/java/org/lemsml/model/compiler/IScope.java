package org.lemsml.model.compiler;

import java.util.Set;

import org.lemsml.visitors.Visitable;



public interface IScope extends Visitable {
	public String getScopeName();
	public IScope getEnclosingScope();
	public ISymbol<?> resolve(String name);
	ISymbol<?> define(ISymbol<?> sym);
	public Set<String> getDefinedSymbols();

}
