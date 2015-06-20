package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Constant;
import org.lemsml.model.Target;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

public class RemoveIncludes extends BaseVisitor<Boolean, Throwable> implements ILemsProcessor {

	private Lems processedLems;

	public RemoveIncludes() throws Throwable {
		processedLems = new Lems();
	}

	@Override
	public Boolean visit(Constant constant) {
		processedLems.getConstants().add(constant);
		return true;

	}

	@Override
	public Boolean visit(ComponentType componentType) {
		processedLems.getComponentTypes().add(componentType);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Component component) {
		processedLems.getComponents().add(component);
		return true;
	}

	@Override
	public Boolean visit(Target target) {
		processedLems.getTargets().add(target);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Dimension dimension) {
		processedLems.getDimensions().add(dimension);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Unit unit) {
		processedLems.getUnits().add((org.lemsml.model.extended.Unit) unit);
		return true;
	}

	@Override
	public Lems getLems() {
		return processedLems;
	}
}
