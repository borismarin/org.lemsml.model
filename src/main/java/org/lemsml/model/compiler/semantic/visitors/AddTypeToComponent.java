package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.model.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Component;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class AddTypeToComponent extends TraversingVisitor<Boolean, Throwable> {

	private Lems lems;

	/**
	 * @param lems
	 */
	public AddTypeToComponent(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	@Override
	public Boolean visit(Component comp) throws Throwable {
		ComponentType typeToSet = lems.getComponentTypeByName(comp.getType());
		if (null == typeToSet) {
			throw new LEMSCompilerException(
					"Trying to build Component of unknow type "
							+ comp.getType(),
					LEMSCompilerError.ComponentTypeNotDefined);
		}
		comp.setComponentType(typeToSet);
		return true;
	}

}
