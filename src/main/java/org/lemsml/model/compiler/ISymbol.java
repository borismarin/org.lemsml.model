package org.lemsml.model.compiler;

import org.lemsml.model.extended.INamedDimensionalEvaluable;
import org.lemsml.model.extended.ITyped;
import org.lemsml.model.extended.IValueDefinition;

public interface ISymbol<T> extends INamedDimensionalEvaluable, ITyped<T>, IValueDefinition {
	public Double getValue();
	public void setValue(Double val);
}
