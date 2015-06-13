package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.measure.Unit;

import org.lemsml.model.DerivedVariable;
import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.Parameter;
import org.lemsml.model.TimeDerivative;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.LemsNode;
import org.lemsml.visitors.BaseVisitor;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;

public class BuildSymbolicExpressionDependenciesContexts extends BaseVisitor<Boolean, Throwable> {

	private IScope scope;
	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Double> context = new HashMap<String, Double>();
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();
	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	public BuildSymbolicExpressionDependenciesContexts(IScope scope) throws Throwable {
		this.scope = scope;
		// need to process:
		// - derived variables -> build evaluable expression f({stateVar:value})->Double
		// - time derivatives -> build evaluable expression f({stateVar:value})->Double
	}

	@Override
	public Boolean visit(DerivedVariable derVar) throws LEMSCompilerException {
		Component comp = (Component) this.scope;
		buildDependeciesAndContext(comp, derVar, derVar.getName(), derVar.getValue());
		return null;
	}

	@Override
	public Boolean visit(TimeDerivative dx) throws LEMSCompilerException {
		Component comp = (Component) this.scope;
		buildDependeciesAndContext(comp, dx, BuildScope.generateTimeDerivativeName(dx), dx.getValue());
		return null;
	}


	private void buildDependeciesAndContext(IScope scope, LemsNode typeDef, String defName, String defValue) throws LEMSCompilerException {
		expressions.put(defName, defValue);
		dependencies.addNode(defName);
		for (String dep : ExpressionParser.listSymbolsInExpression(defValue)) {
			ISymbol<?> resolved = scope.resolve(dep);
			// TODO: where/how should errors be handled?
			if (resolved == null) {
				String err = MessageFormat
						.format("Symbol[{0}] undefined in  expression [{1}], at [({2}) {3}]",
								dep, defValue, typeDef.getClass().getSimpleName(), defName);
				throw new LEMSCompilerException(err, LEMSCompilerError.UndefinedSymbol);
			}
			buildContext(context, dep, resolved);
			if (resolved.getType().getClass().equals(typeDef.getClass())) {
				// we need to build a dependency graph in order to evaluate
				// things in the proper order
				dependencies.addNode(dep);
				dependencies.addEdge(defName, dep);
			}
		}
	}

	// builds context from resolved symbols, according to scoping rules
	private void buildContext(Map<String, Double> context, String symbol,
			ISymbol<?> resolved) {
		// der pars can depend only on parameters
		NamedDimensionalType depType = (NamedDimensionalType) resolved.getType();
		if (depType instanceof Parameter) {
			context.put(symbol, resolved.evaluate(null));
			unitContext.put(symbol, resolved.getDimensionalValue().getUnit());
		}
	}

	public Map<String, String> getExpressions() {
		return this.expressions;
	}

	public Map<String, Double> getContext() {
		return this.context;
	}

	public DirectedGraph<String> getDependencies() {
		return this.dependencies;
	}

	public Map<String, Unit<?>> getUnitContext() {
		return this.unitContext;
		
	}

}
