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
public class ResolveStatelessVariables extends BaseVisitor<Boolean, Throwable> {
	// applies to Parameters, Constants, DerivedParameters: independent of state

	private Lems lems;

	public ResolveStatelessVariables(Lems lems) throws Throwable {
		this.lems = lems;
	}

	@Override
	public Boolean visit(org.lemsml.model.Lems lems) throws Throwable {
		BuildStatelessDependenciesContexts depCtxt = new BuildStatelessDependenciesContexts(
				this.lems, this.lems);
		TraversingVisitor<Boolean, Throwable> scopeRes = new TraversingVisitor<Boolean, Throwable>(
				new StatelessVariablesTraverser<Throwable>(), depCtxt);
		lems.accept(scopeRes);
		for(Component comp : this.lems.getComponents()){
			comp.accept(this);
		}
		evalInterdependentExprs(this.lems, depCtxt);

		return null;
	}

	@Override
	public Boolean visit(Component comp) throws Throwable {

		ComponentType type = lems.getComponentTypeByName(comp.getType());
		BuildStatelessDependenciesContexts depCtxt = new BuildStatelessDependenciesContexts(
				comp, this.lems);
		TraversingVisitor<Boolean, Throwable> scopeRes = new TraversingVisitor<Boolean, Throwable>(
				new StatelessVariablesTraverser<Throwable>(), depCtxt);
		type.accept(scopeRes);
		evalInterdependentExprs(comp, depCtxt);
		// TODO: handle spurious attributes ie. those that don't correspond to
		// anything in the ComponentType definition
		return null;
	}

	private void evalInterdependentExprs(IScope scope,
			BuildStatelessDependenciesContexts scopRes) {
		Map<String, String> expressions = scopRes.getExpressions();
		Map<String, Double> context = scopRes.getContext();
		Map<String, Unit<?>> unitContext = scopRes.getUnitContext();
		DirectedGraph<String> dependencies = scopRes.getDependencies();
		List<String> sorted = TopologicalSort.sort(dependencies);
		Collections.reverse(sorted);
		for (String depName : sorted) {
			Double val = ExpressionParser.evaluateInContext(
					expressions.get(depName), context);
			// TODO: is this duplicated work? (we already check comp defs for
			// correct dims)
			Unit<?> unit = ExpressionParser.dimensionalAnalysis(
					expressions.get(depName), unitContext);
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
