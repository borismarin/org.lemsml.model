package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;

import javax.measure.Dimension;
import javax.measure.Unit;

import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.IScope;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

import expr_parser.utils.UndefinedParameterException;

/**
 * @author borismarin
 *
 */
public class DimensionalAnalysis extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;

	public DimensionalAnalysis(Lems lems) throws Throwable {
		this.lems = lems;
	}
	
	@Override
	public Boolean visit(org.lemsml.model.Lems lems) throws Throwable {
		checkScope(this.lems);
		for(Component comp : lems.getComponents()){
			comp.accept(this);
		}
		return null;
	}
	
	@Override
	public Boolean visit(Component comp) throws Throwable {
		checkScope(comp);
		return null;
	}

	private void checkScope(IScope scope) throws LEMSCompilerException{
		for(String symb : scope.getDefinedSymbols()){
			try{
				ISymbol<?> resolved = scope.resolve(symb);
				if(null != resolved.evaluate()){//UGLY: guard for StateVariable
					checkUnits(resolved, scope);
				}
			}
			catch(UndefinedParameterException e){
				//OK, since symbolic expressions can't be analysed until fully specified
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
		if (!dimFromValue.equals(dimFromType)) {
			String unitString = resolved.getUnit().toString();
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
