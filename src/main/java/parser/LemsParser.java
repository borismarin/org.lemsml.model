package parser;

import java.io.File;
import java.util.List;

import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;
import org.lemsml.visitors.Visitable;
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

	public void visitList(List<? extends Visitable> visitables, Visitor<Object,Throwable> visitor)
			throws Throwable {
		for(Visitable toVisit : visitables) {
			toVisit.accept(visitor);
		}
	}
	

	public void traverseWithVisitor(Visitable tree, Visitor<Object, Throwable> visitor)
			throws Throwable {

		TraversingVisitor<Object, Throwable> tv = new TraversingVisitor<Object, Throwable>(
				new DepthFirstTraverserImpl<Throwable>(), visitor);
		tv.setTraverseFirst(true);
		tree.accept(tv);
	}

	public void populateNameComponentTypeHM() throws Throwable {
//		traverseWithVisitor((Visitable) lems.getComponentType(), new BuildNameComponentTypeMapVisitor(lems));
		visitList(lems.getComponentType(), new BuildNameComponentTypeMapVisitor(lems));
	}

	public void decorateComponentsWithType() throws Throwable {
		visitList(lems.getComponent(), new AddTypeToComponentVisitor(lems));
	}
}
