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
public class ResolveSymbolicExpressions extends BaseVisitor<Boolean, Throwable> {
	// applies to Parameters, Constants, DerivedParameters: independent of state

	private Lems lems;

	public ResolveSymbolicExpressions(Lems lems) throws Throwable {
		this.lems = lems;
	}
	
	@Override
	public Boolean visit(org.lemsml.model.Lems lems) throws Throwable {
		BuildSymbolicExpressionDependenciesContexts depCtxt = new BuildSymbolicExpressionDependenciesContexts(
				this.lems);
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
		BuildSymbolicExpressionDependenciesContexts depCtxt = new BuildSymbolicExpressionDependenciesContexts(comp);
		TraversingVisitor<Boolean, Throwable> scopeRes = new TraversingVisitor<Boolean, Throwable>(
				new StatelessVariablesTraverser<Throwable>(), depCtxt);
		type.accept(scopeRes);
		evalInterdependentExprs(comp, depCtxt);
		// TODO: handle spurious attributes ie. those that don't correspond to
		// anything in the ComponentType definition
		return null;
	}

	private void evalInterdependentExprs(IScope scope, BuildSymbolicExpressionDependenciesContexts depCtxt) {
		Map<String, String> expressions = depCtxt.getExpressions();
		Map<String, Double> context = depCtxt.getContext();
		Map<String, Unit<?>> unitContext = depCtxt.getUnitContext();
		DirectedGraph<String> dependencies = depCtxt.getDependencies();
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
