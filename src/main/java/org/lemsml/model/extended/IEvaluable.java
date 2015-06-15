package org.lemsml.model.extended;

import expr_parser.utils.UndefinedParameterException;

public interface IEvaluable {
	public Double evaluate() throws UndefinedParameterException;

}
