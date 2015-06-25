package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.Unit;
import javax.xml.namespace.QName;

import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.INamed;
import org.lemsml.model.extended.IScope;
import org.lemsml.model.extended.IValueDefinition;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.LemsNode;
import org.lemsml.model.extended.Symbol;
import org.lemsml.model.extended.SymbolicExpression;
import org.lemsml.model.extended.TimeDerivative;
import org.lemsml.visitors.BaseVisitor;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.TopologicalSort;
import expr_parser.utils.UndefinedSymbolException;

public class SymbolVisitor extends
		BaseVisitor<Boolean, Throwable> {

	private IScope scope;
	private Lems lems;
	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Double> context = new HashMap<String, Double>();
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();
	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	public SymbolVisitor(Lems lems) {
		this.lems = lems;
	}

	@Override
	public Boolean visit(org.lemsml.model.Lems lems)
			throws UndefinedSymbolException {
		this.scope = this.lems;
		evalInterdependentExprs();
		clearMaps();
		return null;
	}

	@Override
	public Boolean visit(Component comp) throws UndefinedSymbolException {
		evalInterdependentExprs(); // this applies to the previous scope
									// (we are traversingFirst)
									// hence, we need to call it in
									// ResolveSymvols.apply() to get the last
									// scope right
		clearMaps();
		this.scope = comp;
		return null;
	}

	@Override
	public Boolean visit(Constant ctt) throws Throwable {
		// TODO: Decide whether consts can be defined via expressions, in which
		// case we just have to uncomment below
		// buildDependeciesAndContext(ctt);
		// otherwise, we treat them as value + unit strings:
		addDimValToSymbol(ctt.getName(), ctt.getValueDefinition());
		return null;
	}

	@Override
	public Boolean visit(Parameter parDef) throws LEMSCompilerException,
			UndefinedSymbolException {
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

	@Override
	public Boolean visit(DerivedParameter derPar) throws LEMSCompilerException,
			UndefinedSymbolException {
		buildDependeciesAndContext(derPar);
		return null;
	}

	@Override
	public Boolean visit(DerivedVariable derVar) throws LEMSCompilerException,
			UndefinedSymbolException {
		buildDependeciesAndContext(derVar);
		return null;
	}

	@Override
	public Boolean visit(TimeDerivative dx) throws LEMSCompilerException,
			UndefinedSymbolException {
		buildDependeciesAndContext(dx);
		return null;
	}

	private void addDimValToSymbol(String symbolName, String symbolDef)
			throws LEMSCompilerException, UndefinedSymbolException {
		Symbol<?> resolved = (Symbol<?>) this.scope.resolve(symbolName);

		String valUnitRegEx = "\\s*([0-9-]*\\.?[0-9]*[eE]?[-+]?[0-9]+)?\\s*(\\w*)";
		Pattern pattern = Pattern.compile(valUnitRegEx);
		Matcher matcher = pattern.matcher(symbolDef);

		if (matcher.find()) {
			resolved.setValue(Double.parseDouble(matcher.group(1)));
			resolved.setUnit(this.lems.getUnitBySymbol(matcher.group(2)));
		} else {
			throw new LEMSCompilerException("Could not parse ",
					LEMSCompilerError.CantParseValueUnit);
		}

	}

	private void buildDependeciesAndContext(LemsNode typeDef)
			throws LEMSCompilerException, UndefinedSymbolException {

		String defName = ((INamed) typeDef).getName();
		String defValue = ((IValueDefinition) typeDef).getValueDefinition();
		expressions.put(defName, defValue);
		dependencies.addNode(defName);

		for (String dep : ExpressionParser.listSymbolsInExpression(defValue)) {
			ISymbol<?> resolved = scope.resolve(dep);
			// TODO: where/how should errors be handled?
			if (resolved == null) {
				String err = MessageFormat
						.format("Symbol[{0}] undefined in  expression [{1}], at [({2}) {3}]",
								dep,
								defValue,
								typeDef.getClass().getSimpleName(), defName);
				throw new LEMSCompilerException(err,
						LEMSCompilerError.UndefinedSymbol);
			}
			try {
				// StateVariables and Requirements are in scope but evaluate to
				// Null
				Double val = resolved.evaluate();
				if (null != val) { // UGLY: guard for StateVariable
					context.put(dep, val);
					unitContext.put(dep, resolved.getUnit());
				}
			} catch (UndefinedSymbolException e) { // will resolve later, in
													// order
				dependencies.addNode(dep);
				dependencies.addEdge(defName, dep);
				expressions.put(dep, resolved.getValueDefinition());
			}
		}
	}

	public void evalInterdependentExprs() throws UndefinedSymbolException {

		List<String> sortedDeps = TopologicalSort.sort(dependencies);
		Collections.reverse(sortedDeps);

		for (String depName : sortedDeps) {
			ISymbol<?> resolved = scope.resolve(depName);
			Double val = null;
			Unit<?> unit = null;
			try {
				val = ExpressionParser.evaluateInContext(
						expressions.get(depName), context);
				// TODO: is this duplicated work? (we already check comptype defs
				// 		 for correct dims)
				unit = ExpressionParser.dimensionalAnalysis(
						expressions.get(depName), unitContext);
				resolved.setValue(val);
				resolved.setUnit(unit);
				context.put(depName, val);
				unitContext.put(depName, unit);
			} catch (UndefinedSymbolException e) {
				// OK, those are symbolic expressions
				((SymbolicExpression<?>) resolved).getContext().putAll(context);
				((SymbolicExpression<?>) resolved).getUnitContext().putAll(
						unitContext);
			}
		}
	}

	private void clearMaps() {
		expressions = new HashMap<String, String>();
		context = new HashMap<String, Double>();
		unitContext = new HashMap<String, Unit<?>>();
		dependencies = new DirectedGraph<String>();
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
