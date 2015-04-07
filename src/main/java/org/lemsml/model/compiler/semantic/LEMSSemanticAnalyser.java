package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.AddTypeToComponent;
import org.lemsml.model.compiler.semantic.visitors.BuildNameToObjectMaps;
import org.lemsml.model.compiler.semantic.visitors.AddParameterValuesToComponent;
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

		AddParameterValuesToComponent paramAdder = new AddParameterValuesToComponent(
				lems);
		lems.accept(paramAdder);

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
