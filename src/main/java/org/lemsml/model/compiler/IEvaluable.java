package org.lemsml.model.compiler;

import java.util.Map;

public interface IEvaluable {
	public Double evaluate(Map<String, Double> context);

}
