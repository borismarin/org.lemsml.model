package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.BuildNameToObjectMaps;
import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.extended.Lems;

public class MapBuilder extends ASemanticPass {

	MapBuilder(Lems lems) {
		this.setLems(lems);
		this.setTraverser(new DepthFirstTraverserExt<Throwable>());
		this.setVisitor(new BuildNameToObjectMaps(lems));
	}

}
