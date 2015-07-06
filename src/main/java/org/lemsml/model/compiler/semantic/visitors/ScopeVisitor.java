package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;

import javax.xml.namespace.QName;

import org.lemsml.model.Constant;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Parameter;
import org.lemsml.model.StateVariable;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.IScoped;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Scope;
import org.lemsml.model.extended.Symbol;
import org.lemsml.model.extended.TimeDerivative;
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
	public Boolean visit(Component comp) throws Throwable {
		this.context = comp;
		this.scope = comp.getScope();
		this.scope.setScopeName(comp.getId());
		this.scope.setUnitContext(this.lems.getSymbolToUnit());
		return null;
	}

	@Override
	public Boolean visit(Parameter par) throws Throwable {
		Component comp = (Component) context;
		String valDef = comp.getOtherAttributes().get(
				new QName(par.getName()));
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
		scope.define(new Symbol(derVar));
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
