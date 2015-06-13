package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Constant;
import org.lemsml.model.Dynamics;
import org.lemsml.model.Parameter;
import org.lemsml.model.Requirement;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.visitors.BaseVisitor;

/**
 * @author borismarin
 *
 */
public class CopyComponentTypeDef extends BaseVisitor<Boolean, Throwable> {

	private ComponentType targetType;

	public CopyComponentTypeDef(ComponentType compType) {
		targetType = compType;
	}

	@Override
	public Boolean visit(Constant constant) {
		targetType.getConstants().add(constant);
		return true;
	}

	@Override
	public Boolean visit(Parameter par) {
		targetType.getParameters().add(par);
		return true;
	}

	@Override
	public Boolean visit(Dynamics dyn) {
		targetType.getDynamics().add(dyn);
		return true;
	}

	@Override
	public Boolean visit(Requirement req) {
		targetType.getRequirements().add(req);
		return true;
	}

}
