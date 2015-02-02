package compiler.semantic;

import compiler.semantic.visitors.AddTypeToComponent;
import compiler.semantic.visitors.BuildNameToDimensionMap;
import compiler.semantic.visitors.BuildNameToCompTypeMap;
import compiler.semantic.visitors.BuildSymbolToUnitMap;
import extended.Lems;

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
		BuildNameToCompTypeMap buildComponentTypeMapVisitor = new BuildNameToCompTypeMap(lems);
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
