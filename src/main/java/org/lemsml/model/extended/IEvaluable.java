package org.lemsml.model.extended;

import expr_parser.utils.UndefinedParameterException;

interface IEvaluable {
	public Double evaluate() throws UndefinedParameterException;

}
