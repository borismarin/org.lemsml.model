package parser;

import java.util.List;

import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;
import org.lemsml.visitors.Visitable;
import org.lemsml.visitors.Visitor;

public class LemsVisitorUtils {

	public static void visitList(List<? extends Visitable> visitables, Visitor<Object,Throwable> visitor)
			throws Throwable {
		for(Visitable toVisit : visitables) {
			toVisit.accept(visitor);
		}
	}
	

	public static void traverseWithVisitor(Visitable tree, Visitor<Object, Throwable> visitor)
			throws Throwable {

		TraversingVisitor<Object, Throwable> tv = new TraversingVisitor<Object, Throwable>(
				new DepthFirstTraverserImpl<Throwable>(), visitor);
		tv.setTraverseFirst(true);
		tree.accept(tv);
	}

	
}
