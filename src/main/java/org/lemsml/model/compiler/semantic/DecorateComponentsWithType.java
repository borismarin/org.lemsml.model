package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.compiler.semantic.visitors.TypeToComponentVisitor;
import org.lemsml.model.extended.Lems;

public class DecorateComponentsWithType extends ASemanticPass {

	DecorateComponentsWithType(Lems lems) {
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new TypeToComponentVisitor(lems));
	}

}
