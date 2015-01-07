package org.lemsml.model.test;

import org.lemsml.model.ComponentType;
import org.lemsml.visitors.BaseVisitor;

import extended.Lems;


public class BuildNameComponentTypeMapVisitor<Void> extends BaseVisitor<Void, Throwable> {
	private Lems lems;

	
	public BuildNameComponentTypeMapVisitor(Lems lems) {
		this.lems = lems;
	}


	@Override
    public Void visit(ComponentType ct) throws Throwable {
		this.lems.registerComponentType(ct.getName(), ct);
		return null;
    }


	public Lems getLems() {
		return lems;
	}

	public void setLems(Lems lems) {
		this.lems = lems;
	}

}
