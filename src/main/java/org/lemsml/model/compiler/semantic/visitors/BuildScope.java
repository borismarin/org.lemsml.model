package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.LemsNode;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class BuildScope extends TraversingVisitor<Boolean, Throwable> {

	private LemsNode context;

	/**
	 * @param lems
	 */
	public BuildScope(LemsNode context) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.context = context;
	}

	@Override
	public Boolean visit(Component comp) throws Throwable {
		ComponentType compType = comp.getComponentType();
		compType.accept(new BuildScope(comp));
//		for (Parameter parDef : compType.getParameters()) {
//			comp.define(new Symbol<Parameter>(parDef.getName(), parDef));
//		}
//		for (DerivedParameter derPar : compType.getDerivedParameters()) {
//			comp.define(new Symbol<DerivedParameter>(derPar.getName(), derPar));
//		}
//		for (Constant constant : compType.getConstants()) {
//			comp.define(new Symbol<Constant>(constant.getName(), constant));
//		}

		return true;
	}

	@Override
	public Boolean visit(Parameter par) throws Throwable {
		((Component) this.context).define(new Symbol<Parameter>(par.getName(), par));
		return true;
	}

	@Override
	public Boolean visit(DerivedParameter par) throws Throwable {
		((Component) this.context).define(new Symbol<DerivedParameter>(par.getName(), par));
		return true;
	}

	@Override
	public Boolean visit(Constant constant) throws Throwable {
		((IScope) this.context).define(new Symbol<Constant>(constant.getName(), constant));
		return true;
	}
}
