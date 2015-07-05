package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlTransient;


public abstract class NamedDimensionalType extends org.lemsml.model.NamedDimensionalType {
	@XmlTransient
	private javax.measure.Unit<?> uomDimension;

	public javax.measure.Unit<?> getUOMDimension() {
		return this.uomDimension;
	}

	public void setUOMDimension(javax.measure.Unit<?> unit) {
		this.uomDimension = unit;
	}

}
