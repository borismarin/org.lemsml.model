package org.lemsml.model.compiler;

import java.util.Set;



public interface IScope {
	public String getScopeName();
	public IScope getEnclosingScope();
	public ISymbol<?> resolve(String name);
	ISymbol<?> define(ISymbol<?> sym);
	public Set<String> getDefinedSymbols();

}
