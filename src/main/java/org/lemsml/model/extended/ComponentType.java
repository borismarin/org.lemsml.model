package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.compiler.INamed;

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

}
