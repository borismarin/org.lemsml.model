package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.compiler.semantic.visitors.ResolveUnitsDimensions;
import org.lemsml.model.extended.Lems;

public class UnitDimensionResolver extends ASemanticPass {

	UnitDimensionResolver(Lems lems) throws Throwable{
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new ResolveUnitsDimensions(lems));
	}

}
