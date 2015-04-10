package org.lemsml.model.extended;

import static tec.units.ri.AbstractUnit.ONE;

import javax.measure.Unit;

import tec.units.ri.quantity.NumberQuantity;

/**
 * @author borismarin
 *
 */
public class PhysicalQuantity {

	public Double value;
	public String unitSymbol;
	public Unit<?> unit;

	public Unit<?> getUnit() {
		return unit;
	}

	public void setUnit(Unit<?> unit) {
		if(null != unit){
			this.unit = unit;
		}else{
			this.unit = ONE;
		}

	}

	public Double getValue() {
		return value;
	}

	public String getUnitSymbol() {
		return unitSymbol;
	}

	public void setUnitSymbol(String unit) {
		this.unitSymbol = unit;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PhysicalQuantity [value=" + value + ", unit=" + unitSymbol
				+ "]";
	}
	
	public Double getValueInSI(){
		return new Double(NumberQuantity.of(value, unit).toSI().getValue().doubleValue());
	}

}
