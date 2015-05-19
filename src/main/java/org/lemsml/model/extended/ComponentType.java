package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlTransient;

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

}
