package org.lemsml.model.compiler;



public interface IScope {
	public String getScopeName();
	public IScope getEnclosingScope();
	public ISymbol<?> resolve(String name);
	void define(ISymbol<?> sym);

}
