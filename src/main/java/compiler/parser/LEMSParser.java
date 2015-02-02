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
	 * @param lemsdocument
	 * @param schema
	 */
	public LEMSParser(File lemsdocument, File schema)
	{
		this.lems = LEMSXMLReader.unmarshall(lemsdocument, schema);
		this.cwd = lemsdocument.getParentFile();
		this.schema = schema;
	}

	/**
	 * @param lemsdocumenturl
	 * @param schema
	 */
	public LEMSParser(URL lemsdocumenturl, File schema)
	{
		this.lems = LEMSXMLReader.unmarshall(lemsdocumenturl, schema);
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
