package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Parameter;
import org.lemsml.model.StateVariable;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.IScope;
import org.lemsml.model.extended.LemsNode;
import org.lemsml.model.extended.Symbol;
import org.lemsml.model.extended.TimeDerivative;
import org.lemsml.model.extended.SymbolicExpression;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 * @param <R>
 *
 */
public class BuildScope extends BaseVisitor<Boolean, Throwable> {

	private LemsNode context;

	/**
	 * @param lems
	 */
	public BuildScope(LemsNode context) {
		this.context = context;
	}

	@Override
	public Boolean visit(org.lemsml.model.Lems lems) throws Throwable {
		for (Component comp : lems.getComponents()) {
			comp.accept(this);
		}
		for (Constant ctt : lems.getConstants()) {
			ctt.accept(this);
		}
		return true;
	}

	@Override
	public Boolean visit(Component comp) throws Throwable {
		TraversingVisitor<Boolean, Throwable> scopeTraverser = new TraversingVisitor<Boolean, Throwable>(
				new DepthFirstTraverserExt<Throwable>(), new BuildScope(comp));
		ComponentType compType = comp.getComponentType();
		compType.accept(scopeTraverser);
		for(Component subComp : comp.getComponent()){
			subComp.accept(new BuildScope(subComp));
		}
		return true;
	}

	@Override
	public Boolean visit(Parameter par) throws Throwable {
		((Component) this.context).define(new Symbol<Parameter>(par.getName(),
				par));
		return true;
	}

	@Override
	public Boolean visit(DerivedParameter par) throws Throwable {
		((Component) this.context).define(new SymbolicExpression<DerivedParameter>(par
				.getName(), par));
		return true;
	}

	@Override
	public Boolean visit(Constant constant) throws Throwable {
		((IScope) this.context).define(new Symbol<Constant>(constant.getName(),
				constant));
		return true;
	}

	@Override
	public Boolean visit(DerivedVariable derVar) throws Throwable {
		((Component) this.context)
				.define(new SymbolicExpression<DerivedVariable>(derVar
						.getName(), derVar));
		return true;
	}

	@Override
	public Boolean visit(StateVariable x) throws Throwable {
		((Component) this.context)
				.define(new Symbol<StateVariable>(x.getName(), x));
		return true;
	}

	@Override
	public Boolean visit(TimeDerivative dx) throws Throwable {
		((Component) this.context)
				.define(new SymbolicExpression<TimeDerivative>(dx.getName(), dx));
		return true;
	}


}
