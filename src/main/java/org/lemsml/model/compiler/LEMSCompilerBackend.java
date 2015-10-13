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
public class LEMSCompilerBackend {

	private File lemsFile;
	File schema;
	private LEMSSemanticAnalyser semanticAnalyser;

	/**
	 * @param lemsFile
	 * @param lemsSchemaFile
	 * @param cwd
	 * @param schema
	 */
	public LEMSCompilerBackend(File lemsFile, File lemsSchemaFile) {
		super();
		this.lemsFile = lemsFile;
		this.schema = lemsSchemaFile;
	}

	public LEMSCompilerBackend(File lemsFile) {
		super();
		this.lemsFile = lemsFile;
		this.schema = getCurrentSchema();
	}

	private File getCurrentSchema() {
		// TODO: hardcode that somewhere else
		return new File(getClass().getResource("/Schemas/LEMS_v0.9.0.xsd")
				.getFile());
	}

	/**
	 * @throws Throwable
	 *
	 */
	public Lems generateLEMSDocument() throws Throwable {
		// First step: parse the LEMS file
		Lems parsedLems = parseLemsFile(lemsFile, schema);

		// Second step: perform semantic analysis
		semanticAnalysis(parsedLems);

		return parsedLems;
	}

	static public Lems parseLemsFile(File document, File schema)
			throws Throwable {
		LEMSParser parser = new LEMSParser(document, schema);
		return parser.parse();
	}

	public Lems semanticAnalysis(Lems lemsDocument) throws Throwable {
		setSemanticAnalyser(new LEMSSemanticAnalyser(lemsDocument));
		return getSemanticAnalyser().analyse();
	}

	public LEMSSemanticAnalyser getSemanticAnalyser() {
		return semanticAnalyser;
	}

	public void setSemanticAnalyser(LEMSSemanticAnalyser semanticAnalyser) {
		this.semanticAnalyser = semanticAnalyser;
	}

}
