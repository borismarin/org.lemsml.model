package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.lemsml.model.ComponentType;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
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

	public ResolveEvaluables(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Void, Throwable>());
		this.lems = lems;
		// TODO: recursively call this visitor over component types
		// need to process:
		// - constants (lems)
		// - derived variables -> build evaluable expression
		// f({stateVar:value})->Double
		// - time derivatives -> build evaluable expression
		// f({stateVar:value})->Double
	}

	@Override
	public Void visit(Component comp) throws Throwable {

		ComponentType type = lems.getComponentTypeByName(comp.getType());
		// TODO: can't we type.accept(this) here?
		resolveParameters(comp, type);
		resolveDerivedParameters(comp, type);
		// TODO: handle spurious attributes ie. those that don't correspond to
		// anything in the ComponentType definition
		return null;
	}

	private void resolveParameters(Component comp, ComponentType type)
			throws LEMSCompilerException {
		// Read parameter (defined in the ComponentType) values from the
		// attributes
		for (Parameter parDef : type.getParameters()) {
			String pName = parDef.getName();
			QName qualiPName = new QName(pName);
			if (comp.getOtherAttributes().keySet().contains(qualiPName)) {
				String def = comp.getOtherAttributes().get(qualiPName);
				ISymbol<?> resolved = comp.resolve(pName);
				PhysicalQuantity pq = new PhysicalQuantity(def);
				pq.setUnit(this.lems.getUnitBySymbol(pq.getUnitSymbol()));
				resolved.setDimensionalValue(pq);
			} else {
				// TODO : decorate ParameterInstance with error instead?
				//        how to pass extra info to it then?
				throw new LEMSCompilerException("Components of type "
						+ comp.getType() + " must define parameter " + pName,
						LEMSCompilerError.RequiredParameterUndefined);
			}
		}
	}

	private void resolveDerivedParameters(Component comp, ComponentType type)
			throws LEMSCompilerException {
		Map<String, String> expressions = new HashMap<String, String>();
		Map<String, Double> context = new HashMap<String, Double>();

		DirectedGraph<String> dependencies = new DirectedGraph<String>();
		for (DerivedParameter derParDef : type.getDerivedParameters()) {
			// a derived parameter can depend on parameters and other derpars.
			String pName = derParDef.getName();
			expressions.put(pName, derParDef.getValue());
			// we only add parameters here, as other derpars are not yet resolved
			dependencies.addNode(pName);
			for (String dep : ExpressionParser
					.listSymbolsInExpression(derParDef.getValue())) {
				ISymbol<?> resolved = comp.resolve(dep);
				if (resolved == null) {
					String err = MessageFormat
							.format("Symbol[{0}] undefined in  expression [{1}], at [({2}) {3}]",
									dep, 
									derParDef.getValue(),
									derParDef.getClass().getSimpleName(),
									derParDef.getName());
					throw new LEMSCompilerException(err,
							LEMSCompilerError.UndefinedSymbol);
				}
				NamedDimensionalType depType = (NamedDimensionalType) resolved.getType();
				// ugly handling of scoping rules!
				if (depType instanceof Parameter) {
					// der pars can depend only on parameters (which are already
					// resolved) or other derpars
					context.put(dep, resolved.evaluate());
				}
				if (depType instanceof DerivedParameter) {
					// we need to build a dependency graph in order to evaluate
					// things in the proper order
					dependencies.addNode(dep);
					dependencies.addEdge(pName, dep);
				}
			}
		}
		// need to evaluate according to dependencies
		evalInterdependentExprs(comp, expressions, context, dependencies);
	}

	private <T> void evalInterdependentExprs(IScope scope,
			Map<String, String> expressions, Map<String, Double> context,
			DirectedGraph<String> dependencies) {
		List<String> sorted = TopologicalSort.sort(dependencies);
		Collections.reverse(sorted);
		for (String depName : sorted) {
			Double val = ExpressionParser.evaluateInContext(
					expressions.get(depName), context);
			ISymbol<?> resolved = scope.resolve(depName);
			NamedDimensionalType depType =  (NamedDimensionalType) resolved.getType();
			// TODO: WRONG!! need to use Dimensional evaluator from expr_parser!
			PhysicalQuantity quant = new PhysicalQuantity(val, depType.getDimension());
			quant.setUnit(lems.getDimensionByName(quant.getUnitSymbol()));
			resolved.setDimensionalValue(quant);
			context.put(depName, val);
		}
	}
}
