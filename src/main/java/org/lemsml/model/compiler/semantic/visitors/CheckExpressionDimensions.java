package org.lemsml.model.compiler.semantic.visitors;

import static tec.units.ri.AbstractUnit.ONE;
import static tec.units.ri.util.SI.SECOND;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
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
import org.lemsml.model.TimeDerivative;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.TraversingVisitor;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.google.common.collect.Sets.SetView;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.TopologicalSort;

public class CheckExpressionDimensions extends BaseVisitor<Void, Throwable> {

	private Lems lems;
	private Map<String, String> expressions;
	private Map<String, Unit<?>> unitContext;
	private DirectedGraph<String> dependencies;

	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(ProcessIncludes.class);

	public CheckExpressionDimensions(Lems lems) {
		this.lems = lems;
        expressions = new HashMap<String, String>();
        unitContext = new HashMap<String, Unit<?>>();
        dependencies = new DirectedGraph<String>();
	}

	//TODO: have a "traverse components / comptypes" traverser, to be used here and with buildcontext
	@Override
	public Void visit(org.lemsml.model.Lems lems) throws Throwable {
		for(ComponentType compType : lems.getComponentTypes()){
			compType.accept(this);
			CheckExpressionDimensions exprCalc = new CheckExpressionDimensions(this.lems);
			TraversingVisitor<Void, Throwable> trav = new TraversingVisitor<Void, Throwable>(
					new DepthFirstTraverserExt<Throwable>(),
					exprCalc);
			compType.accept(trav);
			exprCalc.evalInterdependentExprs(compType);
		}
		//TODO: decide whether constant accept expression
//		for(Constant ctt : lems.getConstants()){
//			ctt.accept(this);
//		}
		return null;
	}

	@Override
	public Void visit(DerivedParameter typeDef) throws LEMSCompilerException {

		Unit<?> dim = this.lems.getDimensionByName(typeDef.getDimension());
		buildDependenciesAndContext(typeDef.getName(), typeDef.getValue(), dim);
		return null;
	}

	@Override
	public Void visit(DerivedVariable derVar) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(derVar.getDimension());
		//TODO: think about "Selected" dervars
		if(derVar.getSelect() != null){
			unitContext.put(derVar.getName(), dim);
		}else{
			buildDependenciesAndContext(derVar.getName(), derVar.getValue(), dim);
		}
		return null;
	}

	@Override
	public Void visit(Parameter parDef) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(parDef.getDimension());
		unitContext.put(parDef.getName(), dim);
		return null;
	}

	@Override
	public Void visit(Constant ctt) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(ctt.getDimension());
		unitContext.put(ctt.getName(), dim);
		return null;
	}

	@Override
	public Void visit(Requirement req) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(req.getDimension());
		unitContext.put(req.getName(), dim);
		return null;
	}

	@Override
	public Void visit(StateVariable x) throws LEMSCompilerException {
		Unit<?> dim = this.lems.getDimensionByName(x.getDimension());
		unitContext.put(x.getName(), dim);
		return null;
	}
	

	@Override
	public Void visit(TimeDerivative dx) throws LEMSCompilerException {
		buildDependenciesAndContext(generateTimeDerivativeName(dx), dx.getValue(), unitContext.get(dx.getVariable()).divide(SECOND));
		return null;
	}

	private void buildDependenciesAndContext(String name, String value,
			Unit<?> dimension) throws LEMSCompilerException {
		expressions.put(name, value);
		dependencies.addNode(name);
		unitContext.put(name, dimension);
		for (String dep : ExpressionParser.listSymbolsInExpression(value)) {
			dependencies.addNode(dep);
			dependencies.addEdge(name, dep);
		}
	}
	
	public void evalInterdependentExprs(ComponentType compType) throws LEMSCompilerException {
		List<String> sorted = TopologicalSort.sort(dependencies);
		Collections.reverse(sorted);
		for (String depName : sorted) {
			String expression = expressions.get(depName);
			if (null != expression){
				Set<String> edgesFrom = dependencies.edgesFrom(depName);
				Set<String> haveUnits = unitContext.keySet();
				SetView<String> difference = com.google.common.collect.Sets.difference(edgesFrom, haveUnits);
				if(difference.size() != 0){
					String err = MessageFormat
							.format("Symbol(s) {0} undefined in  expression [{1}], at [({2}) {3}]",
									difference.toString(), expression, compType.getClass().getSimpleName(), compType.getName());
					throw new LEMSCompilerException(err, LEMSCompilerError.UndefinedSymbol);
				}
				else{
					Unit<?> unit = ONE;
					try {
						unit = ExpressionParser.dimensionalAnalysis(expression, unitContext);
					} catch (NumberFormatException e) {
						logger.warn("Cannot perform unit checking for variable exponent. " + e.getLocalizedMessage());
						logger.warn("Will tacitly assume that the base is adimensional, which is reasonable given that there be dragons with fractional units.");
					}

					unitContext.put(depName, unit);
				}
			}
		}
	}	
	
	public static String generateTimeDerivativeName(TimeDerivative dx) {
		return "d" + dx.getVariable() + "_dt";
	}
	

}
