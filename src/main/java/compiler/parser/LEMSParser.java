package compiler.parser;

import java.io.File;
import java.net.URL;

import compiler.semantic.visitors.ProcessIncludes;

import extended.Lems;

/**
 * @author borismarin
 *
 */
public class LEMSParser
{

	private Lems lems;
	private File cwd;
	private File schema;

	/**
	 * @param lemsDocFile
	 * @param schema
	 */
	public LEMSParser(File lemsDocFile, File schema)
	{
		this.lems = JaxbXMLReader.<Lems>unmarshall(lemsDocFile, schema);
		this.cwd = lemsDocFile.getParentFile();
		this.schema = schema;
	}

	/**
	 * @param lemsDocURL
	 * @param schema
	 */
	public LEMSParser(URL lemsDocURL, File schema)
	{
		this.lems = JaxbXMLReader.<Lems>unmarshall(lemsDocURL, schema);
		// this.cwd = lemsdocumenturl.getParentFile();
		this.schema = schema;
	}

	/**
	 * @throws Throwable
	 * 
	 */
	public Lems parse() throws Throwable
	{
		processIncludes();
		return lems;
	}

	/**
	 * @throws Throwable
	 */
	private void processIncludes() throws Throwable
	{
		ProcessIncludes processIncludes = new ProcessIncludes(lems, schema, cwd);
		lems.accept(processIncludes);
		lems = processIncludes.getInputLems();
	}

}
