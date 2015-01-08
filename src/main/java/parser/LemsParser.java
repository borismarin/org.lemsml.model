package parser;

import java.io.File;

import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;
import org.lemsml.visitors.Visitor;

import visitors.AddTypeToComponentVisitor;
import visitors.BuildNameComponentTypeMapVisitor;
import extended.Lems;

public class LemsParser {

	Lems lems;

	public Lems getLems() {
		return lems;
	}

	public LemsParser(File lemsdocument, File schema) {
		this.lems = LemsXmlUtils.unmarshall(lemsdocument, schema);
	}

	public void traverseWithVisitor(Visitor<Lems, Throwable> visitor)
			throws Throwable {

		TraversingVisitor<Lems, Throwable> tv = new TraversingVisitor<Lems, Throwable>(
				new DepthFirstTraverserImpl<Throwable>(), visitor);
		tv.setTraverseFirst(true);
		lems.accept(tv);
	}

	public void populateNameComponentTypeHM() throws Throwable {
		traverseWithVisitor(new BuildNameComponentTypeMapVisitor<Lems>(lems));
	}

	public void decorateComponentsWithType() throws Throwable {
		traverseWithVisitor(new AddTypeToComponentVisitor<Lems>(lems));
	}
}
