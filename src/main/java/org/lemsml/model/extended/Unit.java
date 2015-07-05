package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.visitors.Visitor;

/**
 * @author borismarin
 *
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
public class Unit extends org.lemsml.model.Unit {

	private javax.measure.Unit<?> uomUnit;

	public javax.measure.Unit<?> getUOMUnit() {
		return this.uomUnit;
	}

	public void setUOMUnit(javax.measure.Unit<?> unit) {
		this.uomUnit = unit;
	}

	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}

}
