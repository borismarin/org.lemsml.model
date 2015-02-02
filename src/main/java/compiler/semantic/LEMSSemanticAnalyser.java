package compiler.semantic;

import compiler.semantic.visitors.AddTypeToComponentVisitor;
import compiler.semantic.visitors.BuildNameComponentTypeMapVisitor;

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
		BuildNameComponentTypeMapVisitor buildComponentTypeMapVisitor = new BuildNameComponentTypeMapVisitor(lems);
		lems.accept(buildComponentTypeMapVisitor);

		AddTypeToComponentVisitor addTypeToComponentVisitor = new AddTypeToComponentVisitor(lems);
		lems.accept(addTypeToComponentVisitor);

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
