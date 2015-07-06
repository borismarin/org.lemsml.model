package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

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
		IScoped {
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

	public Component withParameterValue(String pName, String val) {
		this.getOtherAttributes().put(new QName(pName), val);
		return this;
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

}
