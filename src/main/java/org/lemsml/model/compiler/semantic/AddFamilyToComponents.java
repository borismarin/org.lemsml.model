package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.compiler.semantic.visitors.FamilyVisitor;
import org.lemsml.model.extended.Lems;

public class AddFamilyToComponents extends ASemanticPass {

	AddFamilyToComponents(Lems lems) throws Throwable {
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new FamilyVisitor(lems));
	}

}
