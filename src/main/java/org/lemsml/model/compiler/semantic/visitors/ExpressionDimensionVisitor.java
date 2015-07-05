package org.lemsml.model.compiler.semantic.visitors;

import static tec.units.ri.AbstractUnit.ONE;
import static tec.units.ri.unit.SI.SECOND;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.Unit;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Parameter;
import org.lemsml.model.Requirement;
import org.lemsml.model.StateVariable;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.INamedValueDefinition;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.TimeDerivative;
import org.lemsml.visitors.BaseVisitor;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.TopologicalSort;

public class ExpressionDimensionVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;
	private Map<String, String> expressions;
	private Map<String, Unit<?>> unitContext;
	private DirectedGraph<String> dependencies;
	private HashMap<String, INamedValueDefinition> nameToObj;
	ComponentType context = null;
	private Map<String, Unit<?>> globalUnitContext = new HashMap<String, Unit<?>>();

	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(ProcessIncludes.class);

	public ExpressionDimensionVisitor(Lems lems) {
		this.lems = lems;
		clearMaps();
	}

	public Boolean visit(org.lemsml.model.extended.ComponentType compType)
			throws LEMSCompilerException {
		context = compType;
		// TraverseFirst expected (so the method below will eval correctly)
		evalInterdependentExprs();
		clearMaps();
		return null;
	}

	@Override
	public Boolean visit(DerivedParameter typeDef) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(typeDef.getDimension());
		buildDependenciesAndContext(typeDef, dim);
		return null;
	}

	@Override
	public Boolean visit(DerivedVariable derVar) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(derVar.getDimension());
		// TODO: think about "Selected" dervars
		if (derVar.getSelect() != null) {
			unitContext.put(derVar.getName(), dim);
		} else {
			buildDependenciesAndContext(derVar, dim);
		}
		return null;
	}

	@Override
	public Boolean visit(Parameter parDef) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(parDef.getDimension());
		unitContext.put(parDef.getName(), dim);
		return null;
	}

	@Override
	public Boolean visit(Constant ctt) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(ctt.getDimension());
		if(context != null)
			unitContext.put(ctt.getName(), dim);
		else
			globalUnitContext.put(ctt.getName(), dim);

		return null;
	}

	@Override
	public Boolean visit(Requirement req) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(req.getDimension());
		unitContext.put(req.getName(), dim);
		return null;
	}

	@Override
	public Boolean visit(StateVariable x) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(x.getDimension());
		unitContext.put(x.getName(), dim);
		return null;
	}

	@Override
	public Boolean visit(TimeDerivative dx) throws LEMSCompilerException {
		buildDependenciesAndContext(dx, unitContext.get(dx.getVariable()).divide(SECOND));
		return null;
	}

	private void buildDependenciesAndContext(INamedValueDefinition node,
			Unit<?> dimension) throws LEMSCompilerException {
		String name = node.getName();
		String value = node.getValueDefinition();
		nameToObj.put(name, node);
		expressions.put(name, value);
		dependencies.addNode(name);
		unitContext.put(name, dimension);
		for (String dep : ExpressionParser.listSymbolsInExpression(value)) {
			dependencies.addNode(dep);
			dependencies.addEdge(name, dep);
		}
	}

	private void evalInterdependentExprs()
			throws LEMSCompilerException {
		List<String> sorted = TopologicalSort.sort(dependencies);
		Collections.reverse(sorted);
		for (String depName : sorted) {
			String expression = expressions.get(depName);
			if (null != expression) {
				Set<String> edgesFrom = dependencies.edgesFrom(depName);
				Set<String> knownSymbols = new HashSet<String>();
				knownSymbols.addAll(unitContext.keySet());
				knownSymbols.addAll(globalUnitContext.keySet());
				INamedValueDefinition node = nameToObj.get(depName);
				SetView<String> difference = Sets.difference(edgesFrom, knownSymbols);
				if (difference.size() != 0) {
					String err = MessageFormat
							.format("Unit for Symbol(s) {0} undefined in expression [{1}]"
									+ ", in  [({2}) {3}] in [(ComponentType) {4}]",
									difference.toString(),
									expression,
									node.getClass().getSimpleName(),
									node.getName(),
									context.getName());
					throw new LEMSCompilerException(err, LEMSCompilerError.UndefinedSymbol);
				} else {
					Unit<?> unit = ONE;
					try {
						HashMap<String, Unit<?>> extendedUnits = new HashMap<String, Unit<?>>();
						extendedUnits.putAll(globalUnitContext);
						extendedUnits.putAll(unitContext);
						unit = ExpressionParser.dimensionalAnalysis(expression, extendedUnits);
					} catch (NumberFormatException e) {
						logger.warn("Cannot perform unit checking for variable exponent. "
								+ e.getLocalizedMessage());
						logger.warn("Will tacitly assume that the base is adimensional,"
								+ "which is reasonable given that there be dragons with fractional units.");
					}
					checkUnits(node, unit, unitContext.get(depName));
				}
			}
		}
	}

	private void checkUnits(INamedValueDefinition node, Unit<?> declaredDim,
			Unit<?> calculatedDim) throws LEMSCompilerException {

		if (!declaredDim.equals(calculatedDim)) {
			String err = MessageFormat.format(
					"Unit mismatch for [({0}) {1}] defined in [{2}]:"
							+ " Expecting  [{3}], but"
							+ " dimension of [{4}] is [{5}].",
							node.getClass().getSimpleName(),
							node.getName(),
							context.getName(),
							declaredDim.toString(),
							node.getValueDefinition(),
							calculatedDim);
			throw new LEMSCompilerException(err, LEMSCompilerError.DimensionalAnalysis);
		}
	}

	public void clearMaps() {
		expressions = new HashMap<String, String>();
		unitContext = new HashMap<String, Unit<?>>();
		dependencies = new DirectedGraph<String>();
		nameToObj = new HashMap<String, INamedValueDefinition>();
	}

}
