package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

/**
 * @author borismarin
 *
 */
public class TypeToComponentVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;

	/**
	 * @param lems
	 * @throws Throwable
	 */
	public TypeToComponentVisitor(Lems lems) {
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
