package org.lemsml.model.compiler.semantic.visitors;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.lemsml.model.ComponentType;
import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.PhysicalQuantity;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.TopologicalSort;

/**
 * @author borismarin
 *
 */
public class ResolveEvaluables extends TraversingVisitor<Void, Throwable> {

	private Lems lems;

	public ResolveEvaluables(Lems lems) throws Throwable {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Void, Throwable>());
		this.lems = lems;
		ScopingResolver scopeRes = new ScopingResolver(this.lems, this.lems);
		this.lems.accept(scopeRes);
		evalInterdependentExprs(this.lems, scopeRes);
		// need to process:
		// - derived variables -> build evaluable expression
		// f({stateVar:value})->Double
		// - time derivatives -> build evaluable expression
		// f({stateVar:value})->Double
	}

	@Override
	public Void visit(Component comp) throws Throwable {

		ComponentType type = lems.getComponentTypeByName(comp.getType());
		ScopingResolver scopeRes = new ScopingResolver(comp, this.lems);
		type.accept(scopeRes);
		evalInterdependentExprs(comp, scopeRes);
		// TODO: handle spurious attributes ie. those that don't correspond to
		// anything in the ComponentType definition
		return null;
	}

	private <T> void evalInterdependentExprs(IScope scope,
			ScopingResolver scopRes) {
		Map<String, String> expressions = scopRes.getExpressions();
		Map<String, Double> context = scopRes.getContext();
		DirectedGraph<String> dependencies = scopRes.getDependencies();
		List<String> sorted = TopologicalSort.sort(dependencies);
		Collections.reverse(sorted);
		for (String depName : sorted) {
			Double val = ExpressionParser.evaluateInContext(
					expressions.get(depName), context);
			ISymbol<?> resolved = scope.resolve(depName);
			NamedDimensionalType depType = (NamedDimensionalType) resolved
					.getType();
			// TODO: WRONG!! need to use Dimensional evaluator from expr_parser!
			PhysicalQuantity quant = new PhysicalQuantity(val,
					depType.getDimension());
			quant.setUnit(lems.getDimensionByName(quant.getUnitSymbol()));
			resolved.setDimensionalValue(quant);
			context.put(depName, val);
		}
	}
}
