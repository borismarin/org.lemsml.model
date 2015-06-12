package org.lemsml.model.compiler.semantic.visitors;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.measure.Unit;

import org.lemsml.model.ComponentType;
import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.PhysicalQuantity;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.TraversingVisitor;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.TopologicalSort;

/**
 * @author borismarin
 *
 */
public class ResolveSymbolicExpressions extends TraversingVisitor<Void, Throwable> {
	// applies to Parameters, Constants, DerivedParameters: independent of state

	private Lems lems;

	public ResolveSymbolicExpressions(Lems lems) throws Throwable {
		super(new DepthFirstTraverserExt<Throwable>(), new BaseVisitor<Void, Throwable>());
		this.lems = lems;
		BuildStatelessDependenciesContexts scopeRes = new BuildStatelessDependenciesContexts(this.lems, this.lems);
		this.lems.accept(scopeRes);
		evalInterdependentExprs(this.lems, scopeRes);
	}


	@Override
	public Void visit(Component comp) throws Throwable {

		ComponentType type = lems.getComponentTypeByName(comp.getType());
		BuildStatelessDependenciesContexts scopeRes = new BuildStatelessDependenciesContexts(comp, this.lems);
		type.accept(scopeRes);
		evalInterdependentExprs(comp, scopeRes);
		// TODO: handle spurious attributes ie. those that don't correspond to
		// anything in the ComponentType definition
		return null;
	}

	private void evalInterdependentExprs(IScope scope, BuildStatelessDependenciesContexts scopRes) {
		Map<String, String> expressions = scopRes.getExpressions();
		Map<String, Double> context = scopRes.getContext();
		Map<String, Unit<?>> unitContext = scopRes.getUnitContext();
		DirectedGraph<String> dependencies = scopRes.getDependencies();
		List<String> sorted = TopologicalSort.sort(dependencies);
		Collections.reverse(sorted);
		for (String depName : sorted) {
			Double val = ExpressionParser.evaluateInContext(expressions.get(depName), context);
			Unit<?> unit = ExpressionParser.dimensionalAnalysis(expressions.get(depName), unitContext);
			ISymbol<?> resolved = scope.resolve(depName);
			PhysicalQuantity quant = new PhysicalQuantity(val,
					((NamedDimensionalType) resolved.getType()).getDimension());
			quant.setUnit(unit);
			resolved.setDimensionalValue(quant);
			context.put(depName, val);
			unitContext.put(depName, quant.getUnit());
		}
	}
}
