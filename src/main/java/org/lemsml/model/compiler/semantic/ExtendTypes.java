package org.lemsml.model.compiler.semantic;

import org.lemsml.model.compiler.semantic.visitors.TypeExtensionVisitor;
import org.lemsml.model.compiler.semantic.visitors.traversers.DepthFirstTraverserExt;
import org.lemsml.model.extended.Lems;

public class ExtendTypes extends ASemanticPass {

	private TypeExtensionVisitor visitor;

	ExtendTypes(Lems lems) throws Throwable{
		super.setLems(lems);
		super.setTraverser(new DepthFirstTraverserExt<Throwable>());
		super.setVisitor(new TypeExtensionVisitor(lems));
	}

	@Override
	protected void apply() throws Throwable{
		super.apply();
		setVisitor((TypeExtensionVisitor) super.getVisitor());
		getVisitor().visitToposortedTypes();
	}

	public TypeExtensionVisitor getVisitor() {
		return visitor;
	}

	public void setVisitor(TypeExtensionVisitor visitor) {
		this.visitor = visitor;
	}

}
