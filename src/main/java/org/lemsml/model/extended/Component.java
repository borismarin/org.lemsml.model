package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.INamed;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Component extends org.lemsml.model.Component implements IScope, INamed{
	@XmlTransient
	ComponentType _ComponentType;

	@XmlTransient
	public Map<String, ISymbol<?>> scope = new HashMap<String, ISymbol<?>>();

	@XmlTransient
	private IScope parent;


	public ComponentType getComponentType() {
		return _ComponentType;
	}

	public void setComponentType(ComponentType _ComponentType) {
		this._ComponentType = _ComponentType;
	}

	public ISymbol<Parameter> getParameterByName(String name)
			throws LEMSCompilerException {
		//Instance<Parameter> pVal = this.nameToParameterValue.get(name);
		@SuppressWarnings("unchecked")
		ISymbol<Parameter> parSym = (ISymbol<Parameter>) this.resolve(name);
		if (null != parSym) {
			return parSym;
		} else {
			// TODO: shouldn't this type of error be handled somewhere else?
			throw new LEMSCompilerException("ComponentType" + type
					+ " does not allow parameter " + name,
					LEMSCompilerError.ParameterNotAllowed);
		}
	}

	@Override
	public String getScopeName() {
		return this.getId();
	}

	@Override
	public IScope getEnclosingScope() {
		//TODO: add family to components...
		return this.parent;
	}

	@Override
	public ISymbol<?> define(ISymbol<?> sym) {
		return this.scope.put(sym.getName(), sym);
	}

	@Override
	public ISymbol<?> resolve(String name) {
		return this.scope.get(name);
	}

	public Map<String, ISymbol<?>> getScope() {
		return this.scope;
	}

	@Override
	public String getName() {
		return this.getId();
	}

	public void setName(String name) {
		this.id = name;
	}

	@Override
	public Set<String> getDefinedSymbols() {
		return this.scope.keySet();
	}

}
