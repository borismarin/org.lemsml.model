package org.lemsml.model.extended;

import expr_parser.utils.UndefinedParameterException;


interface IDimensionalEvaluable extends IDimensional, IEvaluable{
	public Double evaluateSI() throws UndefinedParameterException;
}
