package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.NONE)
public class Dimension extends org.lemsml.model.Dimension
{

	private javax.measure.Dimension dimension;

	public javax.measure.Dimension getDimension()
	{
		return dimension;
	}

	public void setDimension(javax.measure.Dimension dimension)
	{
		this.dimension = dimension;
	}

}
