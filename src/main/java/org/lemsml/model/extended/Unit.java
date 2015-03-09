package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author borismarin
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Unit extends org.lemsml.model.Unit
{

	private javax.measure.Unit<?> unit;

	public javax.measure.Unit<?> getUnit()
	{
		return this.unit;
	}

	public void setUnit(javax.measure.Unit<?> unit)
	{
		this.unit = unit;
	}

}
