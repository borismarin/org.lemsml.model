package org.lemsml.model.extended;

import expr_parser.utils.UndefinedSymbolException;

interface IEvaluable {
	public Double evaluate() throws UndefinedSymbolException;

}
