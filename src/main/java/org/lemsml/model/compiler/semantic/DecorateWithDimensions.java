package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DimToDimensionalVisitor;
import org.lemsml.model.compiler.semantic.visitors.traversers.DepthFirstTraverserExt;
import org.lemsml.model.extended.Lems;

public class DecorateWithDimensions extends ASemanticPass {

	DecorateWithDimensions(Lems lems) {
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new DimToDimensionalVisitor(lems));
	}

}
