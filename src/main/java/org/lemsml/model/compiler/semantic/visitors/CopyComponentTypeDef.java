package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Constant;
import org.lemsml.model.Dynamics;
import org.lemsml.model.Parameter;
import org.lemsml.model.Requirement;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class CopyComponentTypeDef extends TraversingVisitor<Boolean, Throwable> {

	private ComponentType targetType;

	public CopyComponentTypeDef(ComponentType compType) {
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
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
