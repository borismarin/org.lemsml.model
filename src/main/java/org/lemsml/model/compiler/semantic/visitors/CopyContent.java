package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Constant;
import org.lemsml.model.Target;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

/**
 * @author matteocantarelli
 * @author borismarin
 *
 */
class CopyContent extends BaseVisitor<Boolean, Throwable> {

	private Lems resolvedLems;

	CopyContent(org.lemsml.model.extended.Lems lems) throws Throwable {
		resolvedLems = lems;
	}

	@Override
	public Boolean visit(Constant constant) {
		resolvedLems.getConstants().add(constant);
		return true;

	}

	@Override
	public Boolean visit(ComponentType componentType) {
		resolvedLems.getComponentTypes().add(componentType);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Component component) {
		resolvedLems.getComponents().add(component);
		return true;
	}

	@Override
	public Boolean visit(Target target) {
		resolvedLems.getTargets().add(target);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Dimension dimension) {
		resolvedLems.getDimensions().add(dimension);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Unit unit) {
		resolvedLems.getUnits().add((org.lemsml.model.extended.Unit) unit);
		return true;
	}
}
