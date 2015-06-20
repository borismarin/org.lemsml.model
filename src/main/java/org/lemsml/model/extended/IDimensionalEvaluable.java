package org.lemsml.model.extended;

import expr_parser.utils.UndefinedSymbolException;


interface IDimensionalEvaluable extends IDimensional, IEvaluable{
	public Double evaluateSI() throws UndefinedSymbolException;
}
