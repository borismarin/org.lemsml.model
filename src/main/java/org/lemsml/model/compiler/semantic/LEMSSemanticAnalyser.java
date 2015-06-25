package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DimensionalAnalysis;
import org.lemsml.model.compiler.semantic.visitors.ResolveSymbols;
import org.lemsml.model.extended.Lems;

/**
 * @author borismarin
 *
 */
public class LEMSSemanticAnalyser {

	private Lems lems;
	private BuildNameToObjMaps mapBuilder;
	private ResolveUnitsDimensions dimensionResolver;
	private DecorateComponentsWithType typeDecorator;
	private ExtendTypes typeExtender;
	private AddFamilyToComponents familyAdder;
	private BuildScope scopeBuilder;
	private CheckExpressionDimensions exprChecker;

	public LEMSSemanticAnalyser(Lems lems) throws Throwable {
		super();
		this.lems = lems;

		mapBuilder = new BuildNameToObjMaps(lems);
		dimensionResolver = new ResolveUnitsDimensions(lems);
		typeDecorator = new DecorateComponentsWithType(lems);
		typeExtender = new ExtendTypes(lems);
		familyAdder = new AddFamilyToComponents(lems);
		scopeBuilder = new BuildScope(lems);
		exprChecker = new CheckExpressionDimensions(lems);
	}

	public Lems analyse() throws Throwable {

		mapBuilder.apply();
		dimensionResolver.apply();
		typeDecorator.apply();
		typeExtender.apply();
		familyAdder.apply();
		scopeBuilder.apply();

		exprChecker.apply();

		lems.accept(new ResolveSymbols(lems));

		DimensionalAnalysis dimensionAnalyzer = new DimensionalAnalysis(lems);
		lems.accept(dimensionAnalyzer);


		// TODO move error checking to dedicated visitors
		// ERROR CHECKING
		// Type mismatch
		// Undeclared variable
		// Reserved identifier misuse
		// Multiple declaration of variable in a scope
		// Accessing an out of scope variable
		// Actual and formal parameter mismatch

		return lems;

	}

}
