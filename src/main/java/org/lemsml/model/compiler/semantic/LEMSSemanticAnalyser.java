package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.AddFamilyToComponents;
import org.lemsml.model.compiler.semantic.visitors.BuildScope;
import org.lemsml.model.compiler.semantic.visitors.CheckExpressionDimensions;
import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.compiler.semantic.visitors.DimensionalAnalysis;
import org.lemsml.model.compiler.semantic.visitors.ResolveSymbols;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.TraversingVisitor;
import org.lemsml.visitors.Visitor;

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

	public LEMSSemanticAnalyser(Lems lems) throws Throwable {
		super();
		this.lems = lems;

		mapBuilder = new BuildNameToObjMaps(lems);
		dimensionResolver = new ResolveUnitsDimensions(lems);
		typeDecorator = new DecorateComponentsWithType(lems);
		typeExtender = new ExtendTypes(lems);
	}

	public Lems analyse() throws Throwable {

		mapBuilder.apply();
		dimensionResolver.apply();
		typeDecorator.apply();
		typeExtender.apply();

		depthFirstWith(new AddFamilyToComponents(lems));

		lems.accept(new BuildScope(lems));

		lems.accept(new CheckExpressionDimensions(lems));

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

	private void depthFirstWith(Visitor<Boolean, Throwable> visitor) throws Throwable {
		TraversingVisitor<Boolean, Throwable> trav = new TraversingVisitor<Boolean, Throwable>(
				new DepthFirstTraverserExt<Throwable>(), visitor);
		lems.accept(trav);
	}

}
