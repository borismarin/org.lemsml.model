package org.lemsml.model.extended;

import javax.measure.Unit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.visitors.Visitor;

@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
public class Dimension extends org.lemsml.model.Dimension {
	// TODO: notice that there is a discrepancy between what LEMS calls
	// dimensions
	// and what UOM calls dimensions. We'll thus confusingly use Unit<?> here
	// to store a dimension...
	@XmlTransient
	private javax.measure.Unit<?> uomDimension;

	public javax.measure.Unit<?> getUOMDimension() {
		return uomDimension;
	}

	public void setUOMDimension(Unit<?> unit) {
		this.uomDimension = unit;
	}

	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}

}
