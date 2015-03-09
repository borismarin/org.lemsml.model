package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.AddTypeToComponent;
import org.lemsml.model.compiler.semantic.visitors.BuildNameToComponentTypeMap;
import org.lemsml.model.compiler.semantic.visitors.BuildNameToDimensionMap;
import org.lemsml.model.compiler.semantic.visitors.BuildSymbolToUnitMap;
import org.lemsml.model.extended.Lems;

/**
 * @author borismarin
 *
 */
public class LEMSSemanticAnalyser
{

	private Lems lems;

	/**
	 * @param lems
	 */
	public LEMSSemanticAnalyser(Lems lems)
	{
		super();
		this.lems = lems;
	}

	/**
	 * @throws Throwable
	 * 
	 */
	public void analyse() throws Throwable
	{

		// DECORATION
		BuildNameToComponentTypeMap buildComponentTypeMapVisitor = new BuildNameToComponentTypeMap(lems);
		lems.accept(buildComponentTypeMapVisitor);

		AddTypeToComponent addTypeToComponent = new AddTypeToComponent(lems);
		lems.accept(addTypeToComponent);

		BuildNameToDimensionMap buildNameToDimensionMap = new BuildNameToDimensionMap(lems);
		lems.accept(buildNameToDimensionMap);

		BuildSymbolToUnitMap buildSymbolToUnitMap = new BuildSymbolToUnitMap(lems);
		lems.accept(buildSymbolToUnitMap);

		// ERROR CHECKING
		// TODO
		// Type mismatch
		// Undeclared variable
		// Reserved identifier misuse
		// Multiple declaration of variable in a scope
		// Accessing an out of scope variable
		// Actual and formal parameter mismatch

	}

}
