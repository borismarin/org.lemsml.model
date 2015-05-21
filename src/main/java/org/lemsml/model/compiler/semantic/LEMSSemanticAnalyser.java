package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.AddTypeToComponent;
import org.lemsml.model.compiler.semantic.visitors.BuildNameToObjectMaps;
import org.lemsml.model.compiler.semantic.visitors.BuildScope;
import org.lemsml.model.compiler.semantic.visitors.ResolveEvaluables;
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

		AddTypeToComponent addTypeToComponent = new AddTypeToComponent(lems);
		lems.accept(addTypeToComponent);

		BuildScope scopeBuilder = new BuildScope(lems);
		lems.accept(scopeBuilder);

		ResolveEvaluables evalResolver = new ResolveEvaluables(lems);
		lems.accept(evalResolver);

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
