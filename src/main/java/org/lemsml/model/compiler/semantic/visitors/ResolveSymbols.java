package org.lemsml.model.compiler.semantic.visitors;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.measure.Unit;

import org.lemsml.model.ComponentType;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.IScope;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.SymbolicExpression;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.TraversingVisitor;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.TopologicalSort;
import expr_parser.utils.UndefinedSymbolException;

/**
 * @author borismarin
 *
 */
public class ResolveSymbols extends BaseVisitor<Boolean, Throwable> {
	// applies to Parameters, Constants, DerivedParameters: independent of state

	private Lems lems;

	public ResolveSymbols(Lems lems) throws Throwable {
		this.lems = lems;
	}

	@Override
	public Boolean visit(org.lemsml.model.Lems lems) throws Throwable {
		BuildSymbolDependenciesContexts depCtxt = new BuildSymbolDependenciesContexts(
				this.lems, this.lems);
		TraversingVisitor<Boolean, Throwable> scopeRes = new TraversingVisitor<Boolean, Throwable>(
				new SymbolPrecedenceTraverser<Throwable>(), depCtxt);
		lems.accept(scopeRes);

		//TODO: traversing logic belongs in traverser!
		for(Component comp : this.lems.getComponents()){
			comp.accept(this);
		}
		evalInterdependentExprs(this.lems, depCtxt);

		return null;
	}

	@Override
	public Boolean visit(Component comp) throws Throwable {
		ComponentType type = lems.getComponentTypeByName(comp.getType());
		BuildSymbolDependenciesContexts depCtxt = new BuildSymbolDependenciesContexts(
				comp, this.lems);
		TraversingVisitor<Boolean, Throwable> scopeRes = new TraversingVisitor<Boolean, Throwable>(
				new SymbolPrecedenceTraverser<Throwable>(), depCtxt);
		type.accept(scopeRes);
		evalInterdependentExprs(comp, depCtxt);

		//TODO: traversing logic belongs in traverser!
		//      We need to traverse parents first
		for(Component subComp : comp.getComponent()){
			subComp.accept(this);
		}
		return null;
	}

	private void evalInterdependentExprs(IScope scope,
			BuildSymbolDependenciesContexts scopRes)
			throws UndefinedSymbolException {

		Map<String, String> expressions = scopRes.getExpressions();
		Map<String, Double> context = scopRes.getContext();
		Map<String, Unit<?>> unitContext = scopRes.getUnitContext();

		DirectedGraph<String> dependencies = scopRes.getDependencies();
		List<String> sortedDeps = TopologicalSort.sort(dependencies);
		Collections.reverse(sortedDeps);

		for (String depName : sortedDeps) {
			ISymbol<?> resolved = scope.resolve(depName);
			Double val = null;
			Unit<?> unit = null;
			try {
				val = ExpressionParser.evaluateInContext(expressions.get(depName), context);
				// TODO: is this duplicated work? (we already check comp defs for correct dims)
				unit = ExpressionParser.dimensionalAnalysis(expressions.get(depName), unitContext);
				resolved.setValue(val);
				resolved.setUnit(unit);
				context.put(depName, val);
				unitContext.put(depName, unit);
			} catch (UndefinedSymbolException e) {
				// OK, those are symbolic expressions
				((SymbolicExpression<?>) resolved).getContext().putAll(context);
				((SymbolicExpression<?>) resolved).getUnitContext().putAll(unitContext);
			}
		}
	}
}
