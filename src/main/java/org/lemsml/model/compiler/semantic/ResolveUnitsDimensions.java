package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.UnitsDimensionsVisitor;
import org.lemsml.model.compiler.semantic.visitors.traversers.DepthFirstTraverserExt;
import org.lemsml.model.extended.Lems;

public class ResolveUnitsDimensions extends ASemanticPass {

	ResolveUnitsDimensions(Lems lems) throws Throwable{
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new UnitsDimensionsVisitor(lems));
	}

}
