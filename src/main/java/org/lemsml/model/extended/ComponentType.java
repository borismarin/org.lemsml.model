package org.lemsml.model.extended;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.visitors.Visitor;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class ComponentType extends org.lemsml.model.ComponentType {
	
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
	
	public String toString(){
		return MessageFormat.format("[{0}]", this.getName());
	}
}
