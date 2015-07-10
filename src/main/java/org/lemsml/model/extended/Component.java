package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

import org.lemsml.model.Parameter;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.interfaces.HasComponents;
import org.lemsml.model.extended.interfaces.INamed;
import org.lemsml.model.extended.interfaces.IScoped;
import org.lemsml.visitors.Visitor;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Component extends org.lemsml.model.Component implements INamed,
		IScoped, HasComponents {
	@XmlTransient
	private ComponentType _ComponentType;

	@XmlTransient
	private List<Component> children;

	@XmlTransient
	private Lems lemsRoot;

	@XmlTransient
	private Scope scope = new Scope(""); //TODO: ugly

	@XmlTransient
	private Component parent;

	public ComponentType getComponentType() {
		return _ComponentType;
	}

	public void setComponentType(ComponentType _ComponentType) {
		this._ComponentType = _ComponentType;
	}

	public Component withParameterValue(String pName, String val) throws LEMSCompilerException {
		this.getOtherAttributes().put(new QName(pName), val);
		try{
			Symbol resolved = getScope().resolve(pName);
			scope.define(new Symbol((NamedDimensionalType) resolved.getType(), val));
		} catch(LEMSCompilerException e){
			if(!e.getErrorCode().equals(LEMSCompilerError.UndefinedSymbol)){
				throw e;
			}
			//OK, we haven't compiled it yet
		}
		return this;
	}

	public String getParameterValue(Parameter par) {
		return getOtherAttributes().get(new QName(par.getName()));
	}

	public String getParameterValue(String pName) {
		return getOtherAttributes().get(new QName(pName));
	}

	public List<Component> getSubComponentsOfType(String type) {
		return Lists.newArrayList(Iterables.filter(getComponent(),
				hasType(type)));
	}

	public static Predicate<Component> hasType(final String type) {
		return new Predicate<Component>() {
			@Override
			public boolean apply(Component input) {
				return input.getType() != null && input.getType().equals(type);
			}
		};
	}

	public Scope getScope() {
		return this.scope;
	}

	@Override
	public String getName() {
		return this.getId();
	}

	public void setName(String name) {
		this.id = name;
	}

	public List<Component> getChildren() {
		return children;
	}

	public void setChildren(List<Component> c) {
		this.children = c;
	}

	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}

	public String toString() {
		return MessageFormat.format("({0}) {1}", this.getType(), this.getId());
	}

	public Component getParent() {
		return this.parent;
	}

	public void setParent(Component comp) {
		this.parent = comp;
	}

	public List<Component> getComponents(){
		//TODO: can't get it pluralized via jaxb...
		return getComponent();
	}

}
