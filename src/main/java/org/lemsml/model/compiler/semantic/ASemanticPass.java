package org.lemsml.model.compiler.semantic;

import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.Traverser;
import org.lemsml.visitors.TraversingVisitor;
import org.lemsml.visitors.Visitor;

public abstract class ASemanticPass {

	private Lems lems;
	private Visitor<Boolean, Throwable> visitor;
	private Traverser<Throwable> traverser;
	private boolean traverseFirst = false;

	protected void apply() throws Throwable {
		traverseVisit();
	}

	public void traverseVisit() throws Throwable {
		TraversingVisitor<Boolean, Throwable> trav = new TraversingVisitor<Boolean, Throwable>(
				this.getTraverser(), this.visitor);
		trav.setTraverseFirst(traverseFirst);
		this.getLems().accept(trav);
	}

	public Lems getLems() {
		return lems;
	}

	public void setLems(Lems lems) {
		this.lems = lems;
	}

	public Traverser<Throwable> getTraverser() {
		return traverser;
	}

	public void setTraverser(Traverser<Throwable> traverser) {
		this.traverser = traverser;
	}

	public Visitor<Boolean, Throwable> getVisitor() {
		return visitor;
	}

	public void setVisitor(Visitor<Boolean, Throwable> visitor) {
		this.visitor = visitor;
	}

	public void setTraverseFirst(boolean b) {
		this.traverseFirst = b;
	}
}
