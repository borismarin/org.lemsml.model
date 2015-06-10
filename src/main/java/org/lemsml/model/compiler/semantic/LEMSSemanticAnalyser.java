package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.AddFamilyToComponents;
import org.lemsml.model.compiler.semantic.visitors.AddTypeToComponent;
import org.lemsml.model.compiler.semantic.visitors.BuildNameToObjectMaps;
import org.lemsml.model.compiler.semantic.visitors.BuildScope;
import org.lemsml.model.compiler.semantic.visitors.ResolveStatelessVariables;
import org.lemsml.model.compiler.semantic.visitors.CheckExpressionDimensions;
import org.lemsml.model.compiler.semantic.visitors.DimensionalAnalysis;
import org.lemsml.model.compiler.semantic.visitors.ProcessTypeExtensions;
import org.lemsml.model.compiler.semantic.visitors.ResolveSymbolicExpressions;
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

		ProcessTypeExtensions typeExtender = new ProcessTypeExtensions(lems);
		lems.accept(typeExtender);
		typeExtender.visitToposortedTypes();

		AddFamilyToComponents adoptionAgent = new AddFamilyToComponents(lems);
		lems.accept(adoptionAgent);

		
		BuildScope scopeBuilder = new BuildScope(lems);
		lems.accept(scopeBuilder);

		CheckExpressionDimensions dimCalc = new CheckExpressionDimensions(lems);
		lems.accept(dimCalc);

		ResolveStatelessVariables evalResolver = new ResolveStatelessVariables(lems);
		lems.accept(evalResolver);

		DimensionalAnalysis dimensionAnalyzer = new DimensionalAnalysis(lems);
		lems.accept(dimensionAnalyzer);

		ResolveSymbolicExpressions symExprResolver = new ResolveSymbolicExpressions(lems);
		lems.accept(symExprResolver);

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
