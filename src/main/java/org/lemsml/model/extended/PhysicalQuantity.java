package org.lemsml.model.extended;

import static tec.units.ri.AbstractUnit.ONE;

import javax.measure.Unit;

import org.lemsml.model.compiler.IDimensionalEvaluable;

import tec.units.ri.quantity.NumberQuantity;

/**
 * @author borismarin
 *
 */
public class PhysicalQuantity implements IDimensionalEvaluable {

	public Double value;
	public Unit<?> unit;
	private String unitSymbol;

	public PhysicalQuantity(Double value, String unitSymbol) {
		this.value = value;
		this.unitSymbol = unitSymbol;
	}

	public PhysicalQuantity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Unit<?> getUnit() {
		return unit;
	}

	public void setUnit(Unit<?> unit) {
		if (null != unit) {
			this.unit = unit;
		} else {
			this.unit = ONE;
		}
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	public String getUnitSymbol() {
		return this.unitSymbol;
	}

	public void setUnitSymbol(String symb) {
		this.unitSymbol = symb;
	}

	@Override
	public String toString() {
		return "PhysicalQuantity [value=" + value + ", unit=" + unitSymbol
				+ "]";
	}

	public Double getValueInSI() {
		return new Double(NumberQuantity.of(value, unit).toSI().getValue()
				.doubleValue());
	}

	public void setValue(double val) {
		this.value = val;
	}

}
