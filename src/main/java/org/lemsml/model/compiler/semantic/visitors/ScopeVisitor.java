package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;

import org.lemsml.model.ConditionalDerivedVariable;
import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Parameter;
import org.lemsml.model.Requirement;
import org.lemsml.model.StateVariable;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Scope;
import org.lemsml.model.extended.Symbol;
import org.lemsml.model.extended.TimeDerivative;
import org.lemsml.model.extended.interfaces.IScoped;
import org.lemsml.visitors.BaseVisitor;

public class ScopeVisitor extends BaseVisitor<Boolean, Throwable> {

	private IScoped context;
	private Scope scope;
	private Lems lems;

	public ScopeVisitor(IScoped context, Lems lems) {
		this.lems = lems;
		this.context = context;
		this.scope = (Scope) context.getScope();
		this.scope.setUnitContext(this.lems.getSymbolToUnit());
	}

	@Override
	public Boolean visit(org.lemsml.model.Lems lems) throws Throwable {
		Lems elems = (Lems) lems;
		this.scope = elems.getScope();
		this.scope.setBelongsTo(elems);
		return null;
	}

	@Override
	public Boolean visit(Component comp) throws Throwable {
		this.context = comp;
		this.scope = comp.getScope();
		this.scope.setScopeName(comp.getId());
		this.scope.setBelongsTo(comp);
		this.scope.setUnitContext(this.lems.getSymbolToUnit());
		return null;
	}

	@Override
	public Boolean visit(Parameter par) throws Throwable {
		Component comp = (Component) context;
		String valDef = comp.getParameterValue(par);
		if (null == valDef) {
			String msg = MessageFormat.format(
					"Component [({0}) {1}] must define parameter {2}",
					comp.getType(),
					comp.getName(),
					par.getName());
			throw new LEMSCompilerException(msg,
					LEMSCompilerError.RequiredParameterUndefined);
		}
		scope.define(new Symbol(par, valDef));
		return true;
	}

	@Override
	public Boolean visit(DerivedParameter par) throws Throwable {
		scope.define(new Symbol(par));
		return true;
	}

	@Override
	public Boolean visit(Constant constant) throws Throwable {
		scope.define(new Symbol(constant));
		return true;
	}

	@Override
	public Boolean visit(DerivedVariable derVar) throws Throwable {
		Symbol sym = new Symbol(derVar);
		scope.define(sym);
		return true;
	}

	@Override
	public Boolean visit(ConditionalDerivedVariable derVar) throws Throwable {
		Symbol sym = new Symbol(derVar);
		scope.define(sym);
		return true;
	}

	@Override
	public Boolean visit(Requirement req) throws Throwable {
		scope.define(new Symbol(req, req.getName()));
		return true;
	}


	@Override
	public Boolean visit(StateVariable x) throws Throwable {
		scope.define(new Symbol(x, x.getName()));
		return true;
	}

	@Override
	public Boolean visit(TimeDerivative dx) throws Throwable {
		scope.define(new Symbol(dx));
		return true;
	}

}
