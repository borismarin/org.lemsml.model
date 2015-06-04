package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;


/**
 * @author borismarin
 *
 */
public class ProcessTypeExtensions extends TraversingVisitor<Boolean, Throwable> {

	private Lems lems;

	public ProcessTypeExtensions(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	@Override
	public Boolean visit(ComponentType compType) throws Throwable {

		// will copy the content of the parent type
		String ext = compType.getExtends();
		if (null != ext) {
			ComponentType base = lems.getComponentTypeByName(ext);
			CopyComponentTypeDef typeCopier = new CopyComponentTypeDef(compType);
			//typeCopier.setTraverseFirst(true);
			base.accept(typeCopier);
		}

		return true;
	}

}
