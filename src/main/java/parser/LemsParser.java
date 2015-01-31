package parser;

import java.io.File;
import java.net.URL;

import visitors.AddTypeToComponentVisitor;
import visitors.AddUOMToDimensionVisitor;
import visitors.BuildNameComponentTypeMapVisitor;
import visitors.ProcessIncludesVisitor;
import extended.Lems;

public class LemsParser {

	Lems lems;
	File cwd;
	File schema;

	public Lems getLems() {
		return lems;
	}

	public LemsParser(File lemsdocument, File schema) {
		this.lems = LemsXmlUtils.unmarshall(lemsdocument, schema);
		this.cwd = lemsdocument.getParentFile();
		this.schema = schema;
	}
	
	public LemsParser(URL lemsdocumenturl, File schema) {
		this.lems = LemsXmlUtils.unmarshall(lemsdocumenturl, schema);
		//this.cwd = lemsdocumenturl.getParentFile();
		this.schema = schema;
	}

	public void populateNameComponentTypeHM() throws Throwable {
		LemsVisitorUtils.traverseWithVisitor(lems, new BuildNameComponentTypeMapVisitor(lems));
	}

	public void decorateComponentsWithType() throws Throwable {
		LemsVisitorUtils.traverseWithVisitor(lems, new AddTypeToComponentVisitor(lems));
	}

	public void processIncludes() throws Throwable {
		ProcessIncludesVisitor incProcVisitor = new ProcessIncludesVisitor(lems, schema, cwd);
		//traverseWithVisitor((Visitable) lems, incProcVisitor);
		LemsVisitorUtils.visitList(lems.getInclude(), incProcVisitor);
		this.lems = incProcVisitor.getResolvedLems();
	}

	public void processDimensions() throws Throwable {
		LemsVisitorUtils.traverseWithVisitor(lems, new AddUOMToDimensionVisitor(lems));
	}
}
