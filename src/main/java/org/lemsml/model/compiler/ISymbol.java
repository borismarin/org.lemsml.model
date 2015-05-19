package org.lemsml.model.compiler;


public interface ISymbol<T> extends INamed, IDimensionalEvaluable {

	public T getType();
	public void setType(T type);

}
