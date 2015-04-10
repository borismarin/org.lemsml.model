package org.lemsml.model.compiler.backend;

import java.util.HashMap;
import java.util.Map;

import org.lemsml.model.ComponentType;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.Parameter;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.ParameterInstance;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import expr_parser.utils.ExpressionParser;

/**
 * @author borismarin
 *
 */
public class BuildEvaluationContext extends
		TraversingVisitor<Boolean, Throwable> {

	private Lems lems;
	Map<String, Double> context = new HashMap<String, Double>();

	/**
	 * @param lems
	 */
	public BuildEvaluationContext(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}
	
	@Override
	public Boolean visit(Component comp) throws Throwable {
		
		ComponentType compType = comp.getComponentType();
		// Evaluates derived parameter expressions
		// TODO: should we do dim analysis here?
		for(Parameter parDef : compType.getParameters()){
			String parName = parDef.getName();
			ParameterInstance parInst = comp.getParameterByName(parName);
			context.put(parName, parInst.getDimensionalValue().getValueInSI());
		}
		for (DerivedParameter derPar : compType.getDerivedParameters()) {
			System.out.println(ExpressionParser.evaluateInContext(derPar.getValue(), context));
		}

		return true;
	}

}
