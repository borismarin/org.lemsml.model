/**
 * 
 */
package compiler;

import java.io.File;

import compiler.parser.LEMSParser;
import compiler.semantic.LEMSSemanticAnalyser;
import extended.Lems;

/**
 * @author matteocantarelli
 * 
 */
public class LEMSCompilerFrontend
{

	Lems lemsDocument;
	File lemsFile;
	File cwd;
	File schema;

	/**
	 * @param lemsFile
	 * @param lemsSchemaFile
	 * @param cwd
	 * @param schema
	 */
	public LEMSCompilerFrontend(File lemsFile, File lemsSchemaFile)
	{
		super();
		this.lemsFile = lemsFile;
		this.schema = lemsSchemaFile;
	}

	/**
	 * @throws Throwable
	 * 
	 */
	public Lems generateLEMSDocument() throws Throwable
	{
		// First step: parse the LEMS file
		LEMSParser parser = new LEMSParser(lemsFile, schema);
		lemsDocument = parser.parse();

		// Second step: perform semantic analysis
		LEMSSemanticAnalyser semanticAnalyser = new LEMSSemanticAnalyser(lemsDocument);
		semanticAnalyser.analyse();
		
		return lemsDocument;
	}


}
