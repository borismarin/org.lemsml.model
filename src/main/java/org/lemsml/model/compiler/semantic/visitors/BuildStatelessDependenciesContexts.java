package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.measure.Unit;
import javax.xml.namespace.QName;

import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
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

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;

public class BuildStatelessDependenciesContexts extends BaseVisitor<Boolean, Throwable> {

	private IScope scope;
	private Lems lems;
	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Double> context = new HashMap<String, Double>();
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();
	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	public BuildStatelessDependenciesContexts(IScope scope, Lems lems) throws Throwable {
		this.scope = scope;
		this.lems = lems;
	}

	@Override
	public Boolean visit(Constant ctt) throws Throwable {
		//TODO: Decide whether consts can be defined via expressions, in which case
		//      we just have to uncomment below
		//buildDependeciesAndContext(this.scope, ctt);
		//       otherwise, we treat them as value + unit strings:	
		addDimValToSymbol(ctt.getName(), ctt.getValue());
		return null;
	}

	@Override
	public Boolean visit(DerivedParameter typeDef) throws LEMSCompilerException {
		Component comp = (Component) this.scope;
		buildDependeciesAndContext(comp, typeDef);
		return null;
	}

	@Override
	public Boolean visit(Parameter parDef) throws LEMSCompilerException {
		Component comp = (Component) this.scope;
		String pName = parDef.getName();
		QName qualiPName = new QName(pName);
		// need to circumvent unschemable component structure... 
		if (comp.getOtherAttributes().keySet().contains(qualiPName)) {
			String def = comp.getOtherAttributes().get(qualiPName);
			addDimValToSymbol(pName, def);
		} else {
			// TODO: this probably should be done somewhere else.
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

	private void buildDependeciesAndContext(IScope scope,
			NamedDimensionalValuedType typeDef) throws LEMSCompilerException {
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
								dep, defValue, typeDef.getClass().getSimpleName(), defName);
				throw new LEMSCompilerException(err, LEMSCompilerError.UndefinedSymbol);
			}
			addToContext(dep, resolved);
			if (resolved.getType().getClass().equals(typeDef.getClass())) {
				// we need to build a dependency graph in order to evaluate
				// things in the proper order
				dependencies.addNode(dep);
				dependencies.addEdge(defName, dep);
			}
		}
	}

	// builds context from resolved symbols
	private void addToContext(String symbol, ISymbol<?> resolved) {
		Double resolvedVal = resolved.evaluate(null);
		if (null != resolvedVal) {
			context.put(symbol, resolvedVal);
			unitContext.put(symbol, resolved.getDimensionalValue().getUnit());
		}else{
			// TODO: actually, this has nothing to do with scoping, my bad. 
			//       it has to do with symbol resolution order.
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
