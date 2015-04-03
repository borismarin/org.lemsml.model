package org.lemsml.model.compiler;

import java.io.File;

import org.lemsml.model.compiler.parser.LEMSParser;
import org.lemsml.model.compiler.semantic.LEMSSemanticAnalyser;
import org.lemsml.model.extended.Lems;

/**
 * @author matteocantarelli
 * @author borismarin
 * 
 */
public class LEMSCompilerFrontend
{

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

	public LEMSCompilerFrontend(File lemsFile)
	{
		super();
		this.lemsFile = lemsFile;
		this.schema = getCurrentSchema();
	}

	private File getCurrentSchema()
	{
		//TODO: hardcode that somewhere else
		return new File(getClass().getResource("/Schemas/LEMS_v0.9.0.xsd").getFile());
	}

	/**
	 * @throws Throwable
	 * 
	 */
	public Lems generateLEMSDocument() throws Throwable
	{
		// First step: parse the LEMS file
		Lems parsedLems = parseLemsFile(lemsFile, schema);

		// Second step: perform semantic analysis
		semanticAnalysis(parsedLems);

		return parsedLems;
	}

	static public Lems parseLemsFile(File document, File schema) throws Throwable {
		LEMSParser parser = new LEMSParser(document, schema);
		return parser.parse();
	}
	
    static public Lems semanticAnalysis(Lems lemsDocument) throws Throwable{
		LEMSSemanticAnalyser semanticAnalyser = new LEMSSemanticAnalyser(lemsDocument);
		return semanticAnalyser.analyse();
	}

}
