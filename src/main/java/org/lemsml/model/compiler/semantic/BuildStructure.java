package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.StructureVisitor;
import org.lemsml.model.compiler.semantic.visitors.traversers.DepthFirstTraverserExt;
import org.lemsml.model.extended.Lems;

public class BuildStructure extends ASemanticPass {


	public BuildStructure(Lems lems) throws Throwable {
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new StructureVisitor(lems));
	}

}
