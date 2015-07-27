package org.lemsml.model.compiler.parser;

import java.io.File;
import java.net.URL;

import org.lemsml.model.compiler.semantic.visitors.DecorateWithSourceFile;
import org.lemsml.model.compiler.semantic.visitors.ProcessIncludes;
import org.lemsml.model.compiler.semantic.visitors.traversers.DepthFirstTraverserExt;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class LEMSParser {

	private Lems lems;
	private File cwd;
	private File sourceDoc;
	private File schema;

	/**
	 * @param lemsDocFile
	 * @param schema
	 */
	public LEMSParser(File lemsDocFile, File schema) {
		this.lems = LEMSXMLReader.unmarshall(lemsDocFile, schema);
		this.sourceDoc = lemsDocFile;
		this.cwd = lemsDocFile.getParentFile();
		this.schema = schema;
	}

	/**
	 * @param lemsDocURL
	 * @param schema
	 */
	public LEMSParser(URL lemsDocURL, File schema) {
		// TODO : @adrianq added this but it is broken
		this.lems = LEMSXMLReader.unmarshall(lemsDocURL, schema);
		// this.cwd = lemsdocumenturl.getParentFile();
		this.schema = schema;
	}

	/**
	 * @throws Throwable
	 *
	 */
	public Lems parse() throws Throwable {
		processIncludes();
		return lems;
	}

	/**
	 * @throws Throwable
	 */
	private void processIncludes() throws Throwable {

		// decorate elements to be added with source file
		TraversingVisitor<Boolean, Throwable> addFile = new TraversingVisitor<Boolean, Throwable>(
				new DepthFirstTraverserExt<Throwable>(),
				new DecorateWithSourceFile(sourceDoc));
		lems.accept(addFile);

		ProcessIncludes processIncludes = new ProcessIncludes(lems, schema, cwd);
		lems.accept(processIncludes);
		lems = processIncludes.getInputLems();
	}

}
