package org.lemsml.model.extended;

import expr_parser.utils.UndefinedParameterException;


public interface IDimensionalEvaluable extends IDimensional, IEvaluable{
	public Double evaluateSI() throws UndefinedParameterException;
}
