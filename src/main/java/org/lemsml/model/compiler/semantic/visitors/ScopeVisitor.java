package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Parameter;
import org.lemsml.model.StateVariable;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.IScope;
import org.lemsml.model.extended.Symbol;
import org.lemsml.model.extended.SymbolicExpression;
import org.lemsml.model.extended.TimeDerivative;
import org.lemsml.visitors.BaseVisitor;

/**
 * @author borismarin
 * @param <R>
 *
 */
public class ScopeVisitor extends BaseVisitor<Boolean, Throwable> {

	private IScope context;

	public ScopeVisitor(IScope context) {
		this.context = context;
	}

//	@Override
//	public Boolean visit(Component comp) throws Throwable {
//		//TODO: can we delegate traversing logic to the traverser?
//		TraversingVisitor<Boolean, Throwable> scopeTraverser = new TraversingVisitor<Boolean, Throwable>(
//				new DepthFirstTraverserExt<Throwable>(), new ScopeVisitor(comp));
//		ComponentType compType = comp.getComponentType();
//		compType.accept(scopeTraverser);
//		for(Component subComp : comp.getComponent()){
//			subComp.accept(new ScopeVisitor(subComp));
//		}
//		return true;
//	}


	@Override
	public Boolean visit(Component comp) throws Throwable {
		this.context = comp;
		return null;
	}

	@Override
	public Boolean visit(Parameter par) throws Throwable {
		((Component) this.context).define(new Symbol<Parameter>(par.getName(), par));
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
