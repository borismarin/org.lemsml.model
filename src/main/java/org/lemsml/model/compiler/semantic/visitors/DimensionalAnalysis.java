package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;

import javax.measure.Dimension;
import javax.measure.Unit;

import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.SymbolicExpression;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class DimensionalAnalysis extends TraversingVisitor<Void, Throwable> {

	private Lems lems;

	public DimensionalAnalysis(Lems lems) throws LEMSCompilerException {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Void, Throwable>());
		this.lems = lems;
		checkScope(lems);
	}
	
	@Override
	public Void visit(Component comp) throws Throwable {
		checkScope(comp);
		return null;
	}

	private void checkScope(IScope scope) throws LEMSCompilerException{
		for(String symb : scope.getDefinedSymbols()){
			if(!(scope.resolve(symb) instanceof SymbolicExpression<?>)){
				checkUnits(scope.resolve(symb), scope);
			}
		}
	}

	private void checkUnits(ISymbol<?> resolved, IScope scope)
			throws LEMSCompilerException {

		Dimension dimFromValue = resolved.getUnit().getDimension();
		String dimNameFromType = ((NamedDimensionalType) resolved.getType())
				.getDimension();
		Unit<?> uomUnitFromType = this.lems.getDimensionByName(dimNameFromType);
		if (uomUnitFromType == null) {
			String err = MessageFormat
					.format("Undefined Dimension [{0}]; used in [({1}) {2}] defined in [{3}].",
							dimNameFromType,
							resolved.getType().getClass().getSimpleName(),
							resolved.getName(),
							scope.getScopeName());
			throw new LEMSCompilerException(err,
					LEMSCompilerError.UndefinedDimension);
		}
		Dimension dimFromType = uomUnitFromType.getDimension();
		String unitString = resolved.getDimensionalValue().getUnit().toString();
		if (!dimFromValue.equals(dimFromType)) {
			String err = MessageFormat
					.format("Unit mismatch for [({0}) {1}] defined in [{2}]:"
							+ " Expecting  [{3}], but"
							+ " dimension of [{4}] is [{5}].",
							resolved.getType().getClass().getSimpleName(),
							resolved.getName(), scope.getScopeName(),
							dimFromType.toString(),
							unitString,
							dimFromValue.toString());
			throw new LEMSCompilerException(err,
					LEMSCompilerError.DimensionalAnalysis);
		}
	}
}
