package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.compiler.INamed;
import org.lemsml.visitors.Visitor;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class ComponentType extends org.lemsml.model.ComponentType implements INamed {
	
	ComponentType parent;

	public ComponentType getParent() {
		return parent;
	}

	public void setParent(ComponentType parent) {
		this.parent = parent;
	}

	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}
}
