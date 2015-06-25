package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.ScopeTraverser;
import org.lemsml.model.compiler.semantic.visitors.ScopeVisitor;
import org.lemsml.model.extended.Lems;

public class BuildScope extends ASemanticPass {

	BuildScope(Lems lems) throws Throwable {
		super.setLems(lems);
		super.setTraverser(new ScopeTraverser<Throwable>());
		super.setVisitor(new ScopeVisitor(lems));
	}

}
