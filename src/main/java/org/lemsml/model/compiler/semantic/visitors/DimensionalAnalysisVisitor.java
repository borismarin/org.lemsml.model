package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;

import javax.measure.Unit;

import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Symbol;
import org.lemsml.model.extended.interfaces.IScope;
import org.lemsml.model.extended.interfaces.IScoped;
import org.lemsml.visitors.BaseVisitor;

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
		checkScope(this.lems);
		return null;
	}

	@Override
	public Boolean visit(Component comp) throws LEMSCompilerException {
		checkScope(comp);
		return null;
	}

	private void checkScope(IScoped scoped) throws LEMSCompilerException {
		IScope scope = scoped.getScope();
		for (String symb : scope.getDefinedSymbols()) {
			Symbol resolved = scope.resolve(symb);
			checkUnits(resolved, scope);
		}
	}

	private void checkUnits(Symbol resolved, IScope scope) throws LEMSCompilerException {

		Unit<?> unitFromValue = null;
		try {
			unitFromValue = scope.evaluate(resolved.getName()).getUnit();
		} catch (LEMSCompilerException e) {
			if (e.getErrorCode().equals(LEMSCompilerError.MissingSymbolValue) ||
				e.getErrorCode().equals(LEMSCompilerError.NoMatchingCase))
				return;// OK, those are symbolic expressions
			else {
				throw e;
			}
		}

		String dimNameFromType = ((NamedDimensionalType) resolved.getType()).getDimension();
		Unit<?> unitFromType = this.lems.getDimensionByName(dimNameFromType);
		if (unitFromType == null) {
			String err = MessageFormat
					.format("Undefined Dimension [{0}]; used in [({1}) {2}] defined in [{3}].",
							dimNameFromType,
							resolved.getType().getClass().getSimpleName(),
							resolved.getName(),
							scope.toString());
			throw new LEMSCompilerException(err,
					LEMSCompilerError.UndefinedDimension);
		}
		//TODO: think about the need of this "wildcard" dimension
		if (!unitFromValue.isCompatible(unitFromType) && !unitFromType.equals(lems.getAnyDimension())) {
			String unitSymbol = unitFromValue.getSymbol();
			String err = MessageFormat
					.format("Unit mismatch for [({0}) {1}] defined in {2}:"
							+ " Expecting  [{3}], but"
							+ " dimension of [{4}] is [{5}].",
							resolved.getType().getClass().getSimpleName(),
							resolved.getName(),
							scope.toString(),
							unitFromType.toString(),
							unitSymbol,
							unitFromValue.toString());
			throw new LEMSCompilerException(err,
					LEMSCompilerError.DimensionalAnalysis);
		}
	}
}
