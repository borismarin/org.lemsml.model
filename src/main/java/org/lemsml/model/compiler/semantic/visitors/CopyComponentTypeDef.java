package org.lemsml.model.compiler.semantic.visitors;

import java.util.List;

import org.lemsml.model.ComponentReference;
import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.Dynamics;
import org.lemsml.model.Parameter;
import org.lemsml.model.Requirement;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.visitors.BaseVisitor;

/**
 * @author borismarin
 *
 */
class CopyComponentTypeDef extends BaseVisitor<Boolean, Throwable> {

	private ComponentType targetType;

	CopyComponentTypeDef(ComponentType compType) {
		targetType = compType;
	}

	@Override
	public Boolean visit(Constant constant) {
		List<Constant> constants = targetType.getConstants();
		if (!constants.contains(constant))
			constants.add(constant);
		return true;
	}

	@Override
	public Boolean visit(Parameter par) {
		List<Parameter> parameters = targetType.getParameters();
		if (!parameters.contains(par))
			parameters.add(par);
		return true;
	}

	@Override
	public Boolean visit(DerivedParameter dpar) {
		List<DerivedParameter> dparameters = targetType.getDerivedParameters();
		if (!dparameters.contains(dpar))
			dparameters.add(dpar);
		return true;
	}

	@Override
	public Boolean visit(Dynamics dyn) {
		List<Dynamics> dynamics = targetType.getDynamics();
		if (!dynamics.contains(dyn))
			dynamics.add(dyn);
		return true;
	}

	@Override
	public Boolean visit(Requirement req) {
		List<Requirement> requirements = targetType.getRequirements();
		if (!requirements.contains(req))
			requirements.add(req);
		return true;
	}

	@Override
	public Boolean visit(ComponentReference ref) {
		List<ComponentReference> refs = targetType.getComponentReferences();
		if (!refs.contains(ref))
			refs.add(ref);
		return true;
	}

}
