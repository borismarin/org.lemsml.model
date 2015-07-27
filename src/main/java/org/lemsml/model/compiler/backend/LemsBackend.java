package org.lemsml.model.compiler.backend;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.lemsml.model.compiler.parser.LEMSXMLWriter;
import org.lemsml.model.compiler.semantic.visitors.CopyIfNotIncluded;
import org.lemsml.model.compiler.semantic.visitors.ILemsProcessor;
import org.lemsml.model.compiler.semantic.visitors.RemoveIncludes;
import org.lemsml.model.compiler.semantic.visitors.traversers.TopLevelTraverser;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.TraversingVisitor;

public class LemsBackend {
	private Lems lemsDoc;
	private Boolean keepIncludes = false;

	public LemsBackend(Lems lemsDoc) {
		super();
		this.lemsDoc = lemsDoc;
	}

	public void generate(File outFile) throws Throwable, FileNotFoundException,
			JAXBException {

		Lems lems = this.lemsDoc;

		ILemsProcessor preprocessor;

		if (keepIncludes) {
			preprocessor = new CopyIfNotIncluded( lemsDoc.getDefinedIn());
		}
		else{
			preprocessor = new RemoveIncludes();
		}
			TraversingVisitor<Boolean, Throwable> includeCleanup = new TraversingVisitor<Boolean, Throwable>(
					new TopLevelTraverser<Throwable>(), preprocessor);
			lemsDoc.accept(includeCleanup);
			lems = preprocessor.getLems();

		LEMSXMLWriter.marshall(lems, outFile);
	}

	public Boolean getKeepIncludes() {
		return keepIncludes;
	}

	public void setKeepIncludes(Boolean keepIncludes) {
		this.keepIncludes = keepIncludes;
	}
}
