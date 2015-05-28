package org.lemsml.model.compiler;

public interface ISymbol<T> extends INamedDimensionalEvaluable {

	public T getType();

	public void setType(T type);

}
