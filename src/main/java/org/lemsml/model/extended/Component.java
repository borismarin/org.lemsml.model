package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

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
	private Scope scope = new Scope(""); // TODO: ugly

	@XmlTransient
	private Component parent;

	@XmlTransient
	public Multimap<String, Component> nameBindings = ArrayListMultimap
			.create();

	public ComponentType getComponentType() {
		return _ComponentType;
	}

	public void setComponentType(ComponentType _ComponentType) {
		this._ComponentType = _ComponentType;
	}

	public Component withParameterValue(String pName, String val)
			throws LEMSCompilerException {
		this.getOtherAttributes().put(new QName(pName), val);
		try {
			Symbol resolved = getScope().resolve(pName);
			scope.define(new Symbol((NamedDimensionalType) resolved.getType(),
					val));
		} catch (LEMSCompilerException e) {
			if (!e.getErrorCode().equals(LEMSCompilerError.UndefinedSymbol)) {
				throw e;
			}
			// OK, we haven't compiled it yet
		}
		return this;
	}

	public Component withTextValue(String tName, String val)
			throws LEMSCompilerException {
		this.getOtherAttributes().put(new QName(tName), val);
		// TODO: error checking compiler pass
		// if (1 != getTextsWithName(tName).size()) {
		// String message = MessageFormat.format(
		// "Component [{0}] does not define Text field ({1})",
		// getComponentType().getName(), tName);
		// throw new LEMSCompilerException(message,
		// LEMSCompilerError.UndefinedTextField);
		// }
		return this;
	}

	public List<Component> getSubComponentsOfType(String type) {
		ArrayList<Component> comps = new ArrayList<Component>();
		for (Component subComp : getComponent()) {
			ComponentType subCompType = subComp.getComponentType();
			do {
				if (subCompType.getName().equals(type))
					comps.add(subComp);
			} while (null != (subCompType = subCompType.getSuperType()));
		}
		return comps;
	}

	public List<Component> getSubComponentsBoundToName(String name) {
		return new ArrayList<Component>(nameBindings.get(name));
		// return Lists.newArrayList(Iterables.filter(getComponent(),
		// isBoundTo(name)));
	}

	public List<Component> getSubComponentsWithName(String name) {
		return Lists.newArrayList(Iterables.filter(getComponent(), isNamed(name)));
	}

	public List<Component> getSubComponentsWithTextValue(String field, String value) {
		return Lists.newArrayList(Iterables.filter(getComponent(), hasTextNamedValued(field, value)));
	}

	public List<Component> getNamedChildrenWithTextValue(String name, String field, String value) {
		return Lists.newArrayList(Iterables.filter(getSubComponentsBoundToName(name), hasTextNamedValued(field, value)));
	}


	private static Predicate<Component> hasType(final String type) {
		return new Predicate<Component>() {
			@Override
			public boolean apply(Component input) {
				return input.getType() != null && input.getType().equals(type);
			}
		};
	}

	private static Predicate<INamed> isNamed(final String name) {
		return new Predicate<INamed>() {
			@Override
			public boolean apply(INamed input) {
				return input.getName() != null && input.getName().equals(name);
			}
		};
	}

	private static Predicate<Component> hasTextNamedValued(final String name, final String value) {
		return new Predicate<Component>() {
			@Override
			public boolean apply(Component input) {
				return input.getOtherAttributes().get(new QName(name)) != null && input.getOtherAttributes().get(new QName(name)).equals(value);
			}
		};
	}

	public String getParameterValue(Parameter par) {
		return getOtherAttributes().get(new QName(par.getName()));
	}

	public String getParameterValue(String pName) {
		return getOtherAttributes().get(new QName(pName));
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

	public Component getParentComponent() {
		return this.parent;
	}

	public void setParentComponent(Component comp) {
		this.parent = comp;
	}

	public List<Component> getComponents() {
		// TODO: can't get it pluralized via jaxb...
		return getComponent();
	}

	public void bindSubCompToName(Component sc, String name) {
		nameBindings.put(name, sc);

	}

	public <T extends Component> Set<T> getAllOfType(Class<T> type) {
		return getAllOfType(type, this);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> Set<T> getAllOfType(Class<T> type,
			HasComponents node) {
		Set<T> accum = new HashSet<T>();
		for (Component c : node.getComponents()) {
			accum.addAll(getAllOfType(type, c));
		}
		if (type.isInstance(node)) {
			accum.add((T) node);
		}
		return accum;
	}


}
