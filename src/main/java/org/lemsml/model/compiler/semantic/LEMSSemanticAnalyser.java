package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.AddTypeToComponent;
import org.lemsml.model.compiler.semantic.visitors.BuildNameToObjectMaps;
import org.lemsml.model.compiler.semantic.visitors.BuildScope;
import org.lemsml.model.compiler.semantic.visitors.BuildStatelessScope;
import org.lemsml.model.compiler.semantic.visitors.CheckExpressionDimensions;
import org.lemsml.model.compiler.semantic.visitors.DimensionalAnalysis;
import org.lemsml.model.compiler.semantic.visitors.ResolveUnitsDimensions;
import org.lemsml.model.extended.Lems;

/**
 * @author borismarin
 *
 */
public class LEMSSemanticAnalyser {

	private Lems lems;

	/**
	 * @param lems
	 */
	public LEMSSemanticAnalyser(Lems lems) {
		super();
		this.lems = lems;
	}

	/**
	 * @throws Throwable
	 * 
	 */
	public Lems analyse() throws Throwable {

		// DECORATION

		BuildNameToObjectMaps mapBuilder = new BuildNameToObjectMaps(lems);
		lems.accept(mapBuilder);

		ResolveUnitsDimensions unitResolver = new ResolveUnitsDimensions(lems);
		lems.accept(unitResolver);

		AddTypeToComponent addTypeToComponent = new AddTypeToComponent(lems);
		lems.accept(addTypeToComponent);
		
		BuildScope scopeBuilder = new BuildScope(lems);
		lems.accept(scopeBuilder);

		CheckExpressionDimensions dimCalc = new CheckExpressionDimensions(lems);
		lems.accept(dimCalc);

		BuildStatelessScope evalResolver = new BuildStatelessScope(lems);
		lems.accept(evalResolver);

		DimensionalAnalysis dimensionAnalyzer = new DimensionalAnalysis(lems);
		lems.accept(dimensionAnalyzer);

		//BuildEvaluationContext ctxtBuilder = new BuildEvaluationContext(lems);
		//lems.accept(ctxtBuilder);

		// ERROR CHECKING
		// TODO
		// Type mismatch
		// Undeclared variable
		// Reserved identifier misuse
		// Multiple declaration of variable in a scope
		// Accessing an out of scope variable
		// Actual and formal parameter mismatch

		return lems;

	}

}
