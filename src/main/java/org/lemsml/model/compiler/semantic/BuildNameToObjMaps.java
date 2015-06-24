package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.NameObjMapVisitor;
import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.extended.Lems;

public class BuildNameToObjMaps extends ASemanticPass {

	BuildNameToObjMaps(Lems lems) {
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new NameObjMapVisitor(lems));
	}

}
