package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ComponentType;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Component extends org.lemsml.model.Component
{
	ComponentType _ComponentType;

	public ComponentType getComponentType()
	{
		return _ComponentType;
	}

	public void setComponentType(ComponentType _ComponentType)
	{
		this._ComponentType = _ComponentType;
	}

}
