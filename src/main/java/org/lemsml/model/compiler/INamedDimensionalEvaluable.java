package org.lemsml.model.compiler;


public interface INamedDimensionalEvaluable extends IDimensional, IEvaluable, INamed{
	public IDimensionalEvaluable getDimensionalValue();
	public void setDimensionalValue(IDimensionalEvaluable val);
}
