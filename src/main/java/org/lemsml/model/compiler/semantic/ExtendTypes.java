package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.DepthFirstTraverserExt;
import org.lemsml.model.compiler.semantic.visitors.TypeExtensionVisitor;
import org.lemsml.model.extended.Lems;

public class ExtendTypes extends ASemanticPass {

	ExtendTypes(Lems lems) throws Throwable{
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new TypeExtensionVisitor(lems));
	}

	@Override
	protected void apply() throws Throwable{
		super.apply();
		//TODO: is there a better way?
		((TypeExtensionVisitor) super.getVisitor()).visitToposortedTypes();
	}

}
