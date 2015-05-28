package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.NamedDimensionalValuedType;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.PhysicalQuantity;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.TraversingVisitor;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;

public class ScopingResolver extends TraversingVisitor<Void, Throwable> {

	private IScope scope;
	private Lems lems;
	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Double> context = new HashMap<String, Double>();
	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	public ScopingResolver(IScope scope, Lems lems) {
		super(new ComponentScopeResolutionTraverser<Throwable>(),
				new BaseVisitor<Void, Throwable>());
		this.scope = scope;
		this.lems = lems;
	}

	@Override
	public Void visit(Constant ctt) throws Throwable {
		addDimValToSymbol(ctt.getName(), ctt.getValue());
		//TODO: this would handle Constants defined via expressions, as per the docs.
		//buildDependeciesAndContext(this.scope, ctt);
		return null;
	}

	@Override
	public Void visit(Parameter parDef) throws LEMSCompilerException {
		Component comp = (Component) this.scope;
		String pName = parDef.getName();
		QName qualiPName = new QName(pName);
		// need to circumvent unschemable component structure... 
		if (comp.getOtherAttributes().keySet().contains(qualiPName)) {
			String def = comp.getOtherAttributes().get(qualiPName);
			addDimValToSymbol(pName, def);
		} else {
			// TODO : decorate ParameterInstance with error instead?
			// how to pass extra info to it then?
			throw new LEMSCompilerException("Components of type "
					+ comp.getType() + " must define parameter " + pName,
					LEMSCompilerError.RequiredParameterUndefined);
		}
		return null;
	}

	private void addDimValToSymbol(String symbolName, String symbolDef)
			throws LEMSCompilerException {
		ISymbol<?> resolved = this.scope.resolve(symbolName);
		PhysicalQuantity pq = new PhysicalQuantity(symbolDef);
		pq.setUnit(this.lems.getUnitBySymbol(pq.getUnitSymbol()));
		resolved.setDimensionalValue(pq);
	}

	@Override
	public Void visit(DerivedParameter typeDef) throws LEMSCompilerException {
		Component comp = (Component) this.scope;
		buildDependeciesAndContext(comp, typeDef);
		return null;
	}

	private void buildDependeciesAndContext(IScope scope,
			NamedDimensionalValuedType typeDef) throws LEMSCompilerException {
		// a derived parameter can depend on parameters and other derpars.
		String defName = typeDef.getName();
		String defValue = typeDef.getValue();
		expressions.put(defName, defValue);
		dependencies.addNode(defName);
		for (String dep : ExpressionParser.listSymbolsInExpression(defValue)) {
			ISymbol<?> resolved = scope.resolve(dep);
			// TODO: where/how should errors be handled?
			if (resolved == null) {
				String err = MessageFormat
						.format("Symbol[{0}] undefined in  expression [{1}], at [({2}) {3}]",
								dep, defValue, typeDef.getClass()
										.getSimpleName(), defName);
				throw new LEMSCompilerException(err,
						LEMSCompilerError.UndefinedSymbol);
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
		NamedDimensionalType depType = (NamedDimensionalType) resolved
				.getType();
		if (depType instanceof Parameter) {
			context.put(symbol, resolved.evaluate());
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

}
