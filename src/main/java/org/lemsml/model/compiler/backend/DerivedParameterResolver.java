package org.lemsml.model.compiler.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemsml.model.DerivedParameter;
import org.lemsml.model.extended.Component;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import expr_parser.parser.AntlrExpressionParser;
import expr_parser.parser.ListVariablesInExprVisitor;

/**
 * @author borismarin
 *
 */
public class DerivedParameterResolver extends TraversingVisitor<Boolean, Throwable> {

	private Component component;

	/**
	 * @param component
	 */
	public DerivedParameterResolver(Component component) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.component = component;
	}

	@Override
	public Boolean visit(DerivedParameter derPar) throws Throwable {
		Map<String, Double> context = new HashMap<String, Double>();
		
		AntlrExpressionParser p = new AntlrExpressionParser(derPar.getValue());
		//TODO: don't forget dimensional analysis!
		ListVariablesInExprVisitor listVars = new ListVariablesInExprVisitor();
		p.parseAndVisitWith(listVars);
		List<String> dependencies =  listVars.getVariablesInExpr();
		
		for(String var : dependencies){
			context.put(var, component.getParameterByName(var).getValue());
		}
		
		return null;
		
	}
	

}
