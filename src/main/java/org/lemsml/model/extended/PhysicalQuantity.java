package org.lemsml.model.extended;

import javax.measure.Unit;

/**
 * @author borismarin
 *
 */
public class PhysicalQuantity {

	public Float value;
	public String unitSymbol;
	public Unit<?> unit;

	public Unit<?> getUnit() {
		return unit;
	}

	public void setUnit(Unit<?> unit) {
		this.unit = unit;
	}

	public Float getValue() {
		return value;
	}

	public String getUnitSymbol() {
		return unitSymbol;
	}

	public void setUnitSymbol(String unit) {
		this.unitSymbol = unit;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PhysicalQuantity [value=" + value + ", unit=" + unitSymbol
				+ "]";
	}

}
