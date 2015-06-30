package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;

import javax.measure.Dimension;
import javax.measure.Unit;

import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.IScope;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Symbol;
import org.lemsml.visitors.BaseVisitor;

import expr_parser.utils.UndefinedSymbolException;

/**
 * @author borismarin
 *
 */
public class DimensionalAnalysisVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;

	public DimensionalAnalysisVisitor(Lems lems) {
		this.lems = lems;
	}

	@Override
	public Boolean visit(org.lemsml.model.Lems lems) throws Throwable {
		checkScope(this.lems.getScope());
		return null;
	}

	@Override
	public Boolean visit(Component comp) throws LEMSCompilerException {
		checkScope(comp.getScope());
		return null;
	}

	private void checkScope(IScope scope) throws LEMSCompilerException{
		for(String symb : scope.getDefinedSymbols()){
			try{
				Symbol resolved = scope.resolve(symb);
				if(null != scope.evaluate(symb)){//UGLY: guard for StateVariable
					checkUnits(resolved, scope);
				}
			}
			catch(UndefinedSymbolException e){
				//OK, since symbolic expressions can't be analysed until fully specified
			}
		}
	}

	private void checkUnits(Symbol resolved, IScope scope)
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
							scope.toString());
			throw new LEMSCompilerException(err,
					LEMSCompilerError.UndefinedDimension);
		}
		Dimension dimFromType = uomUnitFromType.getDimension();
		//TODO: think about the need of this "wildcard" dimension
		if (!dimFromValue.equals(dimFromType) && !uomUnitFromType.equals(lems.getAnyDimension())) {
			String unitString = resolved.getUnit().toString();
			String err = MessageFormat
					.format("Unit mismatch for [({0}) {1}] defined in {2}:"
							+ " Expecting  [{3}], but"
							+ " dimension of [{4}] is [{5}].",
							resolved.getType().getClass().getSimpleName(),
							resolved.getName(),
							scope.toString(),
							dimFromType.toString(),
							unitString,
							dimFromValue.toString());
			throw new LEMSCompilerException(err,
					LEMSCompilerError.DimensionalAnalysis);
		}
	}
}
